package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.RetryEngine;
import cat.hack3.mangrana.downloads.workers.sonarr.SerieRefresher;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.utils.EasyLogger;
import cat.hack3.mangrana.utils.Output;
import cat.hack3.mangrana.utils.PathUtils;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_SERIES_FOLDER_ID;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_TEAM_DRIVE_ID;
import static cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler.CLOUD_WAIT_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler.SONARR_WAIT_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.GrabInfo.*;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.EPISODE;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.SEASON;
import static cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;
import static cat.hack3.mangrana.utils.Output.msg;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SonarrJobHandler implements Runnable {

    private EasyLogger logger;

    public enum DownloadType {SEASON, EPISODE}
    ConfigFileLoader configFileLoader;
    SonarrApiGateway sonarrApiGateway;
    GoogleDriveApiGateway googleDriveApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;
    SonarrJobFile sonarrJobFile;

    String jobTitle="-not_set-";
    final SonarGrabbedDownloadsHandler orchestrator;

    private String fullTitle;
    private String elementName;
    private DownloadType type;
    private int serieId;
    private String fileName;
    private int episodeCount;
    private String downloadId;

    public SonarrJobHandler(ConfigFileLoader configFileLoader, SonarrJobFile sonarrJobFile, SonarGrabbedDownloadsHandler caller) throws IOException {
        this.configFileLoader = configFileLoader;
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        googleDriveApiGateway = new GoogleDriveApiGateway();
        serieRefresher = new SerieRefresher(configFileLoader);
        this.sonarrJobFile = sonarrJobFile;
        orchestrator = caller;
    }

    @Override
    public void run() {
        boolean error=false;
        try {
            loadInfoFromJobFile();
            log("going to handle the so called {0}", fullTitle);
            setJobStateInitiated();
            if (StringUtils.isNotEmpty(fileName)) {
                log("retrieved cached element name from file {0}", fileName);
                elementName = fileName;
            } else {
                Supplier<String> getOutputFromQueue = () -> {
                    logWhenActive("searching from Sonarr Queue downloadId="+downloadId);
                    try {
                        SonarrQueue queue = sonarrApiGateway.getQueue();
                        String outputPath = queue.getRecords()
                                .stream()
                                .filter(rcd -> downloadId.equals(rcd.getDownloadId()))
                                .findFirst()
                                .orElseThrow(() -> new NoElementFoundException("element not found in queue"))
                                .getOutputPath();
                        return PathUtils.getCurrentFromFullPath(outputPath);
                    } catch (NoElementFoundException e) {
                        logWhenActive("not found "+downloadId+" yet on queue, will retry later");
                        return null;
                    }
                };
                RetryEngine<String> retryEngineForQueue = new RetryEngine<>(
                        "SonarrQueueRecord",
                        SONARR_WAIT_INTERVAL,
                        this::logWhenActive);
                elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue);
                writeElementNameToJobInfo(elementName);
            }
            setJobStateWorkingOrSleep();

            if (EPISODE.equals(type)) {
                handleEpisode(true);
            } else {
                handleSeason(true);
            }
            sonarrJobFile.markDone();
        } catch (TooMuchTriesException | IOException | IncorrectWorkingReferencesException | NoElementFoundException e) {
            error=true;
            log("something wrong: "+e.getMessage());
            sonarrJobFile.driveBack();
            e.printStackTrace();
        } finally {
            setJobStateFinished(error);
        }
    }

    public void tryToMoveIfPossible() throws IOException, IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException {
        loadInfoFromJobFile();
        if (StringUtils.isEmpty(fileName)) {
            log("this job has not a fileName retrieved, so it cannot be processed.");
        } else {
            elementName = fileName;
            log("going to try handle the following element: "+elementName);
            if (EPISODE.equals(type)) {
                try {
                    googleDriveApiGateway.lookupElementByName(elementName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
                } catch (NoElementFoundException e) {
                    throw new NoElementFoundException("episode not downloaded yet");
                }
                handleEpisode(false);
            } else {
                try {
                    File parentFolder =  googleDriveApiGateway.lookupElementById(configFileLoader.getConfig(DOWNLOADS_SERIES_FOLDER_ID));
                    File season = googleDriveApiGateway.getChildFromParentByName(elementName, parentFolder, true);
                    List<File> episodes = googleDriveApiGateway.getChildrenFromParent(season, false);
                    if (episodes.size() < episodeCount) throw new NoElementFoundException(msg("some episode is missing: expected {0}, got {1}", episodeCount, episodes.size()));
                } catch (Exception e) {
                    throw new NoElementFoundException("season not downloaded yet");
                }
                handleSeason(false);
            }
            sonarrJobFile.markDone();
        }
    }

    private void loadInfoFromJobFile() {
        jobTitle = fullTitle.substring(0, 38)+"..";
        logger = new EasyLogger("*> "+jobTitle);
        fullTitle = sonarrJobFile.getInfo(SONARR_RELEASE_TITLE);
        downloadId = sonarrJobFile.getInfo(SONARR_DOWNLOAD_ID);
        episodeCount = Integer.parseInt(sonarrJobFile.getInfo(SONARR_RELEASE_EPISODECOUNT));
        type = episodeCount == 1 ? EPISODE : SEASON;
        serieId = Integer.parseInt(sonarrJobFile.getInfo(SONARR_SERIES_ID));
        fileName = sonarrJobFile.getInfo(JAVA_FILENAME);
    }

    private void handleEpisode(boolean waitUntilExists) throws IOException, IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException {
        if (waitUntilExists) copyService.setRetryEngine(new RetryEngine<>(
                "EpisodeOnGoogle",
                CLOUD_WAIT_INTERVAL/2,
                this::log)
        );
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromEpisode(elementName);
        copyService.copyEpisodeFromDownloadToItsLocation(fileName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void handleSeason(boolean waitUntilExists) throws IOException, IncorrectWorkingReferencesException, TooMuchTriesException, NoElementFoundException {
        if (waitUntilExists) {
            Function<File, List<File>> childrenRetriever = file ->
                    googleDriveApiGateway.getChildrenFromParent(file, false);
            Function<File, Boolean> fileNameConstraint = file -> !file.getName().endsWith(".part");
            RetryEngine<File> retryer = new RetryEngine<>(
                    "SeasonOnGoogle",
                    CLOUD_WAIT_INTERVAL,
                    new RetryEngine.ChildrenRequirements<>(episodeCount, childrenRetriever, fileNameConstraint),
                    this::log
            );
            copyService.setRetryEngine(retryer);
        }

        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromSeason(elementName);
        copyService.copySeasonFromDownloadToItsLocation(elementName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void writeElementNameToJobInfo(String elementName) throws IOException {
        try (Writer output = new BufferedWriter(
                new FileWriter(sonarrJobFile.getFile().getAbsolutePath(), true))) {
            output.append(JAVA_FILENAME.name().toLowerCase().concat(": "+elementName));
            log("persisted elementName to job file -> "+elementName);
        }
    }

    private void setJobStateInitiated() {
        synchronized (orchestrator) {
            orchestrator.jobInitiated(jobTitle);
        }
    }

    private void setJobStateWorkingOrSleep() {
        synchronized (orchestrator) {
            orchestrator.jobHasFileName(jobTitle);
            if (orchestrator.isWorkingWithAJob()) {
                log("this job will go to sleep zzz");
                try {
                    Instant beforeWait = Instant.now();
                    while (orchestrator.isWorkingWithAJob()) {
                        orchestrator.wait();
                    }
                    Instant afterWait = Instant.now();
                    if (Duration.between(beforeWait, afterWait).toMinutes()<1) {
                        logger.nHLog("seems that things are going too fast between job sleep and its resume");
                    }
                } catch (InterruptedException e) {
                    logger.nHLog("could not put on waiting the job {0}", jobTitle);
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                log("this job is waking up !.!");
            }
            sonarrJobFile.markDoing();
            orchestrator.jobWorking(jobTitle);
        }
    }

    private void setJobStateFinished(boolean error) {
        synchronized (orchestrator) {
            if (error) {
                orchestrator.jobError(jobTitle, fileName);
            } else {
                orchestrator.jobFinished(jobTitle, fileName);
            }
            orchestrator.notifyAll();
        }
    }

    private void log(String msg, Object... params) {
        logger.nLog(msg, params);
    }

    private void logWhenActive(String msg, Object... params){
        if (!orchestrator.isWorkingWithAJob()
                || orchestrator.isJobWorking(jobTitle)) {
            logger.nLog(msg, params);
        }
    }

    public String getFullTitle() {
        return fullTitle;
    }
}

package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.downloads.workers.RetryEngine;
import cat.hack3.mangrana.downloads.workers.sonarr.EpisodeHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SeasonHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SerieRefresher;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.utils.EasyLogger;
import cat.hack3.mangrana.utils.PathUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_RETRY_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.GrabInfo.*;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.EPISODE;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.SEASON;

public class SonarrJobHandler implements Runnable {

    private EasyLogger logger;

    public enum DownloadType {SEASON, EPISODE}
    final int sonarrWaitInterval;
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
        if (LocalEnvironmentManager.isLocal()){
            sonarrWaitInterval = 2;
        } else {
            sonarrWaitInterval = Integer.parseInt(configFileLoader.getConfig(SONARR_RETRY_INTERVAL));
        }
    }

    public void tryToMoveIfPossible() throws IOException, IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException {
        loadInfoFromJobFile();
        if (StringUtils.isEmpty(fileName)) {
            logger.nLog("this job has not a fileName retrieved, so it cannot be processed.");
        } else {
            elementName = fileName;
            logger.nLog("going to try handle the following element: "+elementName);
            if (EPISODE.equals(type)) {
                new EpisodeHandler(logger, configFileLoader).initValues(elementName, serieId).crashHandle();
            } else {
                new SeasonHandler(logger, configFileLoader).initValues(elementName, serieId, episodeCount).crashHandle();
            }
            sonarrJobFile.markDone();
        }
    }

    private void loadInfoFromJobFile() {
        fullTitle = sonarrJobFile.getInfo(SONARR_RELEASE_TITLE);
        jobTitle = fullTitle.substring(0, 38)+"..";
        logger = new EasyLogger("*> "+jobTitle);
        downloadId = sonarrJobFile.getInfo(SONARR_DOWNLOAD_ID);
        episodeCount = Integer.parseInt(sonarrJobFile.getInfo(SONARR_RELEASE_EPISODECOUNT));
        type = episodeCount == 1 ? EPISODE : SEASON;
        serieId = Integer.parseInt(sonarrJobFile.getInfo(SONARR_SERIES_ID));
        fileName = sonarrJobFile.getInfo(JAVA_FILENAME);
    }

    @Override
    public void run() {
        boolean error=false;
        try {
            loadInfoFromJobFile();
            logger.nLog("going to handle the so called {0}", fullTitle);
            setJobStateInitiated();
            if (StringUtils.isNotEmpty(fileName)) {
                logger.nLog("retrieved cached element name from file {0}", fileName);
                elementName = fileName;
            } else {
                retrieveFileNameFromSonarr();
                writeElementNameToJobInfo(elementName);
            }
            setJobStateWorkingOrSleep();

            if (EPISODE.equals(type)) {
                new EpisodeHandler(logger, configFileLoader).initValues(elementName, serieId).handle();
            } else {
                new SeasonHandler(logger, configFileLoader).initValues(elementName, serieId, episodeCount).handle();
            }
            sonarrJobFile.markDone();
        } catch (TooMuchTriesException | IOException | IncorrectWorkingReferencesException | NoElementFoundException e) {
            error=true;
            logger.nLog("something wrong: "+e.getMessage());
            sonarrJobFile.driveBack();
            e.printStackTrace();
        } finally {
            setJobStateFinished(error);
        }
    }

    private void retrieveFileNameFromSonarr() throws TooMuchTriesException {
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
                sonarrWaitInterval,
                this::logWhenActive);
        elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue);
    }

    private void writeElementNameToJobInfo(String elementName) throws IOException {
        try (Writer output = new BufferedWriter(
                new FileWriter(sonarrJobFile.getFile().getAbsolutePath(), true))) {
            output.append(JAVA_FILENAME.name().toLowerCase().concat(": "+elementName));
            logger.nLog("persisted elementName to job file -> "+elementName);
        }
    }

    private void setJobStateWorkingOrSleep() {
        synchronized (orchestrator) {
            orchestrator.jobHasFileName(jobTitle);
            if (orchestrator.isWorkingWithAJob()) {
                logger.nLog("this job will go to sleep zzz");
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
                logger.nLog("this job is waking up !.!");
            }
            sonarrJobFile.markDoing();
            orchestrator.jobWorking(jobTitle);
        }
    }

    private void setJobStateInitiated() {
        synchronized (orchestrator) {
            orchestrator.jobInitiated(jobTitle);
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

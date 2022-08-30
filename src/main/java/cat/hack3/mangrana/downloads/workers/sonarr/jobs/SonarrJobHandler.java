package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.RetryEngine;
import cat.hack3.mangrana.downloads.workers.sonarr.SerieRefresher;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;
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
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import static cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler.CLOUD_WAIT_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler.SONARR_WAIT_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.GrabInfo.*;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.EPISODE;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.SEASON;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SonarrJobHandler implements Runnable {

    public enum DownloadType {SEASON, EPISODE}
    SonarrApiGateway sonarrApiGateway;
    GoogleDriveApiGateway googleDriveApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;
    SonarrJobFile sonarrJobFile;

    String jobTitle="-not_set-";
    final SonarGrabbedDownloadsHandler orchestrator;

    public SonarrJobHandler(ConfigFileLoader configFileLoader, SonarrJobFile sonarrJobFile, SonarGrabbedDownloadsHandler caller) throws IOException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        googleDriveApiGateway = new GoogleDriveApiGateway();
        serieRefresher = new SerieRefresher(configFileLoader);
        this.sonarrJobFile = sonarrJobFile;
        orchestrator = caller;
    }

    @Override
    public void run() {
        try {
            String fullTitle = sonarrJobFile.getInfo(SONARR_RELEASE_TITLE);
            jobTitle = fullTitle.substring(0, 38)+"..";
            log("going to handle the so called: "+fullTitle);
            synchronized (orchestrator) {
                orchestrator.jobInitiated(jobTitle);
            }
            String downloadId = sonarrJobFile.getInfo(SONARR_DOWNLOAD_ID);
            int episodeCount = Integer.parseInt(sonarrJobFile.getInfo(SONARR_RELEASE_EPISODECOUNT));
            DownloadType type = episodeCount == 1 ? EPISODE : SEASON;
            int serieId = Integer.parseInt(sonarrJobFile.getInfo(SONARR_SERIES_ID));
            String fileName = sonarrJobFile.getInfo(JAVA_FILENAME);

            String elementName;
            if (StringUtils.isNotEmpty(fileName)) {
                log("retrieved cached element name from file :D -> "+fileName);
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
                                .orElseThrow(() -> new NoSuchElementException("element not found in queue"))
                                .getOutputPath();
                        return PathUtils.getCurrentFromFullPath(outputPath);
                    } catch (NoSuchElementException e) {
                        logWhenActive("not found "+downloadId+" yet on queue, will retry later");
                        return null;
                    }
                };
                RetryEngine<String> retryEngineForQueue = new RetryEngine<>(SONARR_WAIT_INTERVAL, this::logWhenActive);
                elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue);
                writeElementNameToJobInfo(elementName);
            }
            synchronized (orchestrator) {
                orchestrator.jobHasFileName(jobTitle);
                if (orchestrator.isWorkingWithAJob()) {
                    holdOn();
                }
                sonarrJobFile.markDoing();
                orchestrator.jobWorking(jobTitle);
            }

            if (EPISODE.equals(type)) {
                handleEpisode(serieId, elementName);
            } else {
                handleSeason(serieId, elementName, episodeCount);
            }
            sonarrJobFile.markDone();
        } catch (Exception e) {
            log("something wrong: "+e.getMessage());
            sonarrJobFile.driveBack();
            e.printStackTrace();
        } finally {
            synchronized (orchestrator) {
                orchestrator.jobFinished(jobTitle);
                resumeOtherJobs();
            }
        }
    }

    private void handleEpisode(int serieId, String fileName) throws IOException, IncorrectWorkingReferencesException {
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromEpisode(fileName);
        copyService.setRetryEngine(new RetryEngine<>(CLOUD_WAIT_INTERVAL/2, this::log));
        copyService.copyEpisodeFromDownloadToItsLocation(fileName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void handleSeason(int serieId, String folderName, int episodesMustHave) throws IOException, IncorrectWorkingReferencesException {
        Function<File, List<File>> childrenRetriever = file ->
                googleDriveApiGateway.getChildrenFromParent(file, false);
        Function<File, Boolean> fileNameConstraint = file -> !file.getName().endsWith(".part");
        RetryEngine<File> retryer = new RetryEngine<>(
                CLOUD_WAIT_INTERVAL,
                new RetryEngine.ChildrenRequirements<>(episodesMustHave, childrenRetriever, fileNameConstraint),
                this::log);
        copyService.setRetryEngine(retryer);

        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromSeason(folderName);
        copyService.copySeasonFromDownloadToItsLocation(folderName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void writeElementNameToJobInfo(String elementName) throws IOException {
        try (Writer output = new BufferedWriter(
                new FileWriter(sonarrJobFile.getFile().getAbsolutePath(), true))) {
            output.append(JAVA_FILENAME.name().toLowerCase().concat(": "+elementName));
            log("persisted elementName to job file -> "+elementName);
        }
    }

    public void holdOn(){
        synchronized (orchestrator) {
            log("job received hold on order");
            try {
                Instant beforeWait = Instant.now();
                while (orchestrator.isWorkingWithAJob()) {
                    log("job going to sleep");
                    orchestrator.wait();
                    log("job waking up");
                }
                Instant afterWait = Instant.now();
                if (Duration.between(beforeWait, afterWait).toMinutes()<1) {
                    log("seems that things are going too fast, sleeping few seconds");
                    Thread.sleep(30000);
                }
            } catch (InterruptedException e) {
                log("could not put on waiting the job " + jobTitle);
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            log("job will resume");
        }
    }

    public void resumeOtherJobs(){
        log("job FINISHED and will order a GLOBAL RESUME");
        synchronized (orchestrator) {
            orchestrator.notifyAll();
        }
    }

    private void log(String msg) {
        Output.log(jobTitle+": "+msg);
    }

    private void logWhenActive(String msg){
        if (!orchestrator.isWorkingWithAJob()
                || orchestrator.isJobWorking(jobTitle)) {
            log(msg);
        }
    }
}

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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import static cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler.CLOUD_WAIT_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler.SONARR_WAIT_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader.GrabInfo.*;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SonarrJobHandler implements Runnable {

    SonarrApiGateway sonarrApiGateway;
    GoogleDriveApiGateway googleDriveApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;
    SonarrJobFileLoader sonarrJobFileLoader;

    String downloadId="-not_set-";
    final SonarGrabbedDownloadsHandler orchestrator;

    public SonarrJobHandler(ConfigFileLoader configFileLoader, SonarrJobFileLoader sonarrJobFileLoader, SonarGrabbedDownloadsHandler caller) throws IOException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        googleDriveApiGateway = new GoogleDriveApiGateway();
        serieRefresher = new SerieRefresher(configFileLoader);
        this.sonarrJobFileLoader = sonarrJobFileLoader;
        orchestrator = caller;
    }

    @Override
    public void run() {
        try {
            downloadId = sonarrJobFileLoader.getInfo(SONARR_DOWNLOAD_ID);
            orchestrator.jobInitiated(downloadId);
            log("going to handle the so called: "+sonarrJobFileLoader.getInfo(SONARR_RELEASE_TITLE));
            int episodeCount = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_RELEASE_EPISODECOUNT));
            SonarGrabbedDownloadsHandler.DownloadType type = episodeCount == 1 ? SonarGrabbedDownloadsHandler.DownloadType.EPISODE : SonarGrabbedDownloadsHandler.DownloadType.SEASON;
            int serieId = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_SERIES_ID));
            String fileName = sonarrJobFileLoader.getInfo(JAVA_FILENAME);

            Supplier<String> getOutputFromQueue = () -> {
                log("searching from Sonarr Queue downloadId="+downloadId);
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
                    log("not found yet on queue, will retry later");
                    return null;
                }
            };

            String elementName;
            if (StringUtils.isNotEmpty(fileName)) {
                log("retrieved cached element name from file :D -> "+fileName);
                elementName = fileName;
            } else {
                RetryEngine<String> retryEngineForQueue = new RetryEngine<>(SONARR_WAIT_INTERVAL);
                elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue, t -> holdOn());
                sonarrJobFileLoader.markDoing();
                orchestrator.setWorkingWithAJob(true);
                writeElementNameToJobInfo(elementName);
            }
            orchestrator.jobHasFileName(downloadId);

            if (SonarGrabbedDownloadsHandler.DownloadType.EPISODE.equals(type)) {
                handleEpisode(serieId, elementName);
            } else {
                handleSeason(serieId, elementName, episodeCount);
            }
            sonarrJobFileLoader.markDone();
            resumeOtherJobs();
        } catch (Exception e) {
            log("something wrong: "+e.getMessage());
            sonarrJobFileLoader.driveBack();
            e.printStackTrace();
        } finally {
            orchestrator.jobFinished(downloadId);
        }
    }

    private void handleEpisode(int serieId, String fileName) throws IOException, IncorrectWorkingReferencesException {
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromEpisode(fileName);
        copyService.setRetryEngine(new RetryEngine<>(CLOUD_WAIT_INTERVAL/2));
        copyService.copyEpisodeFromDownloadToItsLocation(fileName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void handleSeason(int serieId, String folderName, int episodesMustHave) throws IOException, IncorrectWorkingReferencesException {
        Function<File, List<File>> childrenRetriever = file ->
                googleDriveApiGateway.getChildrenFromParent(file, false);
        copyService.setRetryEngine(new RetryEngine<>(CLOUD_WAIT_INTERVAL, episodesMustHave, childrenRetriever));

        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromSeason(folderName);
        copyService.copySeasonFromDownloadToItsLocation(folderName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void writeElementNameToJobInfo(String elementName) throws IOException {
        try (Writer output = new BufferedWriter(
                new FileWriter(sonarrJobFileLoader.getFile().getAbsolutePath(), true))) {
            output.append(JAVA_FILENAME.name().toLowerCase().concat(": "+elementName));
            log("persisted elementName to job file -> "+elementName);
        }
    }

    public void holdOn(){
        synchronized (orchestrator) {
            log("job received hold on order");
            try {
                while (orchestrator.isWorkingWithAJob()) {
                    log("job going to sleep");
                    orchestrator.wait();
                    log("job waking up");
                }
            } catch (InterruptedException e) {
                log("could not put on waiting the job " + downloadId);
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
            log("job will resume");
        }
    }

    public void resumeOtherJobs(){
        log("job finished and will order a global resume");
        synchronized (orchestrator) {
            orchestrator.setWorkingWithAJob(false);
            orchestrator.notifyAll();
        }
    }

    private void log(String msg) {
        Output.log(downloadId+": "+msg);
    }
}

package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.Handler;
import cat.hack3.mangrana.downloads.workers.RetryEngine;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;
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

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader.GrabInfo.*;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SonarGrabbedDownloadsHandler implements Handler {

    SonarrApiGateway sonarrApiGateway;
    GoogleDriveApiGateway googleDriveApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;
    SonarrJobFileLoader sonarrJobFileLoader;

    public enum DownloadType {SEASON, EPISODE}

    private static final int CLOUD_WAIT_INTERVAL = 30;

    public SonarGrabbedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException, IncorrectWorkingReferencesException {
        sonarrJobFileLoader = new SonarrJobFileLoader(configFileLoader);
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        googleDriveApiGateway = new GoogleDriveApiGateway();
        serieRefresher = new SerieRefresher(configFileLoader);
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        new SonarGrabbedDownloadsHandler(configFileLoader).handle();
    }

    @Override
    public void handle() {
        try {
            log("going to handle the so called: "+sonarrJobFileLoader.getInfo(SONARR_RELEASE_TITLE));
            int episodeCount = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_RELEASE_EPISODECOUNT));
            DownloadType type = episodeCount == 1 ? DownloadType.EPISODE : DownloadType.SEASON;
            int serieId = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_SERIES_ID));
            String downloadId = sonarrJobFileLoader.getInfo(SONARR_DOWNLOAD_ID);
            String fileName = sonarrJobFileLoader.getInfo(JAVA_FILENAME);
            sonarrJobFileLoader.markDoing();

            Supplier<String> getOutputFromQueue = () -> {
                log("searching queue for element "+downloadId);
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
                    log("not found yet on queue, will retry");
                    return null;
                }
            };

            String elementName;
            if (StringUtils.isNotEmpty(fileName)) {
                elementName = fileName;
            } else {
                RetryEngine<String> retryEngineForQueue = new RetryEngine<>(15);
                elementName = retryEngineForQueue.tryWaitAndRetry(getOutputFromQueue);
                writeElementNameToJobInfo(elementName);
            }
            if (DownloadType.EPISODE.equals(type)) {
                handleEpisode(serieId, elementName);
            } else {
                handleSeason(serieId, elementName, episodeCount);
            }
            sonarrJobFileLoader.markDone();
        } catch (Exception e) {
            log("something wrong: "+e.getMessage());
            sonarrJobFileLoader.driveBack();
            e.printStackTrace();
        }
    }

    private void handleEpisode(int serieId, String fileName) throws IOException, IncorrectWorkingReferencesException {
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromEpisode(fileName);
        copyService.setRetryEngine(new RetryEngine<>(CLOUD_WAIT_INTERVAL));
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
            log("persisted elementName to job file");
        }
    }

}

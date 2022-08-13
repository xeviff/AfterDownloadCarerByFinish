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

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader.GrabInfo.*;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SonarrFinishedDownloadsHandler implements Handler {

    SonarrApiGateway sonarrApiGateway;
    GoogleDriveApiGateway googleDriveApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;
    SonarrJobFileLoader sonarrJobFileLoader;

    public enum DownloadType {SEASON, EPISODE}

    public SonarrFinishedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException, IncorrectWorkingReferencesException {
        sonarrJobFileLoader = new SonarrJobFileLoader();
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        googleDriveApiGateway = new GoogleDriveApiGateway();
        serieRefresher = new SerieRefresher(configFileLoader);
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        new SonarrFinishedDownloadsHandler(configFileLoader).handle();
    }

    @Override
    public void handle() {
        try {
            int episodeCount = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_RELEASE_EPISODECOUNT));
            DownloadType type = episodeCount == 1 ? DownloadType.EPISODE : DownloadType.SEASON;
            int serieId = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_SERIES_ID));
            String downloadId = sonarrJobFileLoader.getInfo(SONARR_DOWNLOAD_ID);
            String fileName = sonarrJobFileLoader.getInfo(JAVA_FILENAME);

            Supplier<String> getOutputFromQueue = () -> {
                SonarrQueue queue = sonarrApiGateway.getQueue();
                String outputPath = queue.getRecords()
                        .stream()
                        .filter(rcd -> downloadId.equals(rcd.getDownloadId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("not found"))
                        .getOutputPath();
                return DownloadType.SEASON.equals(type)
                        ? PathUtils.getCurrentFromFullPath(outputPath)
                        : outputPath;
            };

            String elementName = StringUtils.isNotEmpty(fileName)
                    ? fileName
                    : getOutputFromQueue.get();

            if (DownloadType.EPISODE.equals(type)) {
                handleEpisode(serieId, elementName);
            } else {
                handleSeason(serieId, elementName, episodeCount);
            }

        } catch (Exception e) {
            log("something wrong: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEpisode(int serieId, String fileName) throws IOException, IncorrectWorkingReferencesException {
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromEpisode(fileName);
        copyService.setRetryEngine(new RetryEngine(10));
        copyService.copyEpisodeFromDownloadToItsLocation(fileName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void handleSeason(int serieId, String folderName, int episodesMustHave) throws IOException, IncorrectWorkingReferencesException {
        Function<File, List<File>> childrenRetriever = file ->
                googleDriveApiGateway.getChildrenFromParent(file, false);
        copyService.setRetryEngine(new RetryEngine(10, episodesMustHave, childrenRetriever));

        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromSeason(folderName);
        copyService.copySeasonFromDownloadToItsLocation(folderName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }
}

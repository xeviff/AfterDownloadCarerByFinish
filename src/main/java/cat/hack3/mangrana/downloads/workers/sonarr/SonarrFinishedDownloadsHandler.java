package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.Handler;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;

import java.io.IOException;

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader.GrabInfo.*;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SonarrFinishedDownloadsHandler implements Handler {

    SonarrApiGateway sonarrApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;
    SonarrJobFileLoader sonarrJobFileLoader;

    public enum DownloadType {SEASON, EPISODE}

    public SonarrFinishedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException, IncorrectWorkingReferencesException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        serieRefresher = new SerieRefresher(configFileLoader);

    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        new SonarrFinishedDownloadsHandler(configFileLoader).handle();
    }

    @Override
    public void handle() {
        try {
            int episodeCount = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_RELEASE_EPISODENUMBERS));
            DownloadType type = episodeCount == 1 ? DownloadType.EPISODE : DownloadType.SEASON;
            int serieId = Integer.parseInt(sonarrJobFileLoader.getInfo(SONARR_SERIES_ID));
            //TODO get fileName from Queue once actually grabbed
            String fileName = sonarrJobFileLoader.getInfo(FILE_NAME);
            if (DownloadType.EPISODE.equals(type)) {
                handleEpisode(serieId, fileName);
            } else {
                handleSeason(serieId, fileName);
            }

        } catch (Exception e) {
            log("something wrong: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEpisode(int serieId, String fileName) throws IOException, IncorrectWorkingReferencesException {
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromEpisode(fileName);
        copyService.copyEpisodeFromDownloadToItsLocation(fileName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    private void handleSeason(int serieId, String folderName) throws IOException, IncorrectWorkingReferencesException {
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromSeason(folderName);
        copyService.copySeasonFromDownloadToItsLocation(folderName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }
}

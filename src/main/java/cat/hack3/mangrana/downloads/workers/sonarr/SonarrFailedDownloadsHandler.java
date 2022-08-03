package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.sonarr.bean.Season;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.plex.url.PlexCommandLauncher;
import cat.hack3.mangrana.radarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.Record;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.utils.StringCaptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cat.hack3.mangrana.utils.Output.log;
import static java.util.stream.Collectors.groupingBy;

public class SonarrFailedDownloadsHandler {

    SonarrApiGateway sonarrApiGateway;
    RemoteCopyService copyService;
    PlexCommandLauncher plexCommander;


    public SonarrFailedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        plexCommander = new PlexCommandLauncher(configFileLoader);
    }

    public void handle () {
        log("this is the sonarr failed downloads handler. Scanning queue...");
        SonarrQueue queue = sonarrApiGateway.getQueue();
        processRecords(queue.getRecords());
    }

    private void processRecords(List<Record> queue) {
        Map<String, List<Record>> recordsGroupedByTitle =
            queue
                .stream()
                .collect(groupingBy(Record::getTitle));

        handleSeasons(
                recordsGroupedByTitle.entrySet()
                    .stream()
                    .filter(recordGroup -> recordGroup.getValue().size() > 1)
                    .map(this::buildSeason)
                    .collect(Collectors.toList()));

        handleSingleEpisodes(
                recordsGroupedByTitle.values()
                        .stream()
                        .filter(records -> records.size() == 1)
                        .map(list -> list.get(0))
                        .collect(Collectors.toList()));
    }

    private void handleSeasons(List<Season> recordsByTitle) {
        log("going to handle seasons");
        recordsByTitle.forEach(this::handleSeason);
    }

    private void handleSingleEpisodes(List<Record> episodeRecords) {
        log("going to handle episodes");
        episodeRecords.forEach(this::handleEpisode);
    }

    private void handleSeason(Season season) {
        try {
            SonarrSerie serie = sonarrApiGateway.getSerieById(season.getSerieId());
            copyService.copySeasonFromDownloadToItsLocation(
                    season.getDownloadedFolderName(),
                    serie.getPath(),
                    getSeasonFolderNameFromSeason(season.getDownloadedFolderName())
            );
            refreshSerieInSonarrAndPlex(serie, season.getQueueItemId());
            log("season handled!");
        } catch (Exception e) {
            log("could not handle the season because of "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEpisode(Record episodeRecord) {
        try {
            String absolutePathWithFile = episodeRecord.getOutputPath();
            SonarrSerie serie = sonarrApiGateway.getSerieById(episodeRecord.getSeriesId());
            copyService.copyEpisodeFromDownloadToItsLocation(
                    absolutePathWithFile.substring(absolutePathWithFile.lastIndexOf('/')+1),
                    serie.getPath(),
                    getSeasonFolderNameFromEpisode(episodeRecord.getTitle())
            );
            refreshSerieInSonarrAndPlex(serie, episodeRecord.getId());
            log("episode handled!");
        } catch (Exception e) {
            log("could not handle the episode because of "+e.getMessage());
            e.printStackTrace();
        }
    }

    private void refreshSerieInSonarrAndPlex(SonarrSerie serie, Integer queueElementId) {
        sonarrApiGateway.refreshSerie(serie.getId());
        sonarrApiGateway.deleteQueueElement(queueElementId);
        String plexSeriePath = serie.getPath().replaceFirst("/tv", "/mnt/mangrana_series");
        plexCommander.scanByPath(plexSeriePath);
    }

    private String getSeasonFolderNameFromSeason(String seasonFolderName) throws IncorrectWorkingReferencesException {
        String season = Optional.ofNullable(
                StringCaptor.getMatchingSubstring(seasonFolderName, "(S\\d{2})"))
                .orElseThrow(() ->
                        new IncorrectWorkingReferencesException("Couldn't determinate the season from: "+seasonFolderName));
        return season.replaceFirst("S", "Temporada ");
    }

    private String getSeasonFolderNameFromEpisode(String episodeFileName) throws IncorrectWorkingReferencesException {
        String episodeInfo = Optional.ofNullable(
                        StringCaptor.getMatchingSubstring(episodeFileName, "(S\\d{2}E\\d{2})"))
                .orElseThrow(() ->
                        new IncorrectWorkingReferencesException("Couldn't determinate the episode from: "+episodeFileName));
        return "Temporada ".concat(episodeInfo.substring(1,3));
    }

    private Season buildSeason(Map.Entry<String, List<Record>> entry) {
        return new Season(entry.getKey(), entry.getValue().get(0));
    }

}

package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.sonarr.bean.Season;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.Record;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cat.hack3.mangrana.utils.Output.log;
import static java.util.stream.Collectors.groupingBy;

public class SonarrFailedDownloadsHandler {

    SonarrApiGateway sonarrApiGateway;
    RemoteCopyService copyService;


    public SonarrFailedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
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
        episodeRecords.forEach(ep -> log(ep.getTitle()));
    }

    private void handleSeason(Season season) {
        sonarrApiGateway.getSerieById(season.getSeriesId());
    }

    private Season buildSeason(Map.Entry<String, List<Record>> entry) {
        return new Season(entry.getKey(), entry.getValue().get(0).getSeriesId());
    }

}

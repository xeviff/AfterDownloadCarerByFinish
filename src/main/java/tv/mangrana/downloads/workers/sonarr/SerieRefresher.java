package tv.mangrana.downloads.workers.sonarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.plex.url.PlexCommandLauncher;
import tv.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import tv.mangrana.sonarr.api.schema.series.SonarrSerie;
import tv.mangrana.utils.Output;

public class SerieRefresher {

    SonarrApiGateway sonarrApiGateway;
    PlexCommandLauncher plexCommander;

    public SerieRefresher (ConfigFileLoader configFileLoader) {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        plexCommander = new PlexCommandLauncher(configFileLoader);
    }

    public void refreshSerieInSonarrAndPlex(SonarrSerie serie, Integer queueElementId) {
        try {
            sonarrApiGateway.refreshSerie(serie.getId());
            if (queueElementId != null) {
                sonarrApiGateway.deleteQueueElement(queueElementId);
            }
        } catch (Exception e) {
            Output.log("could have not refreshed the serie {0}-{2} because of unexpected error {1}. Stacktrace will be printed", serie.getId(), serie.getTitle(), e.getMessage());
            e.printStackTrace();
        }
        plexCommander.scanSerieByPath(serie.getPath());
    }

    public void refreshSerieInSonarrAndPlex(SonarrSerie serie) {
        refreshSerieInSonarrAndPlex(serie, null);
    }

}

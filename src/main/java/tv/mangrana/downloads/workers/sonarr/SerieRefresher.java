package tv.mangrana.downloads.workers.sonarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.plex.url.PlexCommandLauncher;
import tv.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import tv.mangrana.sonarr.api.schema.series.SonarrSerie;

public class SerieRefresher {

    SonarrApiGateway sonarrApiGateway;
    PlexCommandLauncher plexCommander;

    public SerieRefresher (ConfigFileLoader configFileLoader) {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        plexCommander = new PlexCommandLauncher(configFileLoader);
    }

    public void refreshSerieInSonarrAndPlex(SonarrSerie serie, Integer queueElementId) {
        sonarrApiGateway.refreshSerie(serie.getId());
        if (queueElementId != null) {
            sonarrApiGateway.deleteQueueElement(queueElementId);
        }
        plexCommander.scanByPath(serie.getPath());
    }

    public void refreshSerieInSonarrAndPlex(SonarrSerie serie) {
        refreshSerieInSonarrAndPlex(serie, null);
    }

}

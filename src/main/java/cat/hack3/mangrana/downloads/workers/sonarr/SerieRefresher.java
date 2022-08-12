package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.plex.url.PlexCommandLauncher;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;

public class SerieRefresher {

    SonarrApiGateway sonarrApiGateway;
    PlexCommandLauncher plexCommander;

    public SerieRefresher (ConfigFileLoader configFileLoader) {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        plexCommander = new PlexCommandLauncher(configFileLoader);
    }

    public void refreshSerieInSonarrAndPlex(SonarrSerie serie, Integer queueElementId) {
        sonarrApiGateway.refreshSerie(serie.getId());
        if (queueElementId != null)
            sonarrApiGateway.deleteQueueElement(queueElementId);
        String plexSeriePath = serie.getPath().replaceFirst("/tv", "/mnt/mangrana_series");
        plexCommander.scanByPath(plexSeriePath);
    }

    public void refreshSerieInSonarrAndPlex(SonarrSerie serie) {
        refreshSerieInSonarrAndPlex(serie, null);
    }

}

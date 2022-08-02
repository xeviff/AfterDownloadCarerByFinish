package cat.hack3.mangrana.sonarr.api.client.gateway;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.radarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.sonarr.api.schema.command.RefreshSerieCommand;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.utils.rest.APIProxyBuilderSingleton;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_API_KEY;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_API_HOST;

public class SonarrApiGateway {

    private final String apiKey;
    private final SonarrAPIInterface proxy;

    public SonarrApiGateway(ConfigFileLoader config) {
        apiKey = config.getConfig(SONARR_API_KEY);
        proxy = APIProxyBuilderSingleton.getSonarrInterface(config.getConfig(SONARR_API_HOST));
    }

    public SonarrQueue getQueue() {
        return proxy.getQueue(apiKey);
    }
    @Deprecated
    public void deleteQueueElement(Integer idElement) {
        boolean isDeletingFromDownloadClient = true;
        if (!isDeletingFromDownloadClient) proxy.deleteQueueElement(idElement, apiKey);
    }

    public SonarrSerie getSerieById(Integer seriesId) {
        return proxy.getSerieById(seriesId, apiKey);
    }

    public void refreshSerie(Integer seriesId) {
        proxy.refreshSeriesCommand(new RefreshSerieCommand(seriesId), apiKey);
    }

}

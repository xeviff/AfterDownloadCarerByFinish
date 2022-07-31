package cat.hack3.mangrana.sonarr.api.client.gateway;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.radarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.utils.rest.APIProxyBuilderSingleton;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_API_KEY;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_HOST;

public class SonarrApiGateway {

    private final String apiKey;
    private final SonarrAPIInterface proxy;

    public SonarrApiGateway(ConfigFileLoader config) {
        apiKey = config.getConfig(SONARR_API_KEY);
        proxy = APIProxyBuilderSingleton.getSonarrInterface(config.getConfig(SONARR_HOST));
    }

    public SonarrQueue getQueue() {
        return proxy.getQueue(apiKey);
    }

    public SonarrSerie getSerieById(Integer seriesId) {
        return proxy.getSerieById(seriesId, apiKey);
    }

}

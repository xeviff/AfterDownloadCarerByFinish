package cat.hack3.mangrana.sonarr.api.client.gateway;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.sonarr.api.schema.command.RefreshSerieCommand;
import cat.hack3.mangrana.sonarr.api.schema.history.SonarrHistory;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.utils.Output;
import cat.hack3.mangrana.utils.rest.APIProxyBuilderSingleton;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_API_HOST;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_API_KEY;

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

    public void deleteQueueElement(Integer idElement) {
        proxy.deleteQueueElement(idElement, false, apiKey);
        log("sent Delete command to Sonarr for the queue element with id "+idElement);
    }

    public SonarrSerie getSerieById(Integer seriesId) {
        SonarrSerie serie = proxy.getSerieById(seriesId, apiKey);
        log("retrieved serie from sonarr with id "+seriesId);
        return serie;
    }

    public void refreshSerie(Integer seriesId) {
        proxy.refreshSeriesCommand(new RefreshSerieCommand(seriesId), apiKey);
        log("sent Refresh command to Sonarr for the serie with id "+seriesId);
    }

    public SonarrHistory getHistory () {
        return proxy.getHistory("date", "desc", 200, 1, apiKey);
    }

    private void log (String msg) {
        Output.log("SonarrApiGateway: "+msg);
    }

}

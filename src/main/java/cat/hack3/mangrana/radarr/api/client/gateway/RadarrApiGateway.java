package cat.hack3.mangrana.radarr.api.client.gateway;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.radarr.api.client.schema.command.RefreshMoviesCommand;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import cat.hack3.mangrana.utils.rest.APIProxyBuilderSingleton;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.RADARR_API_KEY;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.RADARR_API_HOST;
import static cat.hack3.mangrana.utils.Output.log;

public class RadarrApiGateway {

    private final String apiKey;
    private final RadarrAPIInterface proxy;

    public RadarrApiGateway(ConfigFileLoader config) {
        apiKey = config.getConfig(RADARR_API_KEY);
        proxy = APIProxyBuilderSingleton.getRadarrInterface(config.getConfig(RADARR_API_HOST));
    }

    public QueueResourcePagingResource getQueue() {
        return proxy.getQueue(true, apiKey);
    }

    public void removeQueueItem(int itemId) {
        proxy.removeQueueItem(itemId, false, apiKey);
        log("removed item from queue successfully: "+itemId);
    }

    public void refreshMovie(int movieId) {
        proxy.refreshMoviesCommand(new RefreshMoviesCommand(movieId), apiKey);
        log("refreshed movie with id "+movieId);
    }

}

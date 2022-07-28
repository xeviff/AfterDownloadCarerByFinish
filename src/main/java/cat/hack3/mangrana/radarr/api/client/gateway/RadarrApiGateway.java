package cat.hack3.mangrana.radarr.api.client.gateway;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;

import static cat.hack3.mangrana.utils.Output.log;

public class RadarrApiGateway {

    private final String apiKey;
    private final RadarrAPIInterface proxy;

    public RadarrApiGateway(ConfigFileLoader config) {
        apiKey = config.getRadarrApiKey();
        proxy = RadarrAPIInterfaceSingleton.getInterface(config.getRadarrHost());
    }

    public QueueResourcePagingResource getQueue() {
        return proxy.getQueue(true, apiKey);
    }

    public void removeQueueItem(int itemId) {
        proxy.removeQueueItem(itemId, false, apiKey);
        log("removed item from queue successfully: "+itemId);
    }

}

package cat.hack3.mangrana.radarr.api.client.gateway;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;

public class RadarrApiGateway {

    private final String apiKey;
    private final RadarrAPIInterface proxy;

    public RadarrApiGateway(ConfigFileLoader config) {
        apiKey = config.getApiKey();
        proxy = RadarrAPIInterfaceSingleton.getInterface(config.getHost());
    }

    public QueueResourcePagingResource getQueue() {
        return proxy.getQueue(true, apiKey);
    }

}

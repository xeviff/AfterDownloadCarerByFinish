package cat.hack3.mangrana.radarr.api.client.gateway;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import java.util.Objects;

import static cat.hack3.mangrana.utils.Output.log;

public class RadarrAPIInterfaceSingleton {

    private static RadarrAPIInterface apiInterface = null;
    private RadarrAPIInterfaceSingleton(){}

    private static void init (String host) {
        log("Initializing Proxy for host "+ host + " ...");
        UriBuilder fullPath = UriBuilder.fromPath(host);
        ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();
        ResteasyWebTarget target = client.target(fullPath);
        apiInterface = target.proxy(RadarrAPIInterface.class);
    }

    public static RadarrAPIInterface getInterface(String host) {
        if (Objects.isNull(apiInterface))
            init(host);
        return apiInterface;
    }
}

package cat.hack3.mangrana.utils.rest;

import cat.hack3.mangrana.radarr.api.client.gateway.RadarrAPIInterface;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrAPIInterface;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import java.util.Objects;

import static cat.hack3.mangrana.utils.Output.log;

public class APIProxyBuilderSingleton {

    private static APIInterface apiInterface = null;
    private APIProxyBuilderSingleton(){}

    private static void init (String host, Class<? extends APIInterface> clazz) {
        log("Initializing Proxy for host "+ host + " ...");
        UriBuilder fullPath = UriBuilder.fromPath(host);
        ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();
        ResteasyWebTarget target = client.target(fullPath);
        apiInterface = target.proxy(clazz);
    }

    public static RadarrAPIInterface getRadarrInterface(String host) {
        if (Objects.isNull(apiInterface))
            init(host, RadarrAPIInterface.class);
        return (RadarrAPIInterface) apiInterface;
    }

    public static SonarrAPIInterface getSonarrInterface(String host) {
        if (Objects.isNull(apiInterface))
            init(host, SonarrAPIInterface.class);
        return (SonarrAPIInterface) apiInterface;
    }

}


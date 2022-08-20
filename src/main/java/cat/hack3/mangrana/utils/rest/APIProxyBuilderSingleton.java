package cat.hack3.mangrana.utils.rest;

import cat.hack3.mangrana.radarr.api.client.gateway.RadarrAPIInterface;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrAPIInterface;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;
import java.util.Objects;

import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.rest.APIInterface.ProtocolURLMark.HTTPS;

public class APIProxyBuilderSingleton {

    private static RadarrAPIInterface radarrAPIInterface = null;
    private static SonarrAPIInterface sonarrAPIInterface = null;
    private APIProxyBuilderSingleton(){}

    private static void init (String host, Class<? extends APIInterface> clazz) {
        log("Initializing Proxy for host "+ host + " ...");
        UriBuilder fullPath = UriBuilder.fromPath(HTTPS.getMark()+host);
        ResteasyClient client = (ResteasyClient) ClientBuilder.newClient();
        ResteasyWebTarget target = client.target(fullPath);
        APIInterface apiInterface = target.proxy(clazz);
        if (clazz.getName().equals(RadarrAPIInterface.class.getName())) {
            radarrAPIInterface = (RadarrAPIInterface) apiInterface;
        } else if (clazz.getName().equals(SonarrAPIInterface.class.getName())) {
            sonarrAPIInterface = (SonarrAPIInterface) apiInterface;
        }
    }

    public static RadarrAPIInterface getRadarrInterface(String host) {
        if (Objects.isNull(radarrAPIInterface))
            init(host, RadarrAPIInterface.class);
        return radarrAPIInterface;
    }

    public static SonarrAPIInterface getSonarrInterface(String host) {
        if (Objects.isNull(sonarrAPIInterface))
            init(host, SonarrAPIInterface.class);
        return sonarrAPIInterface;
    }

}


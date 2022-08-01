package cat.hack3.mangrana.plex.url;

import cat.hack3.mangrana.config.ConfigFileLoader;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static cat.hack3.mangrana.utils.Output.log;

public class PlexCommandLauncher {

    private final ConfigFileLoader config;

    public PlexCommandLauncher(ConfigFileLoader config) {
        this.config = config;
    }

    public void scanByPath(String fullDestinationPath) {
        log("refreshing artist located on "+fullDestinationPath);
        String plexRefreshURL = getPlexRefreshURL();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest httpGET = RequestBuilder.get()
                    .setUri(new URI(plexRefreshURL))
                    .addParameter("path", fullDestinationPath)
                    .addParameter("X-Plex-Token", config.getConfig(PLEX_TOKEN))
                    .build();
            httpclient.execute(httpGET);
            log("launched url command: "+httpGET.getURI().toString().replaceFirst(config.getConfig(PLEX_TOKEN), "__plex_token__"));
        } catch (URISyntaxException | IOException e) {
            log("could not refresh plex artist because of "+e.getMessage());
            e.printStackTrace();
        }
    }

    private String getPlexRefreshURL() {
        String host = config.getConfig(PLEX_URL);
        String uriFormat = config.getConfig(PLEX_SECTION_REFRESH_URI);
        String uri = uriFormat.replaceFirst("\\{section_id}", config.getConfig(PLEX_SERIES_SECTION_ID));
        return host + uri;
    }
}

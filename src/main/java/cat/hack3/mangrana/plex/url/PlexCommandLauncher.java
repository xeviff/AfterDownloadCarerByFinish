package cat.hack3.mangrana.plex.url;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.utils.EasyLogger;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static cat.hack3.mangrana.utils.rest.APIInterface.ProtocolURLMark.HTTPS;

public class PlexCommandLauncher {

    private final EasyLogger logger;
    private final ConfigFileLoader config;
    private String sectionId;

    public PlexCommandLauncher(ConfigFileLoader config) {
        this.config = config;
        this.logger = new EasyLogger();
    }

    public void scanByPath(String fullDestinationPath) {
        String plexRefreshURL = getPlexRefreshURL();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest httpGET = RequestBuilder.get()
                    .setUri(new URI(plexRefreshURL))
                    .addParameter("path", fullDestinationPath)
                    .addParameter("X-Plex-Token", config.getConfig(PLEX_TOKEN))
                    .build();
            httpclient.execute(httpGET);
            String urlWithTokenHidden = httpGET.getURI().toString().replaceFirst(config.getConfig(PLEX_TOKEN), "__plex_token__");
            logger.nLog("Launched refresh command to section {0} with the following URL: {1}", sectionId, urlWithTokenHidden);
        } catch (URISyntaxException | IOException e) {
            logger.nLog("SHOULD NOT HAPPEN: Some error has happened using the URL <{0}>", plexRefreshURL);
            e.printStackTrace();
        }
    }

    private String getPlexRefreshURL() {
        String host = config.getConfig(PLEX_HOST);
        String uriFormat = config.getConfig(PLEX_SECTION_REFRESH_URI);
        sectionId = config.getConfig(PLEX_SERIES_SECTION_ID);
        String uri = uriFormat.replaceFirst("\\{section_id}", sectionId);
        return HTTPS.getMark() + host + uri;
    }

}

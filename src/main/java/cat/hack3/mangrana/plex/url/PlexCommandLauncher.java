package cat.hack3.mangrana.plex.url;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.EasyLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.rest.APIInterface.ProtocolURLMark.HTTPS;

public class PlexCommandLauncher {

    private final EasyLogger logger;
    private final ConfigFileLoader config;
    private final PlexLibrarySectionsResolver sectionResolver;

    public PlexCommandLauncher(ConfigFileLoader config) {
        this.config = config;
        this.logger = new EasyLogger();
        this.sectionResolver = new PlexLibrarySectionsResolver(this, config);
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException {
        String toRefresh="/tv/Series/C/City on a Hill (2019)";
        new PlexCommandLauncher(new ConfigFileLoader()).scanByPath(toRefresh);
    }

    public void scanByPath(String fullDestinationPath) {
        String plexPathToRefresh = getPlexUrlPath2Refresh(fullDestinationPath);
        String plexRefreshURL = getPlexRefreshURL(plexPathToRefresh);
        if (plexRefreshURL==null) return;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest httpGET = RequestBuilder.get()
                    .setUri(new URI(plexRefreshURL))
                    .addParameter("path", plexPathToRefresh)
                    .addParameter("X-Plex-Token", config.getConfig(PLEX_TOKEN))
                    .build();
            httpclient.execute(httpGET);
            String urlWithTokenHidden = httpGET.getURI().toString().replaceFirst(config.getConfig(PLEX_TOKEN), "__plex_token__");
            logger.nLog("Launched URL command: {0}",  urlWithTokenHidden);
        } catch (Exception e) {
            logger.nHLog("Some error has happened using the URL <{0}>", plexRefreshURL);
            e.printStackTrace();
        }
    }

    public String getPlexUrlPath2Refresh(String fullDestinationPath) {
        Pattern p = Pattern.compile(config.getConfig(SONARR_PATHS_STARTER).concat("(.+/.+ \\(\\d{4}\\))"));
        Matcher m = p.matcher(fullDestinationPath);
        if (m.find()) {
            String pathInPlexDockerStart = config.getConfig(PLEX_PATHS_STARTER);
            return pathInPlexDockerStart.concat(m.group(1));
        }
        return null;
    }

    public Document retrieveSectionsInfo() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpUriRequest httpGET = RequestBuilder.get()
                    .setUri(new URI(getPlexSectionsURL()))
                    .addParameter("X-Plex-Token", config.getConfig(PLEX_TOKEN))
                    .build();
            try (CloseableHttpResponse httpResponse = httpclient.execute(httpGET)) {
                final HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent()) {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        return builder.parse(inputStream);
                    } catch (ParserConfigurationException | SAXException e) {
                        log("could not get the section information from plex url");
                    }
                }
            }
            log("launched url command: "+httpGET.getURI().toString().replaceFirst(config.getConfig(PLEX_TOKEN), "__plex_token__"));
        } catch (URISyntaxException | IOException e) {
            log("could not refresh plex artist because of "+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String getPlexRefreshURL(String fullDestinationPath) {
        String sectionId = sectionResolver.resolveSectionByPath(fullDestinationPath);
        if (sectionId==null) return null;
        String host = config.getConfig(PLEX_HOST);
        String uriFormat = config.getConfig(PLEX_SECTION_REFRESH_URI);
        String uri = uriFormat.replaceFirst("\\{section_id}", sectionId);
        return HTTPS.getMark() + host + uri;
    }

    private String getPlexSectionsURL() {
        String host = HTTPS.getMark() + config.getConfig(PLEX_HOST);
        String uri = config.getConfig(PLEX_SECTIONS_LIST_URI);
        return host + uri;
    }

}

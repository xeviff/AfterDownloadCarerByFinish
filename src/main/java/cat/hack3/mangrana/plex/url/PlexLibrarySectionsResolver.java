package cat.hack3.mangrana.plex.url;


import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.utils.Output;
import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.PLEX_PATHS_STARTER;

public class PlexLibrarySectionsResolver {

    private final PlexCommandLauncher commandLauncher;
    private final ConfigFileLoader config;

    public PlexLibrarySectionsResolver(PlexCommandLauncher commandLauncher, ConfigFileLoader config) {
        this.commandLauncher = commandLauncher;
        this.config = config;
    }

    public String resolveSectionByPath(String fullDestinationPath) {
        final String plexPathStarter = config.getConfig(PLEX_PATHS_STARTER);
        String keyFolder = fullDestinationPath.replaceFirst(plexPathStarter,"").split("/")[1];
        Document xmlDocument = commandLauncher.retrieveSectionsInfo();
        XPath xPath = XPathFactory.newInstance().newXPath();
        String startingLocationText = plexPathStarter.concat("/").concat(keyFolder);
        String expression = "/MediaContainer/Directory/Location[starts-with(@path, '"+startingLocationText+"')]";
        try {
            NodeList candidatesNodes = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            Node directoryNodeOfLocation  = candidatesNodes.item(0).getParentNode();
            return ((DeferredElementImpl) directoryNodeOfLocation).getAttribute("key");
        } catch (XPathExpressionException e) {
            Output.log("could not resolve the section of the movie in plex");
        }
        return null;
    }

}

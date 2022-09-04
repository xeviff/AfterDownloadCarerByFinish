package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.utils.EasyLogger;

import java.io.IOException;

public abstract class ElementHandler {

    protected boolean initiated=false;

    protected final EasyLogger logger;
    protected final ConfigFileLoader configFileLoader;
    protected final SonarrApiGateway sonarrApiGateway;
    protected final GoogleDriveApiGateway googleDriveApiGateway;
    protected final RemoteCopyService copyService;
    protected final SerieRefresher serieRefresher;

    protected String elementName;
    protected int serieId;

    protected ElementHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        this.logger = logger;
        this.configFileLoader = configFileLoader;
        this.sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        this.googleDriveApiGateway = new GoogleDriveApiGateway();
        this.serieRefresher = new SerieRefresher(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
    }
    public ElementHandler initValues (String elementName, int serieId){
        this.elementName = elementName;
        this.serieId = serieId;
        initiated=true;
        return this;
    }

    public abstract void crashHandle () throws IncorrectWorkingReferencesException, TooMuchTriesException, IOException, NoElementFoundException;
    public abstract void handle() throws NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException, IOException;

    protected void log(String msg, Object... params) {
        logger.nLog(msg, params);
    }
    
}

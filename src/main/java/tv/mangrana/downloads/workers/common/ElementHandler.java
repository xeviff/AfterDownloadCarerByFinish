package tv.mangrana.downloads.workers.common;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.config.LocalEnvironmentManager;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.google.api.client.RemoteCopyService;
import tv.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import tv.mangrana.utils.EasyLogger;

import java.io.IOException;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.GOOGLE_RETRY_INTERVAL;

public abstract class ElementHandler {

    protected boolean initiated=false;

    protected int appElementId;
    protected String title;
    protected String elementName;
    protected final int googleWaitInterval;

    protected final EasyLogger logger;
    protected final ConfigFileLoader configFileLoader;
    protected final GoogleDriveApiGateway googleDriveApiGateway;
    protected final RemoteCopyService copyService;


    protected ElementHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        this.logger = logger;
        this.configFileLoader = configFileLoader;
        this.googleDriveApiGateway = new GoogleDriveApiGateway();
        copyService = new RemoteCopyService(configFileLoader);
        if (LocalEnvironmentManager.isLocal()) {
            googleWaitInterval = 10;
        } else {
            googleWaitInterval = Integer.parseInt(configFileLoader.getConfig(GOOGLE_RETRY_INTERVAL));
        }
    }

    public ElementHandler initValues (String title, String elementName, int appElementId){
        this.title = title;
        this.elementName = elementName;
        this.appElementId = appElementId;
        initiated=true;
        return this;
    }

    public abstract void crashHandle () throws IncorrectWorkingReferencesException, TooMuchTriesException, IOException, NoElementFoundException;
    public abstract void handle() throws NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException, IOException;

    protected void log(String msg, Object... params) {
        logger.nLog(msg, params);
    }

}

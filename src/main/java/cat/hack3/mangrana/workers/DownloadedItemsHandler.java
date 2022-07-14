package cat.hack3.mangrana.workers;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.radarr.api.client.process.DownloadedExternallyHandler;

import java.io.IOException;

import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.Output.logDate;

public class DownloadedItemsHandler {

    DownloadedExternallyHandler radarrExternallyDownloadedHandler;

    private DownloadedItemsHandler() throws IncorrectWorkingReferencesException, IOException {
        log("Hi my friends, here the downloaded movies handler. enjoy");
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        radarrExternallyDownloadedHandler = new DownloadedExternallyHandler(configFileLoader);
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        new DownloadedItemsHandler().process();
    }

    private void process() {
        //TODO get and handle the completed download that supposedly triggered this java program -- after that, check the "externally downloaded"
        radarrExternallyDownloadedHandler.handle();
        log("that's all, folks");
        logDate();
    }

}

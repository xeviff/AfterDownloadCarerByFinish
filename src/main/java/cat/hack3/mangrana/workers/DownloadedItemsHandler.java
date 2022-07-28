package cat.hack3.mangrana.workers;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.IOException;

import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.Output.logDate;

public class DownloadedItemsHandler {

    RadarrFailedDownloadsHandler radarrFailedDownloadsHandler;

    private DownloadedItemsHandler() throws IncorrectWorkingReferencesException, IOException {
        log("Hi my friends, here the downloaded movies handler. enjoy");
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        radarrFailedDownloadsHandler = new RadarrFailedDownloadsHandler(configFileLoader);
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        new DownloadedItemsHandler().process();
    }

    private void process() {
        //TODO get and handle the completed download that supposedly triggered this java program -- after that, check the "externally downloaded"
        radarrFailedDownloadsHandler.handle();
        log("that's all, folks");
        logDate();
    }

}

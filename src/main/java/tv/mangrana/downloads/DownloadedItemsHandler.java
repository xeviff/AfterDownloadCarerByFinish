package tv.mangrana.downloads;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.GrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.common.Handler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.IOException;
import java.util.EnumMap;

import static tv.mangrana.utils.Output.log;
import static tv.mangrana.utils.Output.logWithDate;

public class DownloadedItemsHandler {

    private enum ActionType {DOWNLOADS_REMOTE_COPY}
    private final EnumMap<ActionType, Handler> actionHandler;
    ConfigFileLoader configFileLoader;

    private DownloadedItemsHandler() throws IncorrectWorkingReferencesException {
        log("") ;log(""); log("");
        log("************************************************************************************************");
        log("********* Hi my friends, here the downloaded movies and series handler. enjoy ******************");
        log("************************************************************************************************");
        configFileLoader = new ConfigFileLoader();
        actionHandler = new EnumMap<>(ActionType.class);
        actionHandler.put(ActionType.DOWNLOADS_REMOTE_COPY, new GrabbedDownloadsHandler(configFileLoader));
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException {
        new DownloadedItemsHandler().process();
    }

    private void process() {
        actionHandler.get(ActionType.DOWNLOADS_REMOTE_COPY).handle();
        logWithDate("Handlers running. Main thread finished.");
    }

}

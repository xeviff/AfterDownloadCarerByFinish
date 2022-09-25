package cat.hack3.mangrana.downloads;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.GrabbedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.common.Handler;
import cat.hack3.mangrana.downloads.workers.radarr.RadarrFailedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarrFailedDownloadsHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.IOException;
import java.util.EnumMap;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.MANAGE_FAILED_DOWNLOADS;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.Output.logWithDate;

public class DownloadedItemsHandler {

    private enum ActionType {DOWNLOADS_REMOTE_COPY, SONARR_FAILED, RADARR_FAILED}
    private final EnumMap<ActionType, Handler> actionHandler;
    ConfigFileLoader configFileLoader;

    private DownloadedItemsHandler() throws IncorrectWorkingReferencesException, IOException {
        log("********************************************************");
        log("Hi my friends, here the downloaded movies and series handler. enjoy");
        configFileLoader = new ConfigFileLoader();
        actionHandler = new EnumMap<>(ActionType.class);
        actionHandler.put(ActionType.SONARR_FAILED, new SonarrFailedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.RADARR_FAILED, new RadarrFailedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.DOWNLOADS_REMOTE_COPY, new GrabbedDownloadsHandler(configFileLoader));
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        new DownloadedItemsHandler().process();
    }

    private void process() {
        if (Boolean.parseBoolean(configFileLoader.getConfig(MANAGE_FAILED_DOWNLOADS))) {
            actionHandler.get(ActionType.RADARR_FAILED).handle();
            actionHandler.get(ActionType.SONARR_FAILED).handle();
        }
        actionHandler.get(ActionType.DOWNLOADS_REMOTE_COPY).handle();
        logWithDate("Handlers running. Main thread finished.");
    }

}

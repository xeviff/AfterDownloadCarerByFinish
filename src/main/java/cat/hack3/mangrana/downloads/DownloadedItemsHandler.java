package cat.hack3.mangrana.downloads;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.Handler;
import cat.hack3.mangrana.downloads.workers.radarr.RadarrFailedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.radarr.RadarrFinishedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarrFailedDownloadsHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.IOException;
import java.util.EnumMap;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.MANAGE_FAILED_DOWNLOADS;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.Output.logWithDate;

public class DownloadedItemsHandler {

    private enum ActionType {SONARR_GRAB, RADARR_GRAB, SONARR_FAILED, RADARR_FAILED}
    private final EnumMap<ActionType, Handler> actionHandler;
    ConfigFileLoader configFileLoader;

    private DownloadedItemsHandler() throws IncorrectWorkingReferencesException, IOException {
        log("********************************************************");
        log("Hi my friends, here the downloaded movies handler. enjoy");
        configFileLoader = new ConfigFileLoader();
        actionHandler = new EnumMap<>(ActionType.class);
        actionHandler.put(ActionType.SONARR_GRAB, new SonarGrabbedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.RADARR_GRAB, new RadarrFinishedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.SONARR_FAILED, new SonarrFailedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.RADARR_FAILED, new RadarrFailedDownloadsHandler(configFileLoader));
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        new DownloadedItemsHandler().process();
    }

    private void process() {
        if (Boolean.parseBoolean(configFileLoader.getConfig(MANAGE_FAILED_DOWNLOADS))) {
            actionHandler.get(ActionType.RADARR_FAILED).handle();
            actionHandler.get(ActionType.SONARR_FAILED).handle();
        }

        actionHandler.get(ActionType.SONARR_GRAB).handle();

        logWithDate("that's all, folks");
    }

}

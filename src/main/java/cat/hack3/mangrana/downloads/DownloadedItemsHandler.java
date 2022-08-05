package cat.hack3.mangrana.downloads;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.Handler;
import cat.hack3.mangrana.downloads.workers.ParametrizedHandler;
import cat.hack3.mangrana.downloads.workers.radarr.RadarrFailedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.radarr.RadarrFinishedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarrFailedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarrFinishedDownloadsHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.MANAGE_FAILED_DOWNLOADS;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.Output.logDate;

public class DownloadedItemsHandler {

    private enum ActionType {SONARR_FINISHED, RADARR_FINISHED, SONARR_FAILED, RADARR_FAILED}
    private final EnumMap<ActionType, Handler> actionHandler;
    ConfigFileLoader configFileLoader;

    private DownloadedItemsHandler() throws IncorrectWorkingReferencesException, IOException {
        log("Hi my friends, here the downloaded movies handler. enjoy");
        configFileLoader = new ConfigFileLoader();
        actionHandler = new EnumMap<>(ActionType.class);
        actionHandler.put(ActionType.SONARR_FINISHED, new SonarrFinishedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.RADARR_FINISHED, new RadarrFinishedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.SONARR_FAILED, new SonarrFailedDownloadsHandler(configFileLoader));
        actionHandler.put(ActionType.RADARR_FAILED, new RadarrFailedDownloadsHandler(configFileLoader));
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        new DownloadedItemsHandler().process(args);
    }

    private void process(String[] args) {
        List<String> parameters = Arrays.asList(args);
        ActionType parametrizedAction = ActionType.valueOf(parameters.remove(0));
        if (Stream.of(ActionType.SONARR_FINISHED, ActionType.RADARR_FINISHED)
                .collect(Collectors.toList())
                .contains(parametrizedAction)) {
            ((ParametrizedHandler) actionHandler.get(parametrizedAction)).handle(parameters);
        }

        if (Boolean.parseBoolean(configFileLoader.getConfig(MANAGE_FAILED_DOWNLOADS))) {
            actionHandler.get(ActionType.RADARR_FAILED).handle();
            actionHandler.get(ActionType.SONARR_FAILED).handle();
        }
        log("that's all, folks");
        logDate();
    }

}

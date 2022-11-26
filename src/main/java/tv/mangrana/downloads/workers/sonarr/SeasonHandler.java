package tv.mangrana.downloads.workers.sonarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.RetryEngine;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.sonarr.api.schema.series.SonarrSerie;
import tv.mangrana.utils.EasyLogger;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.CHECK_EPISODE_FILES_NUMBER_UPLOADED;
import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_SERIES_FOLDER_ID;
import static tv.mangrana.utils.Output.msg;
import static tv.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SeasonHandler extends SonarrElementHandler {

    private int episodeCount;

    public SeasonHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        super(logger, configFileLoader);
    }

    public SeasonHandler initValues (String title, String elementName, int serieId, int episodeCount){
        this.episodeCount = episodeCount;
        return (SeasonHandler) super.initValues(title, elementName, serieId);
    }

    public void handle() throws NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException, IOException {
        handle(true);
    }

    public void handle(boolean waitUntilExists) throws IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException, IOException {
        if (!initiated) throw new CircuitBreakingException("initValues method execution is needed first");
        if (waitUntilExists) {
            Function<File, List<File>> childrenRetriever = file ->
                    googleDriveApiGateway.getChildrenFromParent(file, false);
            Function<File, Boolean> fileNameConstraint = file -> !file.getName().endsWith(".part");
            RetryEngine<File> retryer = null;
            if (Boolean.parseBoolean(configFileLoader.getConfig(CHECK_EPISODE_FILES_NUMBER_UPLOADED))) {
                retryer = new RetryEngine<>(
                        "SeasonOnGoogle",
                        googleWaitInterval,
                        new RetryEngine.ChildrenRequirements<>(episodeCount, childrenRetriever, fileNameConstraint),
                        this::log
                );
            } else {
                retryer = new RetryEngine<>(
                        "SeasonOnGoogle",
                        googleWaitInterval,
                        this::log
                );
            }
            copyService.setRetryEngine(retryer);
        }

        SonarrSerie serie = sonarrApiGateway.getSerieById(appElementId);
        if (Objects.isNull(serie)) throw new NoElementFoundException(msg("Could not found serie with id {0} in Sonarr", String.valueOf(appElementId)));
        String seasonFolderName = getSeasonFolderNameFromSeason(title);
        copyService.copySeasonFromDownloadToItsLocation(elementName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    public void crashHandle () throws NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException, IOException {
        try {
            File parentFolder =  googleDriveApiGateway.lookupElementById(configFileLoader.getConfig(DOWNLOADS_SERIES_FOLDER_ID));
            File season = googleDriveApiGateway.getChildFromParentByName(elementName, parentFolder, true);
            List<File> episodes = googleDriveApiGateway.getChildrenFromParent(season, false);
            if (episodes.size() < episodeCount) throw new NoElementFoundException(msg("some episode is missing: expected {0}, got {1}", episodeCount, episodes.size()));
        } catch (Exception e) {
            throw new NoElementFoundException("season not downloaded yet");
        }
        handle(false);
    }

}

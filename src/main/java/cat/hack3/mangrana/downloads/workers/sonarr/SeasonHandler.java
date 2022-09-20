package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.RetryEngine;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.utils.EasyLogger;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_SERIES_FOLDER_ID;
import static cat.hack3.mangrana.utils.Output.msg;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromSeason;

public class SeasonHandler extends ElementHandler {

    private int episodeCount;

    public SeasonHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        super(logger, configFileLoader);
    }

    public SeasonHandler initValues (String elementName, int serieId, int episodeCount){
        this.episodeCount = episodeCount;
        return (SeasonHandler) super.initValues(elementName, serieId);
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
            RetryEngine<File> retryer = new RetryEngine<>(
                    "SeasonOnGoogle",
                    googleWaitInterval,
                    new RetryEngine.ChildrenRequirements<>(episodeCount, childrenRetriever, fileNameConstraint),
                    this::log
            );
            copyService.setRetryEngine(retryer);
        }

        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromSeason(elementName);
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

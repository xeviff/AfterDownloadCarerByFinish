package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.RetryEngine;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.utils.EasyLogger;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;

import java.io.IOException;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_TEAM_DRIVE_ID;
import static cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;
import static cat.hack3.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;

public class EpisodeHandler extends ElementHandler {

    public EpisodeHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        super(logger, configFileLoader);
    }

    public void handle() throws NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException, IOException {
        handle(true);
    }

    public void handle(boolean waitUntilExists) throws IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException, IOException {
        if (!initiated) throw new CircuitBreakingException("initValues method execution is needed first");
        if (waitUntilExists) copyService.setRetryEngine(new RetryEngine<>(
                "EpisodeOnGoogle",
                googleWaitInterval /2,
                this::log)
        );
        SonarrSerie serie = sonarrApiGateway.getSerieById(serieId);
        String seasonFolderName = getSeasonFolderNameFromEpisode(elementName);
        copyService.copyEpisodeFromDownloadToItsLocation(elementName, serie.getPath(), seasonFolderName);
        serieRefresher.refreshSerieInSonarrAndPlex(serie);
    }

    public void crashHandle () throws IncorrectWorkingReferencesException, TooMuchTriesException, IOException, NoElementFoundException {
        try {
            googleDriveApiGateway.lookupElementByName(elementName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
        } catch (NoElementFoundException e) {
            throw new NoElementFoundException("episode not downloaded yet");
        }
        handle(false);
    }

}

package tv.mangrana.downloads.workers.sonarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.RetryEngine;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.sonarr.api.schema.series.SonarrSerie;
import tv.mangrana.utils.EasyLogger;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;

import java.io.IOException;
import java.util.Objects;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_TEAM_DRIVE_ID;
import static tv.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;
import static tv.mangrana.utils.StringCaptor.getSeasonFolderNameFromEpisode;

public class EpisodeHandler extends SonarrElementHandler {

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
        SonarrSerie serie = sonarrApiGateway.getSerieById(appElementId);
        if (Objects.isNull(serie)) return;
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

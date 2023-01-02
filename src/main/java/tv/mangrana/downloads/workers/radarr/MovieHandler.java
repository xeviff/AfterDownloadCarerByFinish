package tv.mangrana.downloads.workers.radarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.ElementHandler;
import tv.mangrana.downloads.workers.common.RetryEngine;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import tv.mangrana.radarr.api.schema.movie.MovieResource;
import tv.mangrana.utils.EasyLogger;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;

import java.io.IOException;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_TEAM_DRIVE_ID;
import static tv.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;

public class MovieHandler extends ElementHandler {

    RadarrApiGateway radarrApiGateway;

    public MovieHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        super(logger, configFileLoader);
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
    }

    public void handle() throws NoElementFoundException, TooMuchTriesException, IOException {
        handle(true);
    }

    public void handle(boolean waitUntilExists) throws NoElementFoundException, TooMuchTriesException, IOException {
        if (!initiated) throw new CircuitBreakingException("initValues method execution is needed first");
        if (waitUntilExists) copyService.setRetryEngine(new RetryEngine<>(
                "MovieOnGoogle",
                googleWaitInterval,
                this::log)
        );
        MovieResource movie = radarrApiGateway.getMovieById(appElementId);
        copyService.copyMovieFile(elementName, movie.getPath());
        //TODO call AfterFileBotCarer
    }

    public void crashHandle () throws IncorrectWorkingReferencesException, TooMuchTriesException, IOException, NoElementFoundException {
        try {
            googleDriveApiGateway.lookupElementByName(elementName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
        } catch (NoElementFoundException e) {
            throw new NoElementFoundException("movie not downloaded yet");
        }
        handle(false);
    }
}

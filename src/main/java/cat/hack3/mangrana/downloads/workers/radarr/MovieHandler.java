package cat.hack3.mangrana.downloads.workers.radarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.ElementHandler;
import cat.hack3.mangrana.downloads.workers.common.RetryEngine;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import cat.hack3.mangrana.radarr.api.schema.movie.MovieResource;
import cat.hack3.mangrana.utils.EasyLogger;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;

import java.io.IOException;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.DOWNLOADS_TEAM_DRIVE_ID;
import static cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;

public class MovieHandler extends ElementHandler {

    RadarrApiGateway radarrApiGateway;

    public MovieHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        super(logger, configFileLoader);
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
    }

    public void handle() throws NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException, IOException {
        handle(true);
    }

    public void handle(boolean waitUntilExists) throws IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException, IOException {
        if (!initiated) throw new CircuitBreakingException("initValues method execution is needed first");
        if (waitUntilExists) copyService.setRetryEngine(new RetryEngine<>(
                "MovieOnGoogle",
                googleWaitInterval /2,
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
            throw new NoElementFoundException("episode not downloaded yet");
        }
        handle(false);
    }
}

package tv.mangrana.downloads.workers.radarr.jobs;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.ElementHandler;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.radarr.MovieHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import tv.mangrana.utils.EasyLogger;

import java.io.IOException;

import static tv.mangrana.downloads.workers.radarr.jobs.RadarrJobFile.GrabInfo.RADARR_RELEASE_TITLE;

public class RadarrJobHandler extends JobHandler {

    RadarrApiGateway radarrApiGateway;
    private int movieId;

    public RadarrJobHandler(ConfigFileLoader configFileLoader, RadarrJobFile radarrJobFile, JobOrchestrator caller) throws IOException, IncorrectWorkingReferencesException {
        super(configFileLoader, radarrJobFile, caller);
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
    }

    @SuppressWarnings("unchecked")
    protected void loadInfoFromJobFile() {
        fullTitle = jobFile.getInfo(RADARR_RELEASE_TITLE);
        jobTitle = fullTitle.substring(0, 45)+"..";
        logger = new EasyLogger("*> "+jobTitle);
        downloadId = jobFile.getInfo(RadarrJobFile.GrabInfo.RADARR_DOWNLOAD_ID);
        movieId = Integer.parseInt(jobFile.getInfo(RadarrJobFile.GrabInfo.RADARR_MOVIE_ID));
    }

    @Override
    protected ElementHandler getElementHandler() throws IOException {
        return new MovieHandler(logger, configFileLoader).initValues(fullTitle, elementName, movieId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isAlreadyComplete() {
        return COMPLETE_STATUS.equals(jobFile.getInfo(RadarrJobFile.GrabInfo.STATUS));
    }

}

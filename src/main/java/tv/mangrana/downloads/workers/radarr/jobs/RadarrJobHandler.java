package tv.mangrana.downloads.workers.radarr.jobs;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.config.LocalEnvironmentManager;
import tv.mangrana.downloads.workers.common.ElementHandler;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.common.RetryEngine;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.radarr.MovieHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import tv.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import tv.mangrana.utils.EasyLogger;
import tv.mangrana.utils.PathUtils;

import java.io.IOException;
import java.util.function.Supplier;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.RADARR_RETRY_INTERVAL;
import static tv.mangrana.downloads.workers.radarr.jobs.RadarrJobFile.GrabInfo.RADARR_RELEASE_TITLE;

public class RadarrJobHandler extends JobHandler {

    final int radarrWaitInterval;
    RadarrApiGateway radarrApiGateway;
    private int movieId;

    public RadarrJobHandler(ConfigFileLoader configFileLoader, RadarrJobFile radarrJobFile, JobOrchestrator caller) throws IOException, IncorrectWorkingReferencesException {
        super(configFileLoader, radarrJobFile, caller);
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
        if (LocalEnvironmentManager.isLocal()){
            radarrWaitInterval = 2;
        } else {
            radarrWaitInterval = Integer.parseInt(configFileLoader.getConfig(RADARR_RETRY_INTERVAL));
        }
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

    protected void retrieveFileNameFromArrApp() throws TooMuchTriesException {
        Supplier<String> getOutputFromQueue = () -> {
            logWhenActive("searching from Radarr Queue downloadId="+downloadId);
            try {
                QueueResourcePagingResource queue = radarrApiGateway.getQueue();
                String outputPath = queue.getRecords()
                        .stream()
                        .filter(rcd -> downloadId.equals(rcd.getDownloadId()))
                        .findFirst()
                        .orElseThrow(() -> new NoElementFoundException("element not found in queue"))
                        .getOutputPath();
                return PathUtils.getCurrentFromFullPath(outputPath);
            } catch (NoElementFoundException e) {
                logWhenActive("not found "+downloadId+" yet on queue, will retry later");
                return null;
            }
        };
        RetryEngine<String> retryEngineForQueue = new RetryEngine<>(
                "RadarrQueueRecord",
                radarrWaitInterval,
                this::logWhenActive);
        elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue, RetryEngine.TOO_MUCH_RETRIES_INFINITE_THRESHOLD);
    }

    protected void handleElement() throws IOException, NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException {
            new MovieHandler(logger, configFileLoader)
                    .initValues(fullTitle, elementName, movieId)
                    .handle();
    }

}

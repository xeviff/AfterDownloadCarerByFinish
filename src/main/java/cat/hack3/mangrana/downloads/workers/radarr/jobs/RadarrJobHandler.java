package cat.hack3.mangrana.downloads.workers.radarr.jobs;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.downloads.workers.common.JobOrchestrator;
import cat.hack3.mangrana.downloads.workers.common.RetryEngine;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.downloads.workers.radarr.MovieHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import cat.hack3.mangrana.utils.EasyLogger;
import cat.hack3.mangrana.utils.PathUtils;

import java.io.IOException;
import java.util.function.Supplier;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.RADARR_RETRY_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.radarr.jobs.RadarrJobFile.GrabInfo.RADARR_RELEASE_TITLE;

public class RadarrJobHandler extends JobHandler {

    final int radarrWaitInterval;
    RadarrApiGateway radarrApiGateway;
    private int serieId;

    public RadarrJobHandler(ConfigFileLoader configFileLoader, RadarrJobFile radarrJobFile, JobOrchestrator caller) throws IOException {
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
        serieId = Integer.parseInt(jobFile.getInfo(RadarrJobFile.GrabInfo.RADARR_MOVIE_ID));
        fileName = jobFile.getInfo(RadarrJobFile.GrabInfo.JAVA_FILENAME);
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
        elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue);
    }

    protected void handleElement() throws IOException, NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException {
            new MovieHandler(logger, configFileLoader)
                    .initValues(elementName, serieId)
                    .handle();
    }

}

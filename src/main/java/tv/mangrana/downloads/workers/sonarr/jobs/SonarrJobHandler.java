package tv.mangrana.downloads.workers.sonarr.jobs;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.config.LocalEnvironmentManager;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.common.RetryEngine;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.sonarr.EpisodeHandler;
import tv.mangrana.downloads.workers.sonarr.SeasonHandler;
import tv.mangrana.downloads.workers.sonarr.SerieRefresher;
import tv.mangrana.downloads.workers.sonarr.SonarrElementHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import tv.mangrana.sonarr.api.schema.queue.SonarrQueue;
import tv.mangrana.utils.EasyLogger;
import tv.mangrana.utils.PathUtils;

import java.io.IOException;
import java.util.function.Supplier;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_RETRY_INTERVAL;
import static tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.GrabInfo.SONARR_RELEASE_TITLE;
import static tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.EPISODE;

public class SonarrJobHandler extends JobHandler {

    public enum DownloadType {SEASON, EPISODE}
    private DownloadType type;
    final int sonarrWaitInterval;
    SonarrApiGateway sonarrApiGateway;
    SerieRefresher serieRefresher;
    private int serieId;
    private int episodeCount;

    public SonarrJobHandler(ConfigFileLoader configFileLoader, SonarrJobFile sonarrJobFile, JobOrchestrator caller) throws IOException, IncorrectWorkingReferencesException {
        super(configFileLoader, sonarrJobFile, caller);
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        serieRefresher = new SerieRefresher(configFileLoader);
        if (LocalEnvironmentManager.isLocal()){
            sonarrWaitInterval = 2;
        } else {
            sonarrWaitInterval = Integer.parseInt(configFileLoader.getConfig(SONARR_RETRY_INTERVAL));
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadInfoFromJobFile() {
        fullTitle = jobFile.getInfo(SONARR_RELEASE_TITLE);
        try {
            jobTitle = fullTitle.substring(0, 45) + "..";
        } catch (Exception e) {
            jobTitle = fullTitle;
        }
        logger = new EasyLogger("*> "+jobTitle);
        downloadId = jobFile.getInfo(SonarrJobFile.GrabInfo.SONARR_DOWNLOAD_ID);
        episodeCount = Integer.parseInt(jobFile.getInfo(SonarrJobFile.GrabInfo.SONARR_RELEASE_EPISODECOUNT));
        type = episodeCount == 1 ? EPISODE : DownloadType.SEASON;
        serieId = Integer.parseInt(jobFile.getInfo(SonarrJobFile.GrabInfo.SONARR_SERIES_ID));
    }

    protected void retrieveFileNameFromArrApp() throws TooMuchTriesException {
        Supplier<String> getOutputFromQueue = () -> {
            logWhenActive("searching from Sonarr Queue downloadId="+downloadId);
            try {
                SonarrQueue queue = sonarrApiGateway.getQueue();
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
                "SonarrQueueRecord",
                sonarrWaitInterval,
                this::logWhenActive);
        elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue, RetryEngine.TOO_MUCH_RETRIES_INFINITE_THRESHOLD);
    }

    protected SonarrElementHandler getElementHandler() throws IOException {
        SonarrElementHandler elementHandler;
        if (EPISODE.equals(type)) {
            elementHandler = (SonarrElementHandler)
                    new EpisodeHandler(logger, configFileLoader)
                    .initValues(fullTitle, elementName, serieId);
        } else {
            elementHandler = new SeasonHandler(logger, configFileLoader)
                    .initValues(fullTitle, elementName, serieId, episodeCount);
        }
        return elementHandler;
    }

}

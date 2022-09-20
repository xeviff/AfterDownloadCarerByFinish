package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.downloads.workers.common.RetryEngine;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.EpisodeHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SeasonHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.SerieRefresher;
import cat.hack3.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.utils.EasyLogger;
import cat.hack3.mangrana.utils.PathUtils;

import java.io.IOException;
import java.util.function.Supplier;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.SONARR_RETRY_INTERVAL;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.EPISODE;

public class SonarrJobHandler extends JobHandler {

    public enum DownloadType {SEASON, EPISODE}
    private DownloadType type;
    final int sonarrWaitInterval;
    SonarrApiGateway sonarrApiGateway;
    SerieRefresher serieRefresher;
    private int serieId;
    private int episodeCount;

    public SonarrJobHandler(ConfigFileLoader configFileLoader, SonarrJobFile sonarrJobFile, SonarGrabbedDownloadsHandler caller) throws IOException {
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
        fullTitle = jobFile.getInfo(SonarrJobFile.GrabInfo.SONARR_RELEASE_TITLE);
        jobTitle = fullTitle.substring(0, 45)+"..";
        logger = new EasyLogger("*> "+jobTitle);
        downloadId = jobFile.getInfo(SonarrJobFile.GrabInfo.SONARR_DOWNLOAD_ID);
        episodeCount = Integer.parseInt(jobFile.getInfo(SonarrJobFile.GrabInfo.SONARR_RELEASE_EPISODECOUNT));
        type = episodeCount == 1 ? EPISODE : DownloadType.SEASON;
        serieId = Integer.parseInt(jobFile.getInfo(SonarrJobFile.GrabInfo.SONARR_SERIES_ID));
        fileName = jobFile.getInfo(SonarrJobFile.GrabInfo.JAVA_FILENAME);
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
        elementName = retryEngineForQueue.tryUntilGotDesired(getOutputFromQueue);
    }

    protected void handleElement() throws IOException, NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException {
        if (EPISODE.equals(type)) {
            new EpisodeHandler(logger, configFileLoader)
                    .initValues(elementName, serieId)
                    .handle();
        } else {
            new SeasonHandler(logger, configFileLoader)
                    .initValues(elementName, serieId, episodeCount)
                    .handle();
        }
    }

}

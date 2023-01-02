package tv.mangrana.downloads.workers.sonarr.jobs;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.sonarr.EpisodeHandler;
import tv.mangrana.downloads.workers.sonarr.SeasonHandler;
import tv.mangrana.downloads.workers.sonarr.SerieRefresher;
import tv.mangrana.downloads.workers.sonarr.SonarrElementHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import tv.mangrana.utils.EasyLogger;

import java.io.IOException;

import static tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.GrabInfo.SONARR_RELEASE_TITLE;
import static tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler.DownloadType.EPISODE;

public class SonarrJobHandler extends JobHandler {

    public enum DownloadType {SEASON, EPISODE}
    private DownloadType type;
    SonarrApiGateway sonarrApiGateway;
    SerieRefresher serieRefresher;
    private int serieId;
    private int episodeCount;

    public SonarrJobHandler(ConfigFileLoader configFileLoader, SonarrJobFile sonarrJobFile, JobOrchestrator caller) throws IOException, IncorrectWorkingReferencesException {
        super(configFileLoader, sonarrJobFile, caller);
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        serieRefresher = new SerieRefresher(configFileLoader);
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

    @SuppressWarnings("unchecked")
    @Override
    public boolean isAlreadyComplete() {
        return COMPLETE_STATUS.equals(jobFile.getInfo(SonarrJobFile.GrabInfo.STATUS));
    }

}

package tv.mangrana.downloads.workers.radarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.AppGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.common.jobs.JobFile;
import tv.mangrana.downloads.workers.common.jobs.JobFileManager;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.radarr.jobs.RadarrJobFile;
import tv.mangrana.downloads.workers.radarr.jobs.RadarrJobHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

import static tv.mangrana.downloads.workers.common.jobs.JobFileManager.JobFileType.RADARR_JOBS;

public class RadarGrabbedDownloadsHandler implements AppGrabbedDownloadsHandler {

    JobFileManager.JobFileType jobFileType;

    public RadarGrabbedDownloadsHandler() {
        jobFileType = RADARR_JOBS;
    }

    @SuppressWarnings("rawtypes")
    public JobFile provideJobFile(File jobFile) throws IncorrectWorkingReferencesException {
        return new RadarrJobFile(jobFile);
    }

    @SuppressWarnings("rawtypes")
    public JobHandler provideJobHandler(ConfigFileLoader configFileLoader, JobFile jobFileManager, JobOrchestrator orchestrator) throws IOException {
        return new RadarrJobHandler(configFileLoader, (RadarrJobFile)jobFileManager, orchestrator);
    }

    public JobFileManager.JobFileType getJobFileType() {
        return jobFileType;
    }
}

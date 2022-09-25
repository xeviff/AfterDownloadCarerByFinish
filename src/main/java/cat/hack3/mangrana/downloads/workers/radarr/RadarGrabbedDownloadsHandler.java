package cat.hack3.mangrana.downloads.workers.radarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.AppGrabbedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.common.JobOrchestrator;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFile;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.downloads.workers.radarr.jobs.RadarrJobFile;
import cat.hack3.mangrana.downloads.workers.radarr.jobs.RadarrJobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager.JobFileType.RADARR_JOBS;

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

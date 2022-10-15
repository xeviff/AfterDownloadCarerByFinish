package tv.mangrana.downloads.workers.sonarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.AppGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.jobs.JobFileManager;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile;
import tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

import static tv.mangrana.jobs.JobFileManager.JobFileType.SONARR_JOBS;

public class SonarGrabbedDownloadsHandler implements AppGrabbedDownloadsHandler {

    JobFileManager.JobFileType jobFileType;

    public SonarGrabbedDownloadsHandler() {
        jobFileType = SONARR_JOBS;
    }

    @SuppressWarnings("rawtypes")
    public JobFile provideJobFile(File jobFile) throws IncorrectWorkingReferencesException {
        return new SonarrJobFile(jobFile);
    }

    @SuppressWarnings("rawtypes")
    public JobHandler provideJobHandler(ConfigFileLoader configFileLoader, JobFile jobFileManager, JobOrchestrator orchestrator) throws IOException {
        return new SonarrJobHandler(configFileLoader, (SonarrJobFile)jobFileManager, orchestrator);
    }

    public JobFileManager.JobFileType getJobFileType() {
        return jobFileType;
    }

}

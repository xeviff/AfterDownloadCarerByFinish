package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.AppGrabbedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.common.JobOrchestrator;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFile;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager.JobFileType.SONARR_JOBS;

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

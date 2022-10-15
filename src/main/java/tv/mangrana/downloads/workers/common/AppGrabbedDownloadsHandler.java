package tv.mangrana.downloads.workers.common;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.jobs.JobFileManager;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

public interface AppGrabbedDownloadsHandler {
    @SuppressWarnings("rawtypes")
    JobFile provideJobFile(File jobFile) throws IncorrectWorkingReferencesException;
    @SuppressWarnings("rawtypes")
    JobHandler provideJobHandler(ConfigFileLoader configFileLoader, JobFile jobFileManager, JobOrchestrator orchestrator) throws IOException;
    JobFileManager.JobFileType getJobFileType();
}

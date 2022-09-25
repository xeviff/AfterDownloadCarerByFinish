package cat.hack3.mangrana.downloads.workers.common;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFile;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

public interface AppGrabbedDownloadsHandler {
    @SuppressWarnings("rawtypes")
    JobFile provideJobFile(File jobFile) throws IncorrectWorkingReferencesException;
    @SuppressWarnings("rawtypes")
    JobHandler provideJobHandler(ConfigFileLoader configFileLoader, JobFile jobFileManager, JobOrchestrator orchestrator) throws IOException;
    JobFileManager.JobFileType getJobFileType();
}

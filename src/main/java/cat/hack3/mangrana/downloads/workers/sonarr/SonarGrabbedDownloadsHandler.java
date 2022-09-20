package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.GrabbedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.common.JobOrchestrator;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFile;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager.JobFileType.SONARR_JOBS;

public class SonarGrabbedDownloadsHandler extends GrabbedDownloadsHandler {


    public SonarGrabbedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        super(configFileLoader);
        jobFileType = SONARR_JOBS;
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        new SonarGrabbedDownloadsHandler(configFileLoader).handle();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected JobFile getJobFile(File jobFile) throws IncorrectWorkingReferencesException {
        return new SonarrJobFile(jobFile);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected JobHandler getJobHandler(ConfigFileLoader configFileLoader, JobFile jobFileManager, JobOrchestrator orchestrator) throws IOException {
        return new SonarrJobHandler(configFileLoader, (SonarrJobFile)jobFileManager, orchestrator);
    }

}

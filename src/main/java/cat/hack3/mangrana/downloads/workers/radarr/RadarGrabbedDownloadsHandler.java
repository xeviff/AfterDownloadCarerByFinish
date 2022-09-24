package cat.hack3.mangrana.downloads.workers.radarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.GrabbedDownloadsHandler;
import cat.hack3.mangrana.downloads.workers.common.JobOrchestrator;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFile;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.downloads.workers.radarr.jobs.RadarrJobFile;
import cat.hack3.mangrana.downloads.workers.radarr.jobs.RadarrJobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;

import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager.JobFileType.RADARR_JOBS;

public class RadarGrabbedDownloadsHandler extends GrabbedDownloadsHandler {

    public RadarGrabbedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        super(configFileLoader);
        jobFileType = RADARR_JOBS;
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        new RadarGrabbedDownloadsHandler(configFileLoader).handle();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected JobFile getJobFile(File jobFile) throws IncorrectWorkingReferencesException {
        return new RadarrJobFile(jobFile);
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected JobHandler getJobHandler(ConfigFileLoader configFileLoader, JobFile jobFileManager, JobOrchestrator orchestrator) throws IOException {
        return new RadarrJobHandler(configFileLoader, (RadarrJobFile)jobFileManager, orchestrator);
    }
}

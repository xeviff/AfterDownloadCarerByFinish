package cat.hack3.mangrana.downloads.workers.radarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.Handler;
import cat.hack3.mangrana.downloads.workers.common.JobOrchestrator;

public class RadarGrabbedDownloadsHandler implements Handler, JobOrchestrator {

    public RadarGrabbedDownloadsHandler(ConfigFileLoader configFileLoader) {
    }

    @Override
    public void handle() {

    }

    @Override
    public boolean isWorkingWithAJob() {
        return false;
    }

    @Override
    public boolean isJobWorking(String jobTitle) {
        return false;
    }

    @Override
    public void jobInitiated(String jobTitle) {

    }

    @Override
    public void jobHasFileName(String jobTitle) {

    }

    @Override
    public void jobWorking(String jobTitle) {

    }

    @Override
    public void jobFinished(String jobTitle, String fileName) {

    }

    @Override
    public void jobError(String jobTitle, String fileName) {

    }
}

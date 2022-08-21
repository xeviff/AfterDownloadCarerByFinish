package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.downloads.workers.Handler;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.GRABBED_FILE_IDENTIFIER_REGEX;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager.*;
import static cat.hack3.mangrana.utils.Output.logWithDate;

public class SonarGrabbedDownloadsHandler implements Handler {

    ConfigFileLoader configFileLoader;
    SonarrApiGateway sonarrApiGateway;
    GoogleDriveApiGateway googleDriveApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;

    public static final int CLOUD_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 30;
    public static final int SONARR_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 5;

    Map<String, String> jobsState = new HashMap<>();
    String jobCurrentlyInWork;

    public SonarGrabbedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
        googleDriveApiGateway = new GoogleDriveApiGateway();
        serieRefresher = new SerieRefresher(configFileLoader);
        this.configFileLoader = configFileLoader;
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        new SonarGrabbedDownloadsHandler(configFileLoader).handle();
    }

    @Override
    public void handle() {
        moveUncompletedJobsToRetry();
        List<File> jobFiles = retrieveJobs(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX));
        if (!jobFiles.isEmpty()) {
//       { while (CollectionUtils.isNotEmpty(retrieveJobs()) && !allJobsFinished()) {
            ExecutorService executor = Executors.newFixedThreadPool(jobFiles.size());
            for (File jobFile : jobFiles) {
                try {
                    SonarrJobFileManager jobFileManager = new SonarrJobFileManager(jobFile);
                    if (!jobFileManager.hasInfo()) throw new IncorrectWorkingReferencesException("no valid info at file");
                    SonarrJobHandler job = new SonarrJobHandler(configFileLoader, jobFileManager, this);
                    executor.execute(job);
                    TimeUnit.SECONDS.sleep(30);
                } catch (IOException | IncorrectWorkingReferencesException | InterruptedException e) {
                    log("not going to work with " + jobFile.getAbsolutePath());
                    if (e instanceof InterruptedException) Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean isWorkingWithAJob() {
        return jobCurrentlyInWork!=null;
    }

    public boolean isJobWorking(String jobTitle) {
        return jobTitle.equals(jobCurrentlyInWork);
    }

    public void jobInitiated(String downloadId) {
        jobsState.put(downloadId, "initiated");
    }

    public void jobHasFileName(String jobTitle) {
        jobsState.put(jobTitle, "has filename");
    }

    public void jobWorking(String jobTitle) {
        log("WORKING WITH "+jobTitle);
        jobsState.put(jobTitle, "working");
        jobCurrentlyInWork=jobTitle;
    }

    public void jobFinished(String jobTitle) {
        log("NOT WORKING ANYMORE WITH "+jobTitle);
        jobsState.put(jobTitle, "finished");
        jobCurrentlyInWork=null;
    }

    private boolean allJobsFinished() {
        return !jobsState.isEmpty() && jobsState.values().stream().allMatch(state -> state.equals("finished"));
    }

    private void log (String msg) {
        logWithDate("ORCHESTRATOR: "+msg);
    }

}

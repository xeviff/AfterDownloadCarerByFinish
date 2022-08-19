package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.downloads.workers.Handler;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader.JOBS_DIRECTORY_PATH;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader.PATH_TODO;
import static cat.hack3.mangrana.utils.Output.log;

public class SonarGrabbedDownloadsHandler implements Handler {

    ConfigFileLoader configFileLoader;
    SonarrApiGateway sonarrApiGateway;
    GoogleDriveApiGateway googleDriveApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;

    public enum DownloadType {SEASON, EPISODE}

    public static final int CLOUD_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 30;
    public static final int SONARR_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 5;

    boolean workingWithAJob=false;
    List<File> jobFiles;
    Map<String, String> jobs = new HashMap<>();

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
        while (CollectionUtils.isNotEmpty(retrieveJobs()) && !allJobsFinished()) {
            ExecutorService executor = Executors.newFixedThreadPool(jobFiles.size());
            for (File jobFile : jobFiles) {
                try {
                    SonarrJobHandler job = new SonarrJobHandler(configFileLoader, new SonarrJobFileLoader(jobFile), this);
                    executor.execute(job);
                } catch (IOException | IncorrectWorkingReferencesException e) {
                    log("not going to work with " + jobFile.getAbsolutePath());
                }
            }
        }
    }

    private List<File> retrieveJobs() {
        log("retrieving job files from to_do folder");
        File jobsDir = new File(System.getProperty("user.dir") + JOBS_DIRECTORY_PATH + PATH_TODO);
        File[] files = jobsDir.listFiles();
        log("found files: "+ (files==null?0: files.length));
        jobFiles = files!=null
                ? Arrays.asList(files)
                : Collections.emptyList();
        return jobFiles;
    }

    public boolean isWorkingWithAJob() {
        return workingWithAJob;
    }

    public void setWorkingWithAJob(boolean workingWithAJob) {
        this.workingWithAJob = workingWithAJob;
    }

    public void jobInitiated(String downloadId) {
        jobs.put(downloadId, "initiated");
    }

    public void jobHasFileName(String downloadId) {
        jobs.put(downloadId, "has filename");
    }

    public void jobFinished(String downloadId) {
        jobs.put(downloadId, "finished");
    }

    private boolean allJobsFinished() {
        return jobs.values().stream().allMatch(state -> state.equals("finished"));
    }

}

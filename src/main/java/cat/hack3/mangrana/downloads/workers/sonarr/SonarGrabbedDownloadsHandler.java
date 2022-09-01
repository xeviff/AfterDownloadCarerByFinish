package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.downloads.workers.Handler;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.MissingElementException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.GRABBED_FILE_IDENTIFIER_REGEX;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager.moveUncompletedJobsToRetry;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager.retrieveJobFiles;
import static cat.hack3.mangrana.utils.Output.logWithDate;

public class SonarGrabbedDownloadsHandler implements Handler {

    ConfigFileLoader configFileLoader;
    SonarrApiGateway sonarrApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;

    public static final int CLOUD_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 30;
    public static final int SONARR_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 5;

    Map<String, String> jobsState = new HashMap<>();
    Set<String> handlingFiles = new HashSet<>();
    String jobCurrentlyInWork;

    public SonarGrabbedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
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
        handleJobsReadyToCopy();
        while (true) {
            List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX));
            if (!jobFiles.isEmpty()) {
                ExecutorService executor = Executors.newFixedThreadPool(jobFiles.size());
                for (File jobFile : jobFiles) {
                    if (handlingFiles.contains(jobFile.getName())) {
                        log("file already in treatment, ignoring... "+jobFile.getName());
                        continue;
                    }
                    try {
                        SonarrJobFile jobFileManager = new SonarrJobFile(jobFile);
                        if (!jobFileManager.hasInfo()) {
                            throw new IncorrectWorkingReferencesException("no valid info at file");
                        }
                        SonarrJobHandler job = new SonarrJobHandler(configFileLoader, jobFileManager, this);
                        executor.execute(job);
                        handlingFiles.add(jobFile.getName());
                        Thread.sleep(5000);
                    } catch (IOException | IncorrectWorkingReferencesException | InterruptedException e) {
                        log("not going to work with " + jobFile.getAbsolutePath());
                        if (e instanceof InterruptedException) Thread.currentThread().interrupt();
                    }
                }
            }
            try {
                Thread.sleep(5 * 60 * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handleJobsReadyToCopy() {
        log("in first place, going to try to copy those elements that are already downloaded");
        List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX));
        if (!jobFiles.isEmpty()) {
            for (File jobFile : jobFiles) {
                try {
                    SonarrJobFile jobFileManager = new SonarrJobFile(jobFile);
                    if (!jobFileManager.hasInfo()) {
                        throw new IncorrectWorkingReferencesException("no valid info at file");
                    }
                    SonarrJobHandler job = new SonarrJobHandler(configFileLoader, jobFileManager, this);
                    job.tryToMoveIfPossible();
                } catch (IOException | IncorrectWorkingReferencesException | NoSuchElementException |
                         MissingElementException e) {
                    log("not going to work with " + jobFile.getAbsolutePath());
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

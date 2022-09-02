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
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.GRABBED_FILE_IDENTIFIER_REGEX;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.IMMORTAL_PROCESS;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager.moveUncompletedJobsToRetry;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager.retrieveJobFiles;
import static cat.hack3.mangrana.utils.Output.logWithDate;
import static cat.hack3.mangrana.utils.Waiter.waitMinutes;
import static cat.hack3.mangrana.utils.Waiter.waitSeconds;

public class SonarGrabbedDownloadsHandler implements Handler {

    ConfigFileLoader configFileLoader;
    SonarrApiGateway sonarrApiGateway;
    RemoteCopyService copyService;
    SerieRefresher serieRefresher;

    public static final int CLOUD_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 15;
    public static final int SONARR_WAIT_INTERVAL = LocalEnvironmentManager.isLocal() ? 1 : 5;

    Map<String, String> jobsState = new HashMap<>();
    int reportDelayCounter = 0;
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
        handleRestOfJobs();
    }

    private void handleJobsReadyToCopy() {
        simpleLog(">>> in first place, going to try to copy those elements that are already downloaded <<<");
        List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX));
        if (!jobFiles.isEmpty()) {
            for (File jobFile : jobFiles) {
                SonarrJobHandler job = null;
                try {
                    SonarrJobFile jobFileManager = new SonarrJobFile(jobFile);
                    if (!jobFileManager.hasInfo()) {
                        throw new IncorrectWorkingReferencesException("no valid info at file");
                    }
                    job = new SonarrJobHandler(configFileLoader, jobFileManager, this);
                    job.tryToMoveIfPossible();
                } catch (IOException | IncorrectWorkingReferencesException | NoSuchElementException |
                         MissingElementException e) {
                    String identifier = jobFile.getAbsolutePath();
                    if (Objects.nonNull(job) && StringUtils.isNotEmpty(job.getFullTitle()))
                        identifier = job.getFullTitle();
                    simpleLog("not going to work now with " + identifier);
                }
            }
        }
        simpleLog(">>> finished --check and copy right away if possible-- round, now will start the normal process <<<");
    }

    private void handleRestOfJobs() {
        boolean keepLooping = true;
        while (keepLooping) {
            List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX));
            if (!jobFiles.isEmpty()) {
                ExecutorService executor = Executors.newFixedThreadPool(jobFiles.size());
                handleJobs(jobFiles, executor);
            }
            waitMinutes(5);
            keepLooping = Boolean.parseBoolean(configFileLoader.getConfig(IMMORTAL_PROCESS));
        }
    }

    private void handleJobs(List<File> jobFiles, ExecutorService executor) {
        long filesIncorporated = 0;
        long filesIgnored = 0;
        for (File jobFile : jobFiles) {
            if (handlingFiles.contains(jobFile.getName())) {
                filesIgnored++;
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
                filesIncorporated++;
                waitSeconds(5);
            } catch (IOException | IncorrectWorkingReferencesException e) {
                log("not going to work with " + jobFile.getAbsolutePath());
            }
        }
        log(MessageFormat.format("handled jobs loop resume: filesIncorporated={0}, filesIgnored={1}",
                filesIncorporated, filesIgnored));
        resumeJobsLogPrint();
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

    public void jobFinished(String jobTitle, String fileName) {
        log("NOT WORKING ANYMORE WITH "+jobTitle);
        jobsState.put(jobTitle, "finished");
        handlingFiles.remove(fileName);
        jobCurrentlyInWork=null;
    }

    private void resumeJobsLogPrint() {
        if (reportDelayCounter == 10) {
            simpleLog("**** RESUME JOBS ****");
            this.jobsState.forEach((jobName, state) ->
                    simpleLog(MessageFormat
                            .format("Job: {0} | current state: {1}"
                                    , jobName, state))
            );
            reportDelayCounter = 0;
            simpleLog("**** RESUME JOBS ****");
        } else {
            reportDelayCounter++;
        }
    }

    private void simpleLog (String msg) {
        log(msg);
    }

    private void log (String msg) {
        logWithDate("ORCHESTRATOR: "+msg);
    }

}

package cat.hack3.mangrana.downloads.workers.common;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFile;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobsResume;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.utils.EasyLogger;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.GRABBED_FILE_IDENTIFIER_REGEX;
import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.IMMORTAL_PROCESS;
import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager.moveUncompletedJobsToRetry;
import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager.retrieveJobFiles;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.Waiter.waitMinutes;
import static cat.hack3.mangrana.utils.Waiter.waitSeconds;

public abstract class GrabbedDownloadsHandler implements Handler, JobOrchestrator {

    private final EasyLogger logger;

    ConfigFileLoader configFileLoader;

    JobsResume jobsState = new JobsResume();
    Set<String> handlingFiles = new HashSet<>();
    String jobCurrentlyInWork;
    protected JobFileManager.JobFileType jobFileType;

    protected GrabbedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        this.logger = new EasyLogger("ORCHESTRATOR");
        this.configFileLoader = configFileLoader;
    }

    @Override
    public void handle() {
        moveUncompletedJobsToRetry(jobFileType);
        handleJobsReadyToCopy();
        handleRestOfJobs();
    }

    private void handleJobsReadyToCopy() {
        log(">>>> in first place, going to try to copy those {0} elements that are already downloaded <<<<", jobFileType.getFolderName());
        List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX), jobFileType);
        if (!jobFiles.isEmpty()) {
            for (File jobFile : jobFiles) {
                JobHandler job = null;
                try {
                    @SuppressWarnings("rawtypes")
                    JobFile jobFileManager = getJobFile(jobFile);
                    if (jobFileManager.hasNoInfo()) {
                        throw new IncorrectWorkingReferencesException("no valid info at file");
                    }
                    job = getJobHandler(configFileLoader, jobFileManager, this);
                    job.tryToMoveIfPossible();
                } catch (IOException | IncorrectWorkingReferencesException | NoSuchElementException |
                         NoElementFoundException | TooMuchTriesException e) {
                    String identifier = jobFile.getAbsolutePath();
                    if (Objects.nonNull(job) && StringUtils.isNotEmpty(job.getFullTitle()))
                        identifier = job.getFullTitle();
                    log("not going to work now with " + identifier);
                }
            }
        }
        log(">>>> finished --check and copy right away if possible-- round, now after a while will start the normal process <<<<");
        log("-------------------------------------------------------------------------------------------------------------------");
        if (!LocalEnvironmentManager.isLocal()) waitMinutes(1);
    }

    @SuppressWarnings("rawtypes")
    protected abstract JobFile getJobFile(File jobFile) throws IncorrectWorkingReferencesException;
    @SuppressWarnings("rawtypes")
    protected abstract JobHandler getJobHandler(ConfigFileLoader configFileLoader, JobFile jobFileManager, JobOrchestrator orchestrator) throws IOException;

    private void handleRestOfJobs() {
        boolean keepLooping = true;
        while (keepLooping) {
            List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX), jobFileType);
            if (!jobFiles.isEmpty()) {
                ExecutorService executor = Executors.newFixedThreadPool(jobFiles.size());
                handleJobsInParallel(jobFiles, executor);
            }
            jobsState.resumeJobsLogPrint();
            waitMinutes(5);
            keepLooping = Boolean.parseBoolean(configFileLoader.getConfig(IMMORTAL_PROCESS));
        }
    }

    private void handleJobsInParallel(List<File> jobFiles, ExecutorService executor) {
        long filesIncorporated = 0;
        long filesIgnored = 0;
        for (File jobFile : jobFiles) {
            if (handlingFiles.contains(jobFile.getName())) {
                filesIgnored++;
                continue;
            }
            try {
                @SuppressWarnings("rawtypes")
                JobFile jobFileManager = getJobFile(jobFile);
                if (jobFileManager.hasNoInfo()) {
                    throw new IncorrectWorkingReferencesException("no valid info at file");
                }
                JobHandler job = getJobHandler(configFileLoader, jobFileManager, this);
                executor.execute(job);
                handlingFiles.add(jobFile.getName());
                filesIncorporated++;
                waitSeconds(5);
            } catch (IOException | IncorrectWorkingReferencesException e) {
                logger.nLogD("not going to work with " + jobFile.getAbsolutePath());
            }
        }
        if (filesIncorporated > 0) {
            logger.nLogD("handled jobs loop resume: filesIncorporated={0}, filesIgnored={1}",
                    filesIncorporated, filesIgnored);
            try {
                configFileLoader.refresh();
            } catch (IncorrectWorkingReferencesException e) {
                logger.nHLog("couldn't refresh the values from the project config file");
            }
        }
    }

    public boolean isWorkingWithAJob() {
        return jobCurrentlyInWork!=null;
    }

    public boolean isJobWorking(String jobTitle) {
        return jobTitle.equals(jobCurrentlyInWork);
    }

    public void jobInitiated(String jobTitle) {
        jobsState.put(jobTitle, "initiated");
    }

    public void jobHasFileName(String jobTitle) {
        jobsState.put(jobTitle, "has filename");
    }

    public void jobWorking(String jobTitle) {
        logger.nLog("WORKING WITH "+jobTitle);
        jobsState.put(jobTitle, "working");
        jobCurrentlyInWork=jobTitle;
    }

    public void jobFinished(String jobTitle, String fileName) {
        logger.nLog("NOT WORKING ANYMORE WITH "+jobTitle);
        jobsState.put(jobTitle, "finished");
        handlingFiles.remove(fileName);
        jobCurrentlyInWork=null;
    }

    public void jobError(String jobTitle, String fileName) {
        logger.nLog("NOT WORKING ANYMORE WITH "+jobTitle);
        jobsState.put(jobTitle, "error");
        handlingFiles.remove(fileName);
        jobCurrentlyInWork=null;
    }

}

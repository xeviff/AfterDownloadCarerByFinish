package tv.mangrana.downloads.workers.common;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.config.LocalEnvironmentManager;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.common.jobs.JobsResume;
import tv.mangrana.downloads.workers.radarr.RadarGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.utils.EasyLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.GRABBED_FILE_IDENTIFIER_REGEX;
import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.IMMORTAL_PROCESS;
import static tv.mangrana.jobs.JobFileManager.moveUncompletedJobsToRetry;
import static tv.mangrana.jobs.JobFileManager.retrieveJobFiles;
import static tv.mangrana.utils.Output.log;
import static tv.mangrana.utils.Waiter.waitMinutes;
import static tv.mangrana.utils.Waiter.waitSeconds;

public class GrabbedDownloadsHandler implements Handler, JobOrchestrator {

    private final EasyLogger logger;

    ConfigFileLoader configFileLoader;

    JobsResume jobsState = new JobsResume();
    Set<String> handlingJobs = new HashSet<>();
    JobHandler jobCurrentlyInWork;

    RadarGrabbedDownloadsHandler radarHandler = new RadarGrabbedDownloadsHandler();
    SonarGrabbedDownloadsHandler sonarrHandler = new SonarGrabbedDownloadsHandler();

    public GrabbedDownloadsHandler(ConfigFileLoader configFileLoader) {
        this.logger = new EasyLogger("ORCHESTRATOR");
        this.configFileLoader = configFileLoader;
    }

    @Override
    public void handle() {
        if (!LocalEnvironmentManager.isLocal()) {
            moveUncompletedJobsToRetry(radarHandler.getJobFileType());
            moveUncompletedJobsToRetry(sonarrHandler.getJobFileType());
        }
        handleJobsReadyToCopy();
        handleRestOfJobs();
    }

    private void handleJobsReadyToCopy() {
        log(">>>> in first place, going to try to copy those elements that are already downloaded <<<<");
        List<JobHandler> jobs = resolveJobHandlers(radarHandler);
        jobs.addAll(resolveJobHandlers(sonarrHandler));
        if (!jobs.isEmpty()) {
            for (JobHandler job : jobs) {
                try {
                    job.tryToMoveIfPossible();
                } catch (NoElementFoundException e) {
                    logger.nLog("not going to work now with {0} because its content is not present yet", job.getFullTitle());
                } catch (Exception e) {
                    logger.nHLog("unexpected error of type {0} when trying to crash-handle the element {1}", e.getMessage(), job.getFullTitle());
                    e.printStackTrace();
                }
            }
        }
        log(">>>> finished --check and copy right away if possible-- round, now after a while will start the normal process <<<<");
        String endLine = "-------------------------------------------------------------------------------------------------------------------";
        log(endLine); log(endLine);
    }

    private List<JobHandler> resolveJobHandlers (AppGrabbedDownloadsHandler downloadsHandler) {
        long filesIncorporated = 0;
        long filesIgnored = 0;
        List<JobHandler> jobs = new ArrayList<>();
        List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX), downloadsHandler.getJobFileType());
        if (!jobFiles.isEmpty()) {
            for (File jobFile : jobFiles) {
                if (handlingJobs.contains(jobFile.getName())) {
                    filesIgnored++;
                    continue;
                }
                try {
                    @SuppressWarnings("rawtypes")
                    JobFile jobFileManager = downloadsHandler.provideJobFile(jobFile);
                    if (jobFileManager.hasNoInfo()) {
                        throw new IncorrectWorkingReferencesException("no valid info at file");
                    }
                    JobHandler job = downloadsHandler.provideJobHandler(configFileLoader, jobFileManager, this);
                    jobs.add(job);
                    filesIncorporated++;
                } catch (IOException | IncorrectWorkingReferencesException  e) {
                    String identifier = jobFile.getAbsolutePath();
                    log("could not get the job from file " + identifier);
                }
            }
            if (filesIncorporated > 0) {
                logger.nLogD("Resolved {2} jobs for handling loop: filesIncorporated={0}, filesIgnored={1}",
                        filesIncorporated, filesIgnored, downloadsHandler.getJobFileType().getFolderName().toUpperCase());
                try {
                    configFileLoader.refresh();
                } catch (IncorrectWorkingReferencesException e) {
                    logger.nHLog("couldn't refresh the values from the project config file");
                }
            }
        }
        return jobs;
    }

    private void handleRestOfJobs() {
        boolean keepLooping = true;
        while (keepLooping) {
            List<JobHandler> jobs = resolveJobHandlers(radarHandler);
            jobs.addAll(resolveJobHandlers(sonarrHandler));
            if (!jobs.isEmpty()) {
                ExecutorService executor = Executors.newFixedThreadPool(jobs.size());
                handleJobsInParallel(jobs, executor);
            }
            jobsState.resumeJobsLogPrint(!jobs.isEmpty());
            waitMinutes(5);
            keepLooping = Boolean.parseBoolean(configFileLoader.getConfig(IMMORTAL_PROCESS));
        }
    }

    private void handleJobsInParallel(List<JobHandler> jobHandlers, ExecutorService executor) {
        for (JobHandler jobHandler : jobHandlers) {
            executor.execute(jobHandler);
            handlingJobs.add(getFileNameFromJob(jobHandler));
            waitSeconds(5);
        }
    }

    public boolean isWorkingWithAJob() {
        return jobCurrentlyInWork!=null;
    }

    public boolean isJobWorking(JobHandler job) {
        return job.equals(jobCurrentlyInWork);
    }

    public void jobInitiated(JobHandler job) {
        jobsState.put(job.getJobType(), job.getJobTitle(), "initiated");
    }

    public void jobHasFileName(JobHandler job) {
        jobsState.put(job.getJobType(), job.getJobTitle(), "has filename");
    }

    public void jobWorking(JobHandler job) {
        logger.nLog("WORKING WITH "+job.getFullTitle());
        jobsState.put(job.getJobType(), job.getJobTitle(), "working");
        jobCurrentlyInWork=job;
    }

    public void jobFinished(JobHandler job) {
        logger.nLog("NOT WORKING ANYMORE WITH "+job.getFullTitle());
        jobsState.put(job.getJobType(), job.getJobTitle(), "finished");
        handlingJobs.remove(getFileNameFromJob(job));
        jobCurrentlyInWork=null;
    }

    public void jobError(JobHandler job) {
        logger.nLog("NOT WORKING ANYMORE WITH "+job.getFullTitle());
        jobsState.put(job.getJobType(), job.getJobTitle(), "error");
        handlingJobs.remove(getFileNameFromJob(job));
        jobCurrentlyInWork=null;
    }

    private String getFileNameFromJob(JobHandler job) {
        return job.getJobFile().getFile().getName();
    }

}

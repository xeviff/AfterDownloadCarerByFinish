package tv.mangrana.downloads.workers;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.config.LocalEnvironmentManager;
import tv.mangrana.downloads.workers.common.AppGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.common.Handler;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.transmission.TransmissionJobFile;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.common.jobs.JobsResume;
import tv.mangrana.downloads.workers.radarr.RadarGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.jobs.JobFileManager;
import tv.mangrana.utils.EasyLogger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static tv.mangrana.jobs.JobFileManager.moveUncompletedJobsToRetry;
import static tv.mangrana.jobs.JobFileManager.retrieveJobFiles;
import static tv.mangrana.utils.Output.log;
import static tv.mangrana.utils.Waiter.waitMinutes;
import static tv.mangrana.utils.Waiter.waitSeconds;

public class FinishedDownloadsHandler implements Handler, JobOrchestrator {

    private final EasyLogger logger;

    ConfigFileLoader configFileLoader;

    JobsResume jobsState = new JobsResume();
    Set<String> handlingJobs = new HashSet<>();
    JobHandler jobCurrentlyInWork;

    RadarGrabbedDownloadsHandler radarHandler = new RadarGrabbedDownloadsHandler();
    SonarGrabbedDownloadsHandler sonarrHandler = new SonarGrabbedDownloadsHandler();

    public FinishedDownloadsHandler() throws IncorrectWorkingReferencesException {
        this.logger = new EasyLogger("IMMORTAL_HANDLER");
        configFileLoader = new ConfigFileLoader();
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException {
        new FinishedDownloadsHandler().handle();
    }

    @Override
    public void handle() {
        log("************************************************************************************************");
        log("************************************************************************************************");
        log("************************************************************************************************");
        log("********* Hi my friends, here the downloaded movies and series handler. Enjoy ******************");
        log("************************************************************************************************");
        log("************************************************************************************************");
        log("************************************************************************************************");

        if (!LocalEnvironmentManager.isLocal()) {
            moveUncompletedJobsToRetry(radarHandler.getJobFileType());
            moveUncompletedJobsToRetry(sonarrHandler.getJobFileType());
        }
        handleJobsReadyToCopy();
        //TODO implement continuous process
        //handleRestOfJobs();
    }

    private void handleJobsReadyToCopy() {
        log(">>>> in first place, going to try to copy those elements that are already downloaded <<<<");
        try {
            List<JobHandler> jobs = resolveJobHandlersFromTransmissionJobs();

            if (!jobs.isEmpty()) {
                for (JobHandler job : jobs) {
                    try {
                        job.tryToMoveIfPossible();
                    } catch (NoElementFoundException e) {
                        logger.nLog("not going to work now with {0} because its content is not present yet", job.getFullTitle());
                    } catch (Exception e) {
                        logger.nHLog("unexpected error of type {0} when handling the element {1}", e.getMessage(), job.getFullTitle());
                        e.printStackTrace();
                    }
                }
            } else {
                log("No jobs????");
            }
            log(">>>> finished --check and copy right away if possible-- round, now after a while will start the normal process <<<<");
            String endLine = "-------------------------------------------------------------------------------------------------------------------";
        log(endLine); log(endLine); log(endLine); log(endLine);
        } catch (IncorrectWorkingReferencesException e) {
            logger.nHLog("Has been a problem trying to retrieve the jobs from transmission, radarr or sonarr");
        }
    }

    private List<JobHandler> resolveJobHandlersFromTransmissionJobs() throws IncorrectWorkingReferencesException {
        List<JobHandler> presentJobs = new ArrayList<>();
        List<JobHandler> jobs = resolveJobHandlers(radarHandler);
        jobs.addAll(resolveJobHandlers(sonarrHandler));

        List<File> transmissionJobFiles = retrieveJobFiles(configFileLoader.getConfig(DOWNLOADED_TORRENT_FILE_IDENTIFIER_REGEX), JobFileManager.JobFileType.TRANSMISSION_JOBS);
        if (!transmissionJobFiles.isEmpty()) {
            for (File transmissionJobFile : transmissionJobFiles) {
                TransmissionJobFile transmissionJob = new TransmissionJobFile(transmissionJobFile);
                String torrentHash = transmissionJob.getInfo(TransmissionJobFile.GrabInfo.TORRENT_HASH);
                Optional<JobHandler> optionalArrJob = getJobByDownloadId(torrentHash, jobs);
                if (optionalArrJob.isPresent()) {
                    JobHandler arrJob = optionalArrJob.get();
                    arrJob.setTransmissionJob(transmissionJob);
                    presentJobs.add(arrJob);
                } else
                    logger.nHLog("Not found any sonarr/radarr job-file for this element: <{0}>", transmissionJob.getInfo(TransmissionJobFile.GrabInfo.TORRENT_NAME));
            }
        }
        return presentJobs;
    }

    private Optional<JobHandler> getJobByDownloadId(String torrentHash, List<JobHandler> jobs) {
        return Optional.ofNullable(torrentHash).flatMap(downloadHash -> jobs.stream()
                .filter(job -> downloadHash.toUpperCase().equals(job.getDownloadId()))
                .findFirst());
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
            waitMinutes(Integer.parseInt(configFileLoader.getConfig(JOB_FILES_PICK_UP_INTERVAL)));
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

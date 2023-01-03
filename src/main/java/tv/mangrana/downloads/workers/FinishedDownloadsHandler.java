package tv.mangrana.downloads.workers;

import org.apache.commons.collections4.CollectionUtils;
import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.config.LocalEnvironmentManager;
import tv.mangrana.downloads.workers.common.AppGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.common.Handler;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.common.jobs.JobHandler;
import tv.mangrana.downloads.workers.common.jobs.JobsResume;
import tv.mangrana.downloads.workers.radarr.RadarGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.sonarr.SonarGrabbedDownloadsHandler;
import tv.mangrana.downloads.workers.transmission.TransmissionJobFile;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.jobs.JobFileManager;
import tv.mangrana.utils.EasyLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static tv.mangrana.downloads.workers.common.jobs.JobHandler.COMPLETE_STATUS;
import static tv.mangrana.jobs.JobFileManager.moveUncompletedJobsToRetry;
import static tv.mangrana.jobs.JobFileManager.retrieveJobFiles;
import static tv.mangrana.utils.Output.log;
import static tv.mangrana.utils.Waiter.waitMinutes;

public class FinishedDownloadsHandler implements Handler, JobOrchestrator {

    private final EasyLogger logger;

    ConfigFileLoader configFileLoader;

    JobsResume jobsState = new JobsResume();

    RadarGrabbedDownloadsHandler radarHandler = new RadarGrabbedDownloadsHandler();
    SonarGrabbedDownloadsHandler sonarrHandler = new SonarGrabbedDownloadsHandler();

    public FinishedDownloadsHandler() throws IncorrectWorkingReferencesException {
        this.logger = new EasyLogger("ORCHESTRATOR");
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
        keepHandlingNewJobs();
    }

    private void handleJobsReadyToCopy() {
        logger.nLog(">>>> in first place, going to try to copy those elements that are already downloaded <<<<");
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
                logger.nLog("No existing jobs to handle");
            }
            logger.nLog(">>>> finished --check and copy right away if possible-- round, now after a while will start the normal process <<<<");
            String endLine = "-------------------------------------------------------------------------------------------------------------------";
        log(endLine); log(endLine); log(endLine); log(endLine);
        } catch (IncorrectWorkingReferencesException e) {
            logger.nHLog("Has been a problem trying to retrieve the jobs from transmission, radarr or sonarr");
        }
    }

    private List<JobHandler> resolveJobHandlersFromTransmissionJobs() throws IncorrectWorkingReferencesException {
        List<JobHandler> presentJobs = new ArrayList<>();
        List<JobHandler> candidateJobs = resolveJobHandlers(radarHandler);
        candidateJobs.addAll(resolveJobHandlers(sonarrHandler));

        List<File> transmissionJobFiles = retrieveJobFiles(configFileLoader.getConfig(DOWNLOADED_TORRENT_FILE_IDENTIFIER_REGEX), JobFileManager.JobFileType.TRANSMISSION_JOBS);
        if (!transmissionJobFiles.isEmpty()) {
            for (File transmissionJobFile : transmissionJobFiles) {
                TransmissionJobFile transmissionJob = new TransmissionJobFile(transmissionJobFile);
                String torrentHash = transmissionJob.getInfo(TransmissionJobFile.GrabInfo.TORRENT_HASH);
                Optional<JobHandler> optionalArrJob = getJobByDownloadId(torrentHash, candidateJobs);
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
        List<JobHandler> jobs = new ArrayList<>();
        List<File> jobFiles = retrieveJobFiles(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER_REGEX), downloadsHandler.getJobFileType());
        if (!jobFiles.isEmpty()) {
            for (File jobFile : jobFiles) {
                try {
                    @SuppressWarnings("rawtypes")
                    JobFile jobFileManager = downloadsHandler.provideJobFile(jobFile);
                    if (jobFileManager.hasNoInfo()) {
                        throw new IncorrectWorkingReferencesException("no valid info at file");
                    }
                    JobHandler job = downloadsHandler.provideJobHandler(configFileLoader, jobFileManager, this);
                    if (job.isAlreadyComplete()) {
                        logger.nLog("WARN: Job already completed. This file shouldn't be in this folder anymore ({0})", job.getJobTitle());
                        continue;
                    }
                    jobs.add(job);
                } catch (IOException | IncorrectWorkingReferencesException  e) {
                    String identifier = jobFile.getAbsolutePath();
                    log("could not get the job from file " + identifier);
                }
            }
        }
        return jobs;
    }

    private void keepHandlingNewJobs() {
        boolean keepLooping = true;
        while (keepLooping) {
            long filesIncorporated = 0;
            long filesIgnored = 0;
            List<JobHandler> candidateJobs = resolveJobHandlers(radarHandler);
            candidateJobs.addAll(resolveJobHandlers(sonarrHandler));
            for (JobHandler arrJob : candidateJobs) {
                if (jobsState.containsDownload(arrJob.getDownloadId()))
                    filesIgnored++;
                else {
                    jobsState.put(arrJob.getJobType(), arrJob.getDownloadId(), arrJob.getJobTitle(), "awaiting");
                    filesIncorporated++;
                }
            }
            List<File> transmissionJobFiles = retrieveJobFiles(configFileLoader.getConfig(DOWNLOADED_TORRENT_FILE_IDENTIFIER_REGEX), JobFileManager.JobFileType.TRANSMISSION_JOBS);
            if (CollectionUtils.isNotEmpty(transmissionJobFiles)) {
                handleJobsInParallel(candidateJobs, transmissionJobFiles);
            }
            if (filesIncorporated > 0) {
                logger.nLogD("Resolved jobs for handling loop: filesIncorporated={0}, filesIgnored={1}", filesIncorporated, filesIgnored);
            }
            jobsState.resumeJobsLogPrint(filesIncorporated > 0);
            waitMinutes(Integer.parseInt(configFileLoader.getConfig(JOB_FILES_PICK_UP_INTERVAL)));
            keepLooping = Boolean.parseBoolean(configFileLoader.getConfig(IMMORTAL_PROCESS));
        }
    }

    private void handleJobsInParallel(List<JobHandler> candidateJobs, List<File> transmissionJobFiles) {
        ExecutorService executor = Executors.newFixedThreadPool(transmissionJobFiles.size());
        getPresentJobsFromCandidates(candidateJobs, transmissionJobFiles).stream()
                .filter(job -> !jobsState.isJobWorking(job.getDownloadId()))
                .forEach(executor::execute);
    }

    private List<JobHandler> getPresentJobsFromCandidates(List<JobHandler> candidateJobs, List<File> transmissionJobFiles) {
        List<JobHandler> presentJobs = new ArrayList<>();
        for (File transmissionJobFile : transmissionJobFiles) {
            try {
                Optional<JobHandler> candidateArrJob = Optional.empty();
                TransmissionJobFile transmissionJob = new TransmissionJobFile(transmissionJobFile);
                String downloadId = transmissionJob.getInfo(TransmissionJobFile.GrabInfo.TORRENT_HASH);
                String torrentName = transmissionJob.getInfo(TransmissionJobFile.GrabInfo.TORRENT_NAME);
                String status = transmissionJob.getInfo(TransmissionJobFile.GrabInfo.STATUS);
                if (COMPLETE_STATUS.equals(status)) {
                    logger.nLog("WARN: Job already completed. This file shouldn't be in this folder anymore: {0}", transmissionJobFile.getName());
                    jobsState.put(JobFileManager.JobFileType.TRANSMISSION_JOBS, downloadId, torrentName, "already handled");
                } else {
                    if (!candidateJobs.isEmpty()) {
                        candidateArrJob = getJobByDownloadId(downloadId, candidateJobs);
                    } else {
                        logger.nHLog("No sonarr nor radarr jobs available");
                    }
                    if (candidateArrJob.isPresent()) {
                        candidateArrJob.get().setTransmissionJob(transmissionJob);
                        presentJobs.add(candidateArrJob.get());
                    } else if (!jobsState.containsDownload(downloadId)){
                        jobsState.put(JobFileManager.JobFileType.TRANSMISSION_JOBS, downloadId, torrentName, "no arr-job found");
                    }
                }
            } catch (Exception e) {
                logger.nHLog("Transmission job could not been prepared: {0}", transmissionJobFile.getName());
            }
        }
        return presentJobs;
    }

    public void jobWorking(JobHandler job) {
        logger.nLog("WORKING WITH "+job.getFullTitle());
        jobsState.put(job.getJobType(), job.getDownloadId(), job.getJobTitle(), "working");
    }

    public void jobFinished(JobHandler job) {
        logger.nLog("NOT WORKING ANYMORE WITH "+job.getFullTitle());
        jobsState.put(job.getJobType(), job.getDownloadId(), job.getJobTitle(), "finished");
    }

    public void jobError(JobHandler job) {
        logger.nLog("NOT WORKING ANYMORE WITH "+job.getFullTitle());
        jobsState.put(job.getJobType(), job.getDownloadId(), job.getJobTitle(), "error");
    }

}

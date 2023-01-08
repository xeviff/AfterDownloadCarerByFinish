package tv.mangrana.downloads.workers.common.jobs;

import org.apache.commons.lang.StringUtils;
import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.ElementHandler;
import tv.mangrana.downloads.workers.common.JobOrchestrator;
import tv.mangrana.downloads.workers.radarr.jobs.RadarrJobHandler;
import tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile;
import tv.mangrana.downloads.workers.sonarr.jobs.SonarrJobHandler;
import tv.mangrana.downloads.workers.transmission.TransmissionJobFile;
import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.exception.JobFileNotMovedException;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.google.api.client.RemoteCopyService;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.jobs.JobFileManager;
import tv.mangrana.utils.EasyLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JobHandler implements Runnable{

    protected EasyLogger logger;

    protected ConfigFileLoader configFileLoader;
    protected RemoteCopyService copyService;
    public static final String COMPLETE_STATUS = "complete";

    @SuppressWarnings("rawtypes")
    protected JobFile jobFile;
    final JobOrchestrator orchestrator;

    protected String jobTitle="-not_set-";
    protected String fullTitle;
    protected String elementName;
    protected String downloadId;
    private TransmissionJobFile transmissionJob;

    @SuppressWarnings("rawtypes")
    protected JobHandler(ConfigFileLoader configFileLoader, JobFile jobFile, JobOrchestrator caller) throws IOException, IncorrectWorkingReferencesException {
        this.configFileLoader = configFileLoader;
        copyService = new RemoteCopyService(configFileLoader);
        this.jobFile = jobFile;
        orchestrator = caller;
        try {
            loadInfoFromJobFile();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IncorrectWorkingReferencesException("A problem was risen when getting info from file: "+e.getMessage());
        }
    }

    protected abstract void loadInfoFromJobFile();
    public void tryToMoveIfPossible() throws IOException, IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException {
        if (StringUtils.isEmpty(elementName)) {
            logger.nLog("this job has not a fileName retrieved, so it cannot be processed.");
        } else {
            logger.nLog("going to try handle the following element: "+downloadId);
            getElementHandler().crashHandle();
            try {
                jobFile.forceMarkDone();
                transmissionJob.forceMarkDone();
            } catch (JobFileNotMovedException e) {
                orchestrator.blackListJob(this);
            }
        }
    }

    @Override
    public void run() {
        boolean error=false;
        try {
            logger.nLog("going to handle the so called <{0}>", fullTitle);
            setJobStateWorking();
            getElementHandler().handle();
            //writeCompletedStatusToJobInfo(jobFile.getFile());
            //writeCompletedStatusToJobInfo(transmissionJob.getFile());
            jobFile.markDone();
            transmissionJob.forceMarkDone();
        } catch (Exception e) {
            error=true;
            logger.nLog("Something wrong handling the job {1}: {0}", e.getMessage(), fullTitle);
            try {
                jobFile.driveBack();
            } catch (JobFileNotMovedException ex) {
                logger.nHLog("Couldn't move black the -doing- job to -to_do- folder");
            }
            e.printStackTrace();
        } finally {
            setJobStateFinished(error);
        }
    }

    protected abstract ElementHandler getElementHandler() throws IOException;

    private void writeCompletedStatusToJobInfo(File file) throws IOException {
        try (BufferedWriter output = new BufferedWriter(
                new FileWriter(file.getAbsolutePath(), true))) {
            output.newLine();
            output.append(SonarrJobFile.GrabInfo.STATUS.name().toLowerCase().concat(": " + COMPLETE_STATUS));
            logger.nLog("persisted status -complete- to job file " + file.getName());
        }
    }

    private void setJobStateWorking() {
        try {
            jobFile.markDoing();
        } catch (JobFileNotMovedException e) {
            logger.nLog("Job has not moved to -doing- folder");
        }
        orchestrator.jobWorking(this);
    }

    private void setJobStateFinished(boolean error) {
        synchronized (orchestrator) {
            if (error) {
                orchestrator.jobError(this);
            } else {
                orchestrator.jobFinished(this);
            }
            orchestrator.notifyAll();
        }
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public JobFileManager.JobFileType getJobType() {
        if (this instanceof RadarrJobHandler)
            return JobFileManager.JobFileType.RADARR_JOBS;
        else if (this instanceof SonarrJobHandler)
            return JobFileManager.JobFileType.SONARR_JOBS;
        else return null;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setTransmissionJob(TransmissionJobFile transmissionJob) {
        this.transmissionJob = transmissionJob;
        this.elementName = transmissionJob.getInfo(TransmissionJobFile.GrabInfo.TORRENT_NAME);
    }

    public abstract boolean isAlreadyComplete();
}

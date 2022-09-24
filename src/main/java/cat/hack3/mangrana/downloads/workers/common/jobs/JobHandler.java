package cat.hack3.mangrana.downloads.workers.common.jobs;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.ElementHandler;
import cat.hack3.mangrana.downloads.workers.common.JobOrchestrator;
import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.utils.EasyLogger;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;

public abstract class JobHandler implements Runnable {

    protected EasyLogger logger;

    protected ConfigFileLoader configFileLoader;
    protected RemoteCopyService copyService;
    @SuppressWarnings("rawtypes")
    protected JobFile jobFile;

    protected String jobTitle="-not_set-";
    final JobOrchestrator orchestrator;

    protected String fullTitle;
    protected String elementName;
    protected String fileName;
    protected String downloadId;

    @SuppressWarnings("rawtypes")
    protected JobHandler(ConfigFileLoader configFileLoader, JobFile jobFile, JobOrchestrator caller) throws IOException {
        this.configFileLoader = configFileLoader;
        copyService = new RemoteCopyService(configFileLoader);
        this.jobFile = jobFile;
        orchestrator = caller;
    }

    protected abstract void loadInfoFromJobFile();
    protected abstract ElementHandler getInitiatedHandler() throws IOException;
    public void tryToMoveIfPossible() throws IOException, IncorrectWorkingReferencesException, NoElementFoundException, TooMuchTriesException {
        loadInfoFromJobFile();
        if (StringUtils.isEmpty(fileName)) {
            logger.nLog("this job has not a fileName retrieved, so it cannot be processed.");
        } else {
            elementName = fileName;
            logger.nLog("going to try handle the following element: "+elementName);
            getInitiatedHandler().crashHandle();
            jobFile.forceMarkDone();
        }
    }

    @Override
    public void run() {
        boolean error=false;
        try {
            loadInfoFromJobFile();
            logger.nLog("going to handle the so called <{0}>", fullTitle);
            setJobStateInitiated();
            if (StringUtils.isNotEmpty(fileName)) {
                logger.nLog("Retrieved successfully from file the cached element name: <{0}> :D", fileName);
                elementName = fileName;
            } else {
                retrieveFileNameFromArrApp();
                writeElementNameToJobInfo(elementName);
            }
            setJobStateWorkingOrSleep();
            handleElement();
            jobFile.markDone();
        } catch (TooMuchTriesException | IOException | IncorrectWorkingReferencesException | NoElementFoundException e) {
            error=true;
            logger.nLog("something wrong: "+e.getMessage());
            jobFile.driveBack();
            e.printStackTrace();
        } finally {
            setJobStateFinished(error);
        }
    }

    protected abstract void handleElement() throws IOException, NoElementFoundException, IncorrectWorkingReferencesException, TooMuchTriesException;

    protected abstract void retrieveFileNameFromArrApp() throws TooMuchTriesException;

    private void writeElementNameToJobInfo(String elementName) throws IOException {
        try (Writer output = new BufferedWriter(
                new FileWriter(jobFile.getFile().getAbsolutePath(), true))) {
            output.append(SonarrJobFile.GrabInfo.JAVA_FILENAME.name().toLowerCase().concat(": "+elementName));
            logger.nLog("persisted elementName to job file -> "+elementName);
        }
    }

    private void setJobStateWorkingOrSleep() {
        synchronized (orchestrator) {
            orchestrator.jobHasFileName(jobTitle);
            if (orchestrator.isWorkingWithAJob()) {
                try {
                    Instant beforeWait = Instant.now();
                    while (orchestrator.isWorkingWithAJob()) {
                        orchestrator.wait();
                    }
                    Instant afterWait = Instant.now();
                    if (Duration.between(beforeWait, afterWait).toMinutes()<1) {
                        logger.nHLog("seems that things are going too fast between job sleep and its resume");
                    }
                } catch (InterruptedException e) {
                    logger.nHLog("could not put on waiting the job {0}", jobTitle);
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
            jobFile.markDoing();
            orchestrator.jobWorking(jobTitle);
        }
    }

    private void setJobStateInitiated() {
        synchronized (orchestrator) {
            orchestrator.jobInitiated(jobTitle);
        }
    }

    private void setJobStateFinished(boolean error) {
        synchronized (orchestrator) {
            if (error) {
                orchestrator.jobError(jobTitle, fileName);
            } else {
                orchestrator.jobFinished(jobTitle, fileName);
            }
            orchestrator.notifyAll();
        }
    }

    protected void logWhenActive(String msg, Object... params){
        if (!orchestrator.isWorkingWithAJob()
                || orchestrator.isJobWorking(jobTitle)) {
            logger.nLog(msg, params);
        }
    }

    public String getFullTitle() {
        return fullTitle;
    }
}

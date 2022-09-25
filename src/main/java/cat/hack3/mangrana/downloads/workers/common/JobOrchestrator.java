package cat.hack3.mangrana.downloads.workers.common;

import cat.hack3.mangrana.downloads.workers.common.jobs.JobFileManager;
import cat.hack3.mangrana.downloads.workers.common.jobs.JobHandler;

public interface JobOrchestrator {

    boolean isWorkingWithAJob();

    boolean isJobWorking(JobHandler job);

    void jobInitiated(JobHandler job);

    void jobHasFileName(JobHandler job);

    void jobWorking(JobHandler job);

    void jobFinished(JobHandler job);

    void jobError(JobHandler job);

}

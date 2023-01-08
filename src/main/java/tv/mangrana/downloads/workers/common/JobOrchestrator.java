package tv.mangrana.downloads.workers.common;

import tv.mangrana.downloads.workers.common.jobs.JobHandler;

public interface JobOrchestrator {

    void jobWorking(JobHandler job);

    void jobFinished(JobHandler job);

    void jobError(JobHandler job);

    void blackListJob(JobHandler job);
}

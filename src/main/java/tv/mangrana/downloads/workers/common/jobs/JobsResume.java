package tv.mangrana.downloads.workers.common.jobs;

import tv.mangrana.jobs.JobFileManager.JobFileType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static tv.mangrana.downloads.workers.common.jobs.JobInfo.*;
import static tv.mangrana.utils.Output.*;

public class JobsResume {

    Map<String, JobInfo> indexedJobsInfo = new HashMap<>();
    List<JobInfo> jobsState;
    List<JobInfo> jobsStatePrintedLastTime = Collections.emptyList();
    int reportDelayCounter = 0;

    public JobsResume(){
        jobsState = new ArrayList<>();
    }

    public void put(JobFileType jobType, String downloadId, String jobTitle, String state) {
        downloadId = downloadId.toUpperCase();
        LocalDateTime now = LocalDateTime.now();
        if (indexedJobsInfo.containsKey(downloadId)) {
            JobInfo jobInfo = indexedJobsInfo.get(downloadId);
            jobInfo.setState(state);
            jobInfo.setUpdateTime(now);
        } else {
            JobInfo jobInfo = new JobInfo(jobType, downloadId, jobTitle, state, now);
            indexedJobsInfo.put(downloadId, jobInfo);
            jobsState.add(jobInfo);
        }
    }

    public void overrideName(String downloadId, String newName) {
        indexedJobsInfo.get(downloadId.toUpperCase()).overrideName(JobInfo.cutTitle(newName));
    }

    public void resumeJobsLogPrint() {
        resumeJobsLogPrint(false);
    }
    public void resumeJobsLogPrint(boolean hasIncorporatedJobs) {
        if (hasIncorporatedJobs || !sameResumeAlreadyPrinted() || reportDelayCounter > 60) {
            log("**** JOBS RESUME ****");
            this.jobsState
                    .stream()
                    .sorted(Comparator.comparing(JobInfo::getUpdateTime).reversed())
                    .forEach(job ->
                            log("* Type: {3} | Id: {4} | Job: {0} | current state: {1} | updated: {2}",
                                    job.title, job.state, job.updateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)), job.jobType.getFolderName(), job.downloadId)
                    );
            reportDelayCounter = 0;
            logWithDate("**** JOBS RESUME ****");
            jobsStatePrintedLastTime = cloneJobsList(jobsState);
        } else {
            reportDelayCounter++;
        }
    }

    public boolean sameResumeAlreadyPrinted() {
        if (jobsStatePrintedLastTime.size() != jobsState.size()) return false;
        for (JobInfo job : jobsState) {
            if (!jobsStatePrintedLastTime.contains(job)) return false;
            JobInfo theOtherJob = jobsStatePrintedLastTime.get(jobsStatePrintedLastTime.indexOf(job));
            if (!theOtherJob.state.equals(job.state))  return false;
            if (!theOtherJob.updateTime.equals(job.updateTime))  return false;
        }
        return true;
    }

    public static List<JobInfo> cloneJobsList(List<JobInfo> list) {
        List<JobInfo> clone = new ArrayList<>(list.size());
        for (JobInfo job : list) clone.add(new JobInfo(job));
        return clone;
    }

    public boolean containsDownload (String downloadId) {
        return indexedJobsInfo.containsKey(downloadId);
    }

    public boolean isJobAlreadyInTreatment(String downloadId) {
        return containsDownload(downloadId)
                && (WORKING_STATE.equals(indexedJobsInfo.get(downloadId).state)
                || ERROR_STATE.equals(indexedJobsInfo.get(downloadId).state)
                || BLACKLISTED.equals(indexedJobsInfo.get(downloadId).state));
    }

}

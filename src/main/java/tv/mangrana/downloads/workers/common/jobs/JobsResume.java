package tv.mangrana.downloads.workers.common.jobs;

import tv.mangrana.jobs.JobFileManager.JobFileType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static tv.mangrana.utils.Output.*;

public class JobsResume {

    Map<String, JobInfo> indexedJobsInfo = new HashMap<>();
    List<JobInfo> jobsState;
    List<JobInfo> jobsStatePrintedLastTime = Collections.emptyList();
    int reportDelayCounter = 0;

    public JobsResume(){
        jobsState = new ArrayList<>();
    }

    private static class JobInfo {
        String downloadId;
        JobFileType jobType;
        String title;
        String state;
        LocalDateTime updateTime;
        public JobInfo(JobFileType jobType, String downloadId, String title, String state, LocalDateTime updateTime) {
            this.jobType = jobType;
            this.downloadId = downloadId;
            this.title = title;
            this.state = state;
            this.updateTime = updateTime;
        }
        public JobInfo(JobInfo toClone) {
            this(toClone.jobType, toClone.downloadId, toClone.title, toClone.state, toClone.updateTime);
        }
        public void setState(String state) {
            this.state = state;
        }
        public LocalDateTime getUpdateTime() {
            return updateTime;
        }
        public void setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JobInfo jobInfo = (JobInfo) o;
            return Objects.equals(downloadId, jobInfo.downloadId);
        }
        @Override
        public int hashCode() {
            return downloadId != null ? downloadId.hashCode() : 0;
        }
    }

    public void put(JobFileType jobType, String downloadId, String jobTitle, String state) {
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

    public void resumeJobsLogPrint() {
        resumeJobsLogPrint(false);
    }
    public void resumeJobsLogPrint(boolean hasIncorporatedJobs) {
        if (hasIncorporatedJobs || reportDelayCounter > 10 || !sameResumeAlreadyPrinted()) {
            log("**** JOBS RESUME ****");
            this.jobsState
                    .stream()
                    .sorted(Comparator.comparing(JobInfo::getUpdateTime))
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

}

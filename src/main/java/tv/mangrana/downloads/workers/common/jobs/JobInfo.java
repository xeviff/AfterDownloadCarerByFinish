package tv.mangrana.downloads.workers.common.jobs;

import tv.mangrana.jobs.JobFileManager;

import java.time.LocalDateTime;
import java.util.Objects;

public class JobInfo {
    public static final String WORKING_STATE = "working";
    public static final String ERROR_STATE = "error";
    public static final String BLACKLISTED = "BLACKLIST";
    String downloadId;
    JobFileManager.JobFileType jobType;
    String title;
    String state;
    LocalDateTime updateTime;

    public JobInfo(JobFileManager.JobFileType jobType, String downloadId, String title, String state, LocalDateTime updateTime) {
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

    public void overrideName(String newName) {
        this.title = newName;
    }

    public static String cutTitle (String title) {
        return title.length() > 45 ? title.substring(0,45)+"..." : title;
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

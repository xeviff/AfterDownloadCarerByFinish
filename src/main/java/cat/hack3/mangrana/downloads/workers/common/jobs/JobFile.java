package cat.hack3.mangrana.downloads.workers.common.jobs;

import cat.hack3.mangrana.config.LocalEnvironmentManager;

import java.io.File;

import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFile.JobLocation.*;
import static cat.hack3.mangrana.utils.PathUtils.shiftFileFolder;

public abstract class JobFile<E> {

    public enum JobLocation {
        PATH_TODO("to_do"),
        PATH_DOING("doing"),
        PATH_DONE("done");
        private final String folderName;
        private static final String LOCAL_PATH_TODO = "local_test";
        JobLocation(String folderName) {
            this.folderName=folderName;
        }
        public String getFolderName(){
            return getLocalNameIfNecessary(this);
        }
        private String getLocalNameIfNecessary(JobLocation location) {
            if (LocalEnvironmentManager.isLocal()
                    && location.equals(JobLocation.PATH_TODO)) {
                return LOCAL_PATH_TODO;
            }
            return location.folderName;
        }
    }

    protected JobFile(File jobFile) {
        this.jobFile = jobFile;
    }

    public abstract String getInfo(E key);

    public abstract boolean hasNoInfo();

    protected File jobFile;

    public File getFile () {
        return jobFile;
    }


    public void markDoing() {
        if (jobFile.getAbsolutePath().contains(PATH_TODO.folderName)) {
            jobFile = shiftFileFolder(jobFile, PATH_TODO, PATH_DOING);
        }
    }

    public void markDone() {
        jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_DONE);
    }

    public void forceMarkDone() {
        if (jobFile.getAbsolutePath().contains(PATH_DOING.folderName)) {
            jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_DONE);
        } else if (jobFile.getAbsolutePath().contains(PATH_TODO.folderName)) {
            jobFile = shiftFileFolder(jobFile, PATH_TODO, PATH_DONE);
        }
    }

    public void driveBack() {
        if (jobFile.getAbsolutePath().contains(PATH_DOING.folderName)) {
            jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_TODO);
        }
    }

}

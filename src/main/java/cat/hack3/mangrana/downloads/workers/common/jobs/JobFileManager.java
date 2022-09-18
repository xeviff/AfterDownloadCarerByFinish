package cat.hack3.mangrana.downloads.workers.common.jobs;

import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.utils.PathUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cat.hack3.mangrana.config.LocalEnvironmentManager.NAS_ACCESS_FOLDER_FROM_MAC;
import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFile.JobLocation;
import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFile.JobLocation.PATH_DOING;
import static cat.hack3.mangrana.downloads.workers.common.jobs.JobFile.JobLocation.PATH_TODO;
import static cat.hack3.mangrana.utils.PathUtils.addSubFolder;
import static cat.hack3.mangrana.utils.PathUtils.rootFolder;

public class JobFileManager {

    private JobFileManager(){}

    public static final String JOBS_FOLDER = "jobs";

    public enum JobFileType {
        SONARR_JOBS("sonarr"),
        RADARR_JOBS("radarr");
        private final String folderName;
        JobFileType(String folderName) {
            this.folderName=folderName;
        }
        public String getFolderName(){
            return folderName;
        }
    }

    public static void moveUncompletedJobsToRetry(JobFileType appType) {
        File jobsDir = new File(getAbsolutePath(PATH_DOING, appType));
        File[] files = jobsDir.listFiles();
        List<File> uncompleted = files!=null
                ? Arrays.asList(files)
                : Collections.emptyList();
        uncompleted.forEach(file -> PathUtils.shiftFileFolder(file, PATH_DOING, PATH_TODO));
    }

    public static List<File> retrieveJobFiles(String fileIdentifierRegex, JobFileType appType) {
        File jobsDir = new File(getAbsolutePath(PATH_TODO, appType));
        File[] files = jobsDir.listFiles();
        return files==null
                ? Collections.emptyList()
                : Arrays.stream(files)
                    .filter(file -> file.getName().matches(fileIdentifierRegex))
                    .sorted(PathUtils::compareFileCreationDate)
                    .collect(Collectors.toList());
    }

    public static String getAbsolutePath(JobLocation location, JobFileType appType) {
        String jobsFolder = LocalEnvironmentManager.isLocal()
                ? addSubFolder(rootFolder(NAS_ACCESS_FOLDER_FROM_MAC), JOBS_FOLDER)
                : rootFolder(JOBS_FOLDER);
        String appFolderPath =  addSubFolder(jobsFolder, appType.getFolderName());
        return addSubFolder(appFolderPath, location.getFolderName());
    }

}

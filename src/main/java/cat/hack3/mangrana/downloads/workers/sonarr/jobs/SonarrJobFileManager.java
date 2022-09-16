package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.config.LocalEnvironmentManager;
import cat.hack3.mangrana.utils.PathUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cat.hack3.mangrana.config.LocalEnvironmentManager.NAS_LOCAL_TEST_JOBS_PATH;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.JobLocation;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.JobLocation.PATH_DOING;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.JobLocation.PATH_TODO;
import static cat.hack3.mangrana.utils.PathUtils.addSubFolder;

public class SonarrJobFileManager {

    private SonarrJobFileManager(){}

    public static final String JOBS_FOLDER_NAME = "jobs";


    public static void moveUncompletedJobsToRetry() {
        File jobsDir = new File(getAbsolutePath(PATH_DOING));
        File[] files = jobsDir.listFiles();
        List<File> uncompleted = files!=null
                ? Arrays.asList(files)
                : Collections.emptyList();
        uncompleted.forEach(file -> PathUtils.shiftFileFolder(file, PATH_DOING, PATH_TODO));
    }

    public static List<File> retrieveJobFiles(String fileIdentifierRegex) {
        File jobsDir = new File(getAbsolutePath(PATH_TODO));
        File[] files = jobsDir.listFiles();
        return files==null
                ? Collections.emptyList()
                : Arrays.stream(files)
                    .filter(file -> file.getName().matches(fileIdentifierRegex))
                    .sorted(PathUtils::compareFileCreationDate)
                    .collect(Collectors.toList());
    }

    public static String getAbsolutePath(JobLocation location) {
        String jobsFolderPath = LocalEnvironmentManager.isLocal()
                ? NAS_LOCAL_TEST_JOBS_PATH
                : addSubFolder(PathUtils.getRootProjectPath(), JOBS_FOLDER_NAME);

        return addSubFolder(jobsFolderPath, location.getFolderName());
    }

}

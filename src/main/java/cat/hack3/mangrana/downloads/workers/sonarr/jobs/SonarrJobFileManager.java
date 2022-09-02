package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.utils.PathUtils;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.JobLocation;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.JobLocation.PATH_DOING;
import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.JobLocation.PATH_TODO;
import static cat.hack3.mangrana.utils.Output.log;

public class SonarrJobFileManager {

    private SonarrJobFileManager(){}

    public static final String JOBS_DIRECTORY_PATH = "/jobs/";

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
        log(MessageFormat.format("retrieved {0} job files from <to_do> folder", (files==null?0: files.length)));
        return files==null
                ? Collections.emptyList()
                : Arrays.stream(files)
                    .filter(file -> file.getName().matches(fileIdentifierRegex))
                    .sorted(PathUtils::compareFileCreationDate)
                    .collect(Collectors.toList());
    }

    public static String getAbsolutePath(JobLocation location) {
        return System.getProperty("user.dir") + JOBS_DIRECTORY_PATH + location.folderName;
    }


}

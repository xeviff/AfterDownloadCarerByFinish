package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.PathUtils;
import cat.hack3.mangrana.utils.yml.FakeYmlLoader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager.JobLocation.*;
import static cat.hack3.mangrana.utils.Output.log;
import static cat.hack3.mangrana.utils.PathUtils.shiftFileFolder;

public class SonarrJobFileManager {

    public enum JobLocation {
        PATH_TODO("to_do"),
        PATH_DOING("doing"),
        PATH_DONE("done");
        final String folderName;
        JobLocation(String folderName) {
            this.folderName=folderName;
        }
        public String getFolderName(){
            return folderName;
        }
    }

    public static final String JOBS_DIRECTORY_PATH = "/jobs/";
    private File jobFile;

    public enum GrabInfo {
        SONARR_RELEASE_TITLE,
        SONARR_SERIES_ID,
        SONARR_RELEASE_EPISODECOUNT,
        SONARR_DOWNLOAD_ID,
        JAVA_FILENAME,
    }

    private final EnumMap<GrabInfo, String> infoMap;

    public SonarrJobFileManager(File jobFile) throws IncorrectWorkingReferencesException {
        this.jobFile = jobFile;
        infoMap = FakeYmlLoader.getEnumMapFromFile(jobFile, GrabInfo.class);
    }

    public static void moveUncompletedJobsToRetry() {
        File jobsDir = new File(getAbsolutePath(PATH_DOING));
        File[] files = jobsDir.listFiles();
        List<File> uncompleted = files!=null
                ? Arrays.asList(files)
                : Collections.emptyList();
        uncompleted.forEach(file -> PathUtils.shiftFileFolder(file, PATH_DOING, PATH_TODO));
    }

    public static List<File> retrieveJobs(String fileIdentifierRegex) {
        log("retrieving job files from to_do folder");
        File jobsDir = new File(getAbsolutePath(PATH_TODO));
        File[] files = jobsDir.listFiles();
        log("found files: "+ (files==null?0: files.length));
        return files==null
                ? Collections.emptyList()
                : Arrays.stream(files)
                    .filter(file -> file.getName().matches(fileIdentifierRegex))
                    .sorted(PathUtils::compareFileCreationDate)
                    .collect(Collectors.toList());
    }

    public String getInfo(GrabInfo key) {
        return infoMap.get(key);
    }

    public boolean hasInfo() {
        return Objects.nonNull(infoMap);
    }

    public File getFile () {
        return jobFile;
    }

    public static String getAbsolutePath(JobLocation location) {
        return System.getProperty("user.dir") + JOBS_DIRECTORY_PATH + location.folderName;
    }

    public void markDoing() {
        if (jobFile.getAbsolutePath().contains(PATH_TODO.folderName)) {
            jobFile = shiftFileFolder(jobFile, PATH_TODO, PATH_DOING);
        }
    }

    public void markDone() {
        jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_DONE);
    }

    public void driveBack() {
        if (jobFile.getAbsolutePath().contains(PATH_DOING.folderName)) {
            jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_TODO);
        }
    }

}

package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.yml.FakeYmlLoader;

import java.io.File;
import java.util.EnumMap;
import java.util.Objects;

import static cat.hack3.mangrana.utils.PathUtils.shiftFileFolder;

public class SonarrJobFileLoader {

    public static final String PATH_TODO = "to_do";
    private static final String PATH_DOING = "doing";
    private static final String PATH_DONE = "done";

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

    public SonarrJobFileLoader(File jobFile) throws IncorrectWorkingReferencesException {
        this.jobFile = jobFile;
        infoMap = FakeYmlLoader.getEnumMapFromFile(jobFile, GrabInfo.class);
    }

//    private File retrieveJobFile(ConfigFileLoader configFileLoader, String stateFolder) {
//        File jobsDir = new File(System.getProperty("user.dir") + JOBS_DIRECTORY_PATH + stateFolder);
//        jobFile = Arrays.stream(Objects.requireNonNull(jobsDir.listFiles()))
//                .filter(File::isFile)
//                .filter(file -> file.getName().endsWith(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER).concat(".log")))
//                .max(PathUtils::compareFileCreationDate)
//                .orElse(null);
//        return jobFile;
//    }

    public String getInfo(GrabInfo key) {
        return infoMap.get(key);
    }

    public boolean hasInfo() {
        return Objects.nonNull(infoMap);
    }

    public File getFile () {
        return jobFile;
    }

    public void markDoing() {
        if (jobFile.getAbsolutePath().contains(PATH_TODO)) {
            jobFile = shiftFileFolder(jobFile, PATH_TODO, PATH_DOING);
        }
    }

    public void markDone() {
        jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_DONE);
    }

    public void driveBack() {
        if (jobFile.getAbsolutePath().contains(PATH_DOING)) {
            jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_TODO);
        }
    }

}

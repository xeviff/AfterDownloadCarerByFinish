package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.yml.YmlFileLoader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.NoSuchElementException;
import java.util.Objects;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.GRABBED_FILE_IDENTIFIER;
import static cat.hack3.mangrana.utils.PathUtils.shiftFileFolder;

public class SonarrJobFileLoader {

    private static final String PATH_TODO = "to_do";
    private static final String PATH_DOING = "doing";
    private static final String PATH_DONE = "done";

    public static final String JOBS_DIRECTORY_PATH = "/jobs/sonarr/"+ PATH_TODO;
    private File jobFile;

    public enum GrabInfo {
        SONARR_RELEASE_TITLE,
        SONARR_SERIES_ID,
        SONARR_RELEASE_EPISODECOUNT,
        SONARR_DOWNLOAD_ID,
        JAVA_FILENAME,
    }

    private final EnumMap<GrabInfo, String> infoMap;

    public SonarrJobFileLoader(ConfigFileLoader configFileLoader) throws IncorrectWorkingReferencesException {
        File jobsDir = new File(System.getProperty("user.dir") + JOBS_DIRECTORY_PATH);
        jobFile = Arrays.stream(Objects.requireNonNull(jobsDir.listFiles()))
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(configFileLoader.getConfig(GRABBED_FILE_IDENTIFIER).concat(".log")))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("no job file was found"));

        infoMap = YmlFileLoader.getEnumMapFromFile(jobFile, GrabInfo.class);
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

    public void markDoing() {
        jobFile = shiftFileFolder(jobFile, PATH_TODO, PATH_DOING);
    }

    public void markDone() {
        jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_DONE);
    }

    public void driveBack() {
        jobFile = shiftFileFolder(jobFile, PATH_DOING, PATH_TODO);
    }

}

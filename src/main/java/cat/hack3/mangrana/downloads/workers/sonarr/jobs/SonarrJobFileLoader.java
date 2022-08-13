package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.yml.YmlFileLoader;

import java.io.File;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SonarrJobFileLoader {

    private static final String JOBS_DIRECTORY_PATH = "/jobs/sonarr";

    public enum GrabInfo {
        SONARR_SERIES_ID,
        SONARR_RELEASE_EPISODECOUNT,
        SONARR_DOWNLOAD_ID,
        JAVA_FILENAME,
    }

    private final EnumMap<GrabInfo, String> infoMap;

    public SonarrJobFileLoader() throws IncorrectWorkingReferencesException {
        File jobsDir = new File(System.getProperty("user.dir") + JOBS_DIRECTORY_PATH);
        File jobFile = Arrays.stream(Objects.requireNonNull(jobsDir.listFiles()))
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith("-grab.log"))
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

}

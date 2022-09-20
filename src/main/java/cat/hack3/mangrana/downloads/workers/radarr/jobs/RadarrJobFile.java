package cat.hack3.mangrana.downloads.workers.radarr.jobs;

import cat.hack3.mangrana.downloads.workers.common.jobs.JobFile;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.yml.FakeYmlLoader;

import java.io.File;
import java.util.EnumMap;
import java.util.Objects;

public class RadarrJobFile extends JobFile<RadarrJobFile.GrabInfo> {

    public enum GrabInfo {
        RADARR_RELEASE_TITLE,
        RADARR_MOVIE_ID,
        RADARR_DOWNLOAD_ID,
        JAVA_FILENAME,
    }

    private final EnumMap<GrabInfo, String> infoMap;

    public RadarrJobFile(File jobFile) throws IncorrectWorkingReferencesException {
        super(jobFile);
        infoMap = FakeYmlLoader.getEnumMapFromFile(jobFile, GrabInfo.class, false);
    }

    @Override
    public String getInfo(GrabInfo key) {
        return infoMap.get(key);
    }

    public boolean hasNoInfo() {
        return !Objects.nonNull(infoMap);
    }

}
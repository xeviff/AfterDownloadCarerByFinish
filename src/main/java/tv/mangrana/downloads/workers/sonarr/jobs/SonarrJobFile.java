package tv.mangrana.downloads.workers.sonarr.jobs;

import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.utils.yml.FakeYmlLoader;

import java.io.File;
import java.util.EnumMap;
import java.util.Objects;

public class SonarrJobFile extends JobFile<SonarrJobFile.GrabInfo> {

    public enum GrabInfo {
        SONARR_RELEASE_TITLE,
        SONARR_SERIES_ID,
        SONARR_RELEASE_EPISODECOUNT,
        SONARR_DOWNLOAD_ID,
        STATUS,
    }

    private final EnumMap<GrabInfo, String> infoMap;

    public SonarrJobFile(File jobFile) throws IncorrectWorkingReferencesException {
        super(jobFile);
        infoMap = FakeYmlLoader.getEnumMapFromFile(jobFile, GrabInfo.class, true);
    }

    @Override
    public String getInfo(GrabInfo key) {
        return infoMap.get(key);
    }

    public boolean hasNoInfo() {
        return !Objects.nonNull(infoMap);
    }

}

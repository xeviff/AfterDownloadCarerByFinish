package tv.mangrana.downloads.workers.transmission;

import tv.mangrana.exception.IncorrectWorkingReferencesException;
import tv.mangrana.jobs.JobFile;
import tv.mangrana.utils.yml.FakeYmlLoader;

import java.io.File;
import java.util.EnumMap;
import java.util.Objects;

public class TransmissionJobFile extends JobFile<TransmissionJobFile.GrabInfo> {

    public enum GrabInfo {
        TORRENT_HASH,
        TORRENT_NAME,
        TORRENT_DIR,
    }

    private final EnumMap<GrabInfo, String> infoMap;

    public TransmissionJobFile(File jobFile) throws IncorrectWorkingReferencesException {
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

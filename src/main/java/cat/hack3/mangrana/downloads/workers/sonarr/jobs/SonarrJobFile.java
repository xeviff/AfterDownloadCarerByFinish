package cat.hack3.mangrana.downloads.workers.sonarr.jobs;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.yml.FakeYmlLoader;

import java.io.File;
import java.util.EnumMap;
import java.util.Objects;

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile.JobLocation.*;
import static cat.hack3.mangrana.utils.PathUtils.shiftFileFolder;

public class SonarrJobFile {

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

    private File jobFile;

    public enum GrabInfo {
        SONARR_RELEASE_TITLE,
        SONARR_SERIES_ID,
        SONARR_RELEASE_EPISODECOUNT,
        SONARR_DOWNLOAD_ID,
        JAVA_FILENAME,
    }

    private final EnumMap<GrabInfo, String> infoMap;

    public SonarrJobFile(File jobFile) throws IncorrectWorkingReferencesException {
        this.jobFile = jobFile;
        infoMap = FakeYmlLoader.getEnumMapFromFile(jobFile, GrabInfo.class, false);
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

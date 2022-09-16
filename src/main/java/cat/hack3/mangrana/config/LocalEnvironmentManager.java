package cat.hack3.mangrana.config;

import org.apache.commons.lang.StringUtils;

import static cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileManager.JOBS_FOLDER_NAME;
import static cat.hack3.mangrana.utils.PathUtils.*;

public class LocalEnvironmentManager {

    private LocalEnvironmentManager(){}

    public static final String NAS_ACCESS_FOLDER_FROM_MAC = "Volumes";
    public static final String SONARR_FOLDER = "sonarr";
    public static final String NAS_LOCAL_TEST_JOBS_PATH = addSubFolder(
            rootFolder(NAS_ACCESS_FOLDER_FROM_MAC),
            addSubFolder(JOBS_FOLDER_NAME, SONARR_FOLDER)
    );

    public static boolean isLocal () {
        String envVar = System.getenv("ENV");
        return
                StringUtils.isNotEmpty(envVar)
                && envVar.equals("local");
    }

}

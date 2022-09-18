package cat.hack3.mangrana.config;

import org.apache.commons.lang.StringUtils;

public class LocalEnvironmentManager {

    private LocalEnvironmentManager(){}

    public static final String NAS_ACCESS_FOLDER_FROM_MAC = "Volumes";

    public static boolean isLocal () {
        String envVar = System.getenv("ENV");
        return
                StringUtils.isNotEmpty(envVar)
                && envVar.equals("local");
    }

}

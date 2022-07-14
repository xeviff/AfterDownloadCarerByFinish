package cat.hack3.mangrana.config;

import org.apache.commons.lang.StringUtils;

public class LocalEnvironmentManager {

    private LocalEnvironmentManager(){}

    public static final String LOCAL_PROJECT_PATH = "/Volumes/usbshare1/docker/MANGRANA/my tools/java projects/AfterFilebotMoveProcessor";

    public static boolean isLocal () {
        String envVar = System.getenv("ENV");
        return
                StringUtils.isNotEmpty(envVar)
                && envVar.equals("local");
    }

}

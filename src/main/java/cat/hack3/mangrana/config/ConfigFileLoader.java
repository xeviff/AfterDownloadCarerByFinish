package cat.hack3.mangrana.config;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.yml.YmlFileLoader;
import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Optional;

import static cat.hack3.mangrana.utils.Output.log;

public class ConfigFileLoader {

    private static final String CONFIG_FOLDER = "/config";
    private static final String CONFIG_FILE = "AfterDownloadCarerConfig.yml";

    public enum ProjectConfiguration {
        MANAGE_FAILED_DOWNLOADS,
        GRABBED_FILE_IDENTIFIER,
        RADARR_API_KEY,
        RADARR_API_HOST,
        SONARR_API_KEY,
        SONARR_API_HOST,
        DOWNLOADS_TEAM_DRIVE_ID,
        MOVIES_TEAM_DRIVE_ID,
        SERIES_TEAM_DRIVE_ID,
        PLEX_TOKEN,
        PLEX_URL,
        PLEX_SECTION_REFRESH_URI,
        PLEX_SERIES_SECTION_ID
    }

    private EnumMap<ProjectConfiguration, String> configurationsMap;

    public ConfigFileLoader() throws IncorrectWorkingReferencesException {
        File configFile = new File(System.getProperty("user.dir")
                + CONFIG_FOLDER.concat("/").concat(CONFIG_FILE));

        configurationsMap = YmlFileLoader.getEnumMapFromFile(configFile, ProjectConfiguration.class);
    }

    public String getConfig(ProjectConfiguration key) {
        return configurationsMap.get(key);
    }

}

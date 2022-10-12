package tv.mangrana.config;

import tv.mangrana.exception.IncorrectWorkingReferencesException;

public class ConfigFileLoader extends CommonConfigFileLoader<ConfigFileLoader.ProjectConfiguration> {

    private static final String CONFIG_FILE = "AfterDownloadCarerConfig.yml";

    public ConfigFileLoader() throws IncorrectWorkingReferencesException {
        super(ProjectConfiguration.class);
    }

    public enum ProjectConfiguration {
        MANAGE_FAILED_DOWNLOADS,
        IMMORTAL_PROCESS,
        GRABBED_FILE_IDENTIFIER_REGEX,
        RADARR_API_KEY,
        RADARR_API_HOST,
        SONARR_API_KEY,
        SONARR_API_HOST,
        DOWNLOADS_TEAM_DRIVE_ID,
        DOWNLOADS_SERIES_FOLDER_ID,
        MOVIES_TEAM_DRIVE_ID,
        SERIES_TEAM_DRIVE_ID,
        GOOGLE_RETRY_INTERVAL,
        SONARR_RETRY_INTERVAL,
        RADARR_RETRY_INTERVAL,
    }

    @Override
    protected String getConfigFileName() {
        return CONFIG_FILE;
    }
}

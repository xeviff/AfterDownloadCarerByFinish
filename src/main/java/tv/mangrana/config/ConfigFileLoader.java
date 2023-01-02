package tv.mangrana.config;

import tv.mangrana.exception.IncorrectWorkingReferencesException;

public class ConfigFileLoader extends CommonConfigFileLoader<ConfigFileLoader.ProjectConfiguration> {

    private static final String CONFIG_FILE = "AfterDownloadCarerConfig.yml";

    public ConfigFileLoader() throws IncorrectWorkingReferencesException {
        super(ProjectConfiguration.class);
    }

    public enum ProjectConfiguration {
        IMMORTAL_PROCESS,
        CHECK_EPISODE_FILES_NUMBER_UPLOADED,
        GRABBED_FILE_IDENTIFIER_REGEX,
        DOWNLOADED_TORRENT_FILE_IDENTIFIER_REGEX,
        DOWNLOADS_TEAM_DRIVE_ID,
        DOWNLOADS_SERIES_FOLDER_ID,
        MOVIES_TEAM_DRIVE_ID,
        SERIES_TEAM_DRIVE_ID,
        GOOGLE_RETRY_INTERVAL,
        JOB_FILES_PICK_UP_INTERVAL,
        SONARR_RETRY_INTERVAL,
        RADARR_RETRY_INTERVAL,
    }

    @Override
    protected String getConfigFileName() {
        return CONFIG_FILE;
    }
}

package cat.hack3.mangrana.config;

import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static cat.hack3.mangrana.config.LocalEnvironmentManager.LOCAL_PROJECT_PATH;
import static cat.hack3.mangrana.config.LocalEnvironmentManager.isLocal;
import static cat.hack3.mangrana.utils.Output.log;

public class ConfigFileLoader {

    private static final String CONFIG_FOLDER = "/config";
    private static final String CONFIG_FILE = "AfterDownloadCarerConfig.yml";
    private static final String RADARR_API_KEY = "radarr_api_key";
    private static final String RADARR_HOST_KEY = "radarr_api_host";
    private static final String SONARR_API_KEY = "sonarr_api_key";
    private static final String SONARR_HOST_KEY = "sonarr_api_host";
    private static final String DOWNLOADS_TD_ID_KEY = "downloads_team_drive_id";
    private static final String MOVIES_TD_ID_KEY = "movies_team_drive_id";
    private static final String SERIES_TD_ID_KEY = "series_team_drive_id";
    private final String radarrApiKey;
    private final String radarrHost;
    private final String sonarrApiKey;
    private final String sonarrHost;
    private final String downloadsTDid;
    private final String moviesTDid;
    private final String seriesTDid;

    public ConfigFileLoader() throws IncorrectWorkingReferencesException {
        log("Loading values from the config file...");
        try {
            YamlMapping config = Yaml.createYamlInput(
                    new File(getConfigFolder().concat("/").concat(CONFIG_FILE)))
                    .readYamlMapping();

            radarrApiKey = Optional.ofNullable(
                     config.string(RADARR_API_KEY))
                     .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the radarr API key :(") );
            radarrHost = Optional.ofNullable(
                     config.string(RADARR_HOST_KEY))
                     .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the radarr HOST :(") );
            sonarrApiKey = Optional.ofNullable(
                            config.string(SONARR_API_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the sonarr API key :(") );
            sonarrHost = Optional.ofNullable(
                            config.string(SONARR_HOST_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the sonarr HOST :(") );

            downloadsTDid = Optional.ofNullable(
                            config.string(DOWNLOADS_TD_ID_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the downloads_team_drive_id :(") );
            moviesTDid = Optional.ofNullable(
                            config.string(MOVIES_TD_ID_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the movies_team_drive_id :(") );
            seriesTDid = Optional.ofNullable(
                            config.string(SERIES_TD_ID_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the series_team_drive_id :(") );
        } catch (IOException e) {
            throw new IncorrectWorkingReferencesException("couldn't find the config file :(");
        }
    }

    private String getConfigFolder(){
        return (isLocal() ? LOCAL_PROJECT_PATH : "")
                + CONFIG_FOLDER;
    }

    public String getRadarrApiKey() {
        return radarrApiKey;
    }
    public String getRadarrHost() {
        return radarrHost;
    }
    public String getSonarrApiKey() {
        return sonarrApiKey;
    }
    public String getSonarrHost() {
        return sonarrHost;
    }
    public String getDownloadsTDid() {
        return downloadsTDid;
    }
    public String getMoviesTDid() {
        return moviesTDid;
    }
    public String getSeriesTDid() {
        return seriesTDid;
    }
}

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
    private static final String CONFIG_FILE = "RadarrFixerConfig.yml";
    private static final String RADARR_API_KEY = "radarr_api_key";
    private static final String RADARR_HOST_KEY = "radarr_api_host";
    private static final String DOWNLOADS_TD_ID_KEY = "downloads_team_drive_id";
    private static final String MOVIES_TD_ID_KEY = "movies_team_drive_id";
    private final String apiKey;
    private final String host;
    private final String downloadsTDid;
    private final String moviesTDid;

    public ConfigFileLoader() throws IncorrectWorkingReferencesException {
        log("Loading values from the config file...");
        try {
            YamlMapping config = Yaml.createYamlInput(
                    new File(getConfigFolder().concat("/").concat(CONFIG_FILE)))
                    .readYamlMapping();

            apiKey = Optional.ofNullable(
                     config.string(RADARR_API_KEY))
                     .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the API key :(") );
            host = Optional.ofNullable(
                     config.string(RADARR_HOST_KEY))
                     .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the HOST :(") );
            downloadsTDid = Optional.ofNullable(
                            config.string(DOWNLOADS_TD_ID_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the downloads_team_drive_id :(") );
            moviesTDid = Optional.ofNullable(
                            config.string(MOVIES_TD_ID_KEY))
                    .orElseThrow(() -> new IncorrectWorkingReferencesException("Couldn't retrieve the movies_team_drive_id :(") );
        } catch (IOException e) {
            throw new IncorrectWorkingReferencesException("couldn't find the config file :(");
        }
    }

    private String getConfigFolder(){
        return (isLocal() ? LOCAL_PROJECT_PATH : "")
                + CONFIG_FOLDER;
    }

    public String getApiKey() {
        return apiKey;
    }
    public String getHost() {
        return host;
    }
    public String getDownloadsTDid() {
        return downloadsTDid;
    }
    public String getMoviesTDid() {
        return moviesTDid;
    }
}

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
    private final String apiKey;
    private final String host;

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
}

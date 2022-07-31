package cat.hack3.mangrana.radarr.api.schema;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.ClassGeneratorFromJson;

import java.io.IOException;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;

/**
 * @deprecated Once the generated classes for client schema are located in the project, this utility is not needed anymore
 */
@Deprecated
public class ClientSchemaGenerator {

    ConfigFileLoader configFileLoader;

    private ClientSchemaGenerator() throws IncorrectWorkingReferencesException {
        configFileLoader = new ConfigFileLoader();
    }

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
         new ClientSchemaGenerator().generateSonarrSeriesClientSchema();
    }

    private void generateRadarrClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(RADARR_HOST),
                configFileLoader.getConfig(RADARR_API_KEY),
                "/api/v3/queue?includeMovie=true?apikey=",
                "cat.hack3.mangrana.radarr.api.schema.queue",
                "QueueResourcePagingResource");
    }

    private void generateSonarrQueueClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(SONARR_HOST),
                "/api/v3/queue?apikey=",
                configFileLoader.getConfig(SONARR_API_KEY),
                "cat.hack3.mangrana.sonarr.api.schema.queue",
                "SonarrQueue");
    }

    private void generateSonarrSeriesClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(SONARR_HOST),
                "/api/v3/series/2220?apikey=",
                configFileLoader.getConfig(SONARR_API_KEY),
                "cat.hack3.mangrana.sonarr.api.schema.series",
                "SonarrSeries");
    }

    private void generate(String host, String uri, String apiKey, String pckg, String className) throws IOException {
        String schemaUrl = host.concat(uri.concat(apiKey));
        ClassGeneratorFromJson generatorFromJson = new ClassGeneratorFromJson();
        generatorFromJson.generateSchema(schemaUrl, pckg, className);
    }

}

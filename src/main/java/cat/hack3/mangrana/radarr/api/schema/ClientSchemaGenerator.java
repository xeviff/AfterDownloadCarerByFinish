package cat.hack3.mangrana.radarr.api.schema;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.utils.ClassGeneratorFromJson;

import java.io.IOException;

/**
 * @deprecated Once the generated classes for client schema are located in the project, this utility is not needed anymore
 */
@Deprecated
public class ClientSchemaGenerator {

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
         new ClientSchemaGenerator().generate();
    }

    private void generate() throws IncorrectWorkingReferencesException, IOException {
        ConfigFileLoader configFileLoader = new ConfigFileLoader();
        String schemaUrl = configFileLoader.getHost().concat("/api/v3/queue?includeMovie=true&apikey=".concat(configFileLoader.getApiKey()));
        ClassGeneratorFromJson generatorFromJson = new ClassGeneratorFromJson();
        generatorFromJson.generateSchema(schemaUrl, "cat.hack3.mangrana.radarr.api.schema.queue", "QueueResourcePagingResource");
    }

}

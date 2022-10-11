package tv.mangrana.utils.schema;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.IOException;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static tv.mangrana.utils.rest.APIInterface.ProtocolURLMark.HTTPS;

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
         new ClientSchemaGenerator().generateRadarrMovieClientSchema();
    }

    @SuppressWarnings("unused")
    private void generateRadarrQueueClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(RADARR_API_HOST),
                "/api/v3/queue?includeMovie=true?apikey=",
                configFileLoader.getConfig(RADARR_API_KEY),
                "tv.mangrana.radarr.api.schema.queue",
                "QueueResourcePagingResource");
    }

    @SuppressWarnings("unused")
    private void generateRadarrMovieClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(RADARR_API_HOST),
                "/api/v3/movie/9216?apikey=",
                configFileLoader.getConfig(RADARR_API_KEY),
                "tv.mangrana.radarr.api.schema.movie",
                "MovieResource");
    }

    @SuppressWarnings("unused")
    private void generateSonarrQueueClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(SONARR_API_HOST),
                "/api/v3/queue?apikey=",
                configFileLoader.getConfig(SONARR_API_KEY),
                "tv.mangrana.sonarr.api.schema.queue",
                "SonarrQueue");
    }

    @SuppressWarnings("unused")
    private void generateSonarrSeriesClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(SONARR_API_HOST),
                "/api/v3/series/2220?apikey=",
                configFileLoader.getConfig(SONARR_API_KEY),
                "tv.mangrana.sonarr.api.schema.series",
                "SonarrSeries");
    }

    @SuppressWarnings("unused")
    private void generateSonarrHistoryClientSchema() throws  IOException {
        generate(
                configFileLoader.getConfig(SONARR_API_HOST),
                "/api/v3/history?sortKey=date&apikey=",
                configFileLoader.getConfig(SONARR_API_KEY),
                "tv.mangrana.sonarr.api.schema.history",
                "SonarrHistory");
    }

    private void generate(String host, String uri, String apiKey, String pckg, String className) throws IOException {
        String schemaUrl = HTTPS.getMark()+host.concat(uri.concat(apiKey));
        ClassGeneratorFromJson generatorFromJson = new ClassGeneratorFromJson();
        generatorFromJson.generateSchema(schemaUrl, pckg, className);
    }

}

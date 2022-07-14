package cat.hack3.mangrana.radarr.api.client.process;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import cat.hack3.mangrana.radarr.api.schema.queue.Record;
import org.apache.commons.lang.NotImplementedException;

public class DownloadedExternallyHandler {

    RadarrApiGateway radarrApiGateway;
    ConfigFileLoader configFileLoader;

    private final String downloadedExternallyMsg = "Download wasn't grabbed by Radarr and not in a category, Skipping";

    public DownloadedExternallyHandler(ConfigFileLoader configFileLoader) throws IncorrectWorkingReferencesException {
        configFileLoader = new ConfigFileLoader();
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
    }

    public void handle () {
        QueueResourcePagingResource queue = radarrApiGateway.getQueue();
        queue.getRecords()
                .stream()
                .filter(rc -> rc.getStatusMessages()
                        .stream()
                        .anyMatch(stMsg -> stMsg.getMessages()
                                .stream()
                                .anyMatch(msg -> msg.contains(downloadedExternallyMsg))))
                .forEach(this::processRecord);
    }

    private void processRecord(Record record) {
        String downloadedFileName = record.getTitle();
        Integer movieRadarrId = record.getMovieId();
        String destinationPath = record.getMovie().getPath();
        copyMovieFromDownloadToRadarrFolder(downloadedFileName, destinationPath);
        notifyRadarrToRefresh(movieRadarrId);
    }

    private void copyMovieFromDownloadToRadarrFolder(String downloadedFileName, String destinationPath) {
        throw new NotImplementedException();
    }

    private String getDestinationPathFromMovieRadarrId(Integer movieRadarrId) {
        throw new NotImplementedException();
    }

    private void notifyRadarrToRefresh(Integer movieRadarrId) {
        throw new NotImplementedException();
    }

}

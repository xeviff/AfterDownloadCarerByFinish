package cat.hack3.mangrana.radarr.api.client.process;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import cat.hack3.mangrana.google.api.client.MovieCloudCopyService;
import cat.hack3.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import cat.hack3.mangrana.radarr.api.schema.queue.Record;
import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;

import static cat.hack3.mangrana.utils.Output.log;

public class DownloadedExternallyHandler {

    RadarrApiGateway radarrApiGateway;
    MovieCloudCopyService copyService;

    private final String downloadedExternallyMsg = "Download wasn't grabbed by Radarr and not in a category, Skipping";

    public DownloadedExternallyHandler(ConfigFileLoader configFileLoader) throws IOException {
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
        copyService = new MovieCloudCopyService(configFileLoader);
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

    private void processRecord(Record queueItem) {
        try {
            copyMovieFromDownloadToRadarrFolder(queueItem);
            notifyRadarrToRefresh(queueItem.getMovieId());
        } catch (IOException e) {
           log("could not copy the file :( "+queueItem.getTitle());
           e.printStackTrace();
        }
    }

    private void copyMovieFromDownloadToRadarrFolder(Record queueItem) throws IOException {
        copyService.copyVideoFile(queueItem.getTitle(), queueItem.getMovie().getPath());
    }

    private void notifyRadarrToRefresh(Integer movieRadarrId) {
        throw new NotImplementedException();
    }

}

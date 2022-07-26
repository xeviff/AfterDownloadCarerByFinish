package cat.hack3.mangrana.radarr.api.client.process;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.google.api.client.MovieCloudCopyService;
import cat.hack3.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import cat.hack3.mangrana.radarr.api.schema.queue.Record;

import java.io.IOException;

import static cat.hack3.mangrana.utils.Output.log;

public class FailedDownloadsHandler {

    RadarrApiGateway radarrApiGateway;
    MovieCloudCopyService copyService;


    public FailedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
        copyService = new MovieCloudCopyService(configFileLoader);
    }

    public void handle () {
        QueueResourcePagingResource queue = radarrApiGateway.getQueue();
        queue.getRecords()
                .stream()
                .filter(this::isRecordSeemsToFailed)
                .forEach(this::processRecord);
    }

    private void processRecord(Record queueItem) {
        try {
            copyService
                    .copyVideoFile(queueItem.getTitle(), queueItem.getMovie().getPath());
            radarrApiGateway
                    .removeQueueItem(queueItem.getId());
        } catch (IOException e) {
           log("could not copy the file :( "+queueItem.getTitle());
           e.printStackTrace();
        }
    }

    private boolean isRecordSeemsToFailed(Record rcd) {
        boolean importing = "importing".equals(rcd.getTrackedDownloadState());
        if (importing) log("skipping already importing download: "+rcd.getTitle());
        return
                "warning".equals(rcd.getTrackedDownloadStatus())
                && !importing;
    }

}

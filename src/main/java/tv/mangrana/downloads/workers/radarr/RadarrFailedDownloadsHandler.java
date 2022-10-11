package tv.mangrana.downloads.workers.radarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.Handler;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.google.api.client.RemoteCopyService;
import tv.mangrana.radarr.api.client.gateway.RadarrApiGateway;
import tv.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import tv.mangrana.radarr.api.schema.queue.Record;

import java.io.IOException;

import static tv.mangrana.utils.Output.log;
@Deprecated
public class RadarrFailedDownloadsHandler implements Handler {

    RadarrApiGateway radarrApiGateway;
    RemoteCopyService copyService;


    public RadarrFailedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        radarrApiGateway = new RadarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
    }

    public void handle () {
        log("this is the radarr failed downloads handler. Scanning queue...");
        QueueResourcePagingResource queue = radarrApiGateway.getQueue();
        queue.getRecords()
                .stream()
                .filter(this::isRecordSeemsToFailed)
                .forEach(this::processRecord);
    }

    private void processRecord(Record queueItem) {
        try {
            copyService
                    .copyMovieFile(queueItem.getTitle(), queueItem.getMovie().getPath());
            radarrApiGateway
                    .refreshMovie(queueItem.getMovieId());
            radarrApiGateway
                    .removeQueueItem(queueItem.getId());
        } catch (IOException | NoElementFoundException | TooMuchTriesException e) {
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

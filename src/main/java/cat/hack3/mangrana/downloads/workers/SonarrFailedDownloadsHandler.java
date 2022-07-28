package cat.hack3.mangrana.downloads.workers;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.google.api.client.RemoteCopyService;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;

import java.io.IOException;

import static cat.hack3.mangrana.utils.Output.log;

public class SonarrFailedDownloadsHandler {

    SonarrApiGateway sonarrApiGateway;
    RemoteCopyService copyService;


    public SonarrFailedDownloadsHandler(ConfigFileLoader configFileLoader) throws IOException {
        sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        copyService = new RemoteCopyService(configFileLoader);
    }

    public void handle () {
        log("this is the sonarr failed downloads handler. Scanning queue...");
        SonarrQueue queue = sonarrApiGateway.getQueue();
        log(queue.toString());
    }

}

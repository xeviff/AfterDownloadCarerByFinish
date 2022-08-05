package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.ParametrizedHandler;

import java.util.List;

public class SonarrFinishedDownloadsHandler implements ParametrizedHandler {

    public SonarrFinishedDownloadsHandler(ConfigFileLoader configFileLoader) {
    }

    @Override
    public void handle(List<String> parameters) {
        // TODO document why this method is empty
    }
}

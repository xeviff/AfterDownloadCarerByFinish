package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.common.ElementHandler;
import cat.hack3.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import cat.hack3.mangrana.utils.EasyLogger;

import java.io.IOException;

public abstract class SonarrElementHandler extends ElementHandler {

    protected final SerieRefresher serieRefresher;
    protected final SonarrApiGateway sonarrApiGateway;

    public SonarrElementHandler(EasyLogger logger, ConfigFileLoader configFileLoader) throws IOException {
        super(logger, configFileLoader);
        this.sonarrApiGateway = new SonarrApiGateway(configFileLoader);
        this.serieRefresher = new SerieRefresher(configFileLoader);
    }

}

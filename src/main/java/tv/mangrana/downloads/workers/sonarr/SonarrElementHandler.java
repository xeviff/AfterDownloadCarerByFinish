package tv.mangrana.downloads.workers.sonarr;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.ElementHandler;
import tv.mangrana.sonarr.api.client.gateway.SonarrApiGateway;
import tv.mangrana.utils.EasyLogger;

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

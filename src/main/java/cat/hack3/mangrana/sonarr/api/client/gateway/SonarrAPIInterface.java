package cat.hack3.mangrana.sonarr.api.client.gateway;

import cat.hack3.mangrana.radarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.sonarr.api.schema.command.RefreshSerieCommand;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.utils.rest.APIInterface;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * For more information, visit: <a href="https://github.com/Sonarr/Sonarr/wiki/API">...</a>
 */
@Path("/api/v3")
public interface SonarrAPIInterface extends APIInterface {

    @GET
    @Path("/queue")
    @Produces({ MediaType.APPLICATION_JSON })
    SonarrQueue getQueue(@QueryParam("apikey") String apikey);

    @DELETE
    @Path("/queue/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    void deleteQueueElement(@PathParam("id") Integer idElement, @QueryParam("apikey") String apikey);

    @GET
    @Path("/series/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    SonarrSerie getSerieById(@PathParam("id") Integer idSerie, @QueryParam("apikey") String apikey);

    @POST
    @Path("/command")
    @Consumes({ MediaType.APPLICATION_JSON })
    void refreshSeriesCommand(RefreshSerieCommand command, @QueryParam("apikey") String apikey);

}
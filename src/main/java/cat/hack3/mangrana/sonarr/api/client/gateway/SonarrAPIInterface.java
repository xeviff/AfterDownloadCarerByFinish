package cat.hack3.mangrana.sonarr.api.client.gateway;

import cat.hack3.mangrana.sonarr.api.schema.history.SonarrHistory;
import cat.hack3.mangrana.sonarr.api.schema.series.SonarrSerie;
import cat.hack3.mangrana.sonarr.api.schema.command.RefreshSerieCommand;
import cat.hack3.mangrana.sonarr.api.schema.queue.SonarrQueue;
import cat.hack3.mangrana.utils.rest.APIInterface;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * For more information, visit: <a href="https://github.com/Sonarr/Sonarr/wiki/API">official Documentation</a>
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
    void deleteQueueElement(@PathParam("id") Integer idElement, @QueryParam("removeFromClient") boolean removeFromClient,
                            @QueryParam("apikey") String apikey);

    @GET
    @Path("/series/{id}")
    @Produces({ MediaType.APPLICATION_JSON })
    SonarrSerie getSerieById(@PathParam("id") Integer idSerie, @QueryParam("apikey") String apikey);

    @POST
    @Path("/command")
    @Consumes({ MediaType.APPLICATION_JSON })
    void refreshSeriesCommand(RefreshSerieCommand command, @QueryParam("apikey") String apikey);

    /**
     * Get history. <a href="https://github.com/Sonarr/Sonarr/wiki/History">see here the official Documentation</a>
     * @param sortKey (REQUIRED, string) - series.title or date
     * @param page (int) - 1-indexed Default: 1
     * @param pageSize (int) - Default: 10
     * @param sortDir (string) - asc or desc - Default: desc
     */
    @GET
    @Path("/history")
    @Consumes({ MediaType.APPLICATION_JSON })
    SonarrHistory getHistory(@QueryParam("sortKey") String sortKey, @QueryParam("sortDir") String sortDir,
                             @QueryParam("pageSize") int pageSize, @QueryParam("page") int page,
                             @QueryParam("apikey") String apikey);

}
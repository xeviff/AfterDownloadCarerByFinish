package cat.hack3.mangrana.sonarr.api.client.gateway;

import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
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

}
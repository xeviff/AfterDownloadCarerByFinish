package cat.hack3.mangrana.radarr.api.client.gateway;

import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * For more information, visit: <a href="https://radarr.video/docs/api/#/Movie/get_api_v3_movie">...</a>
 */
@Path("/api/v3")
public interface RadarrAPIInterface {

    @GET
    @Path("/queue")
    @Produces({ MediaType.APPLICATION_JSON })
    QueueResourcePagingResource getQueue(@QueryParam("includeMovie") boolean includeMovie, @QueryParam("apikey") String apikey);


}
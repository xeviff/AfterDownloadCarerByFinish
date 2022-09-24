package cat.hack3.mangrana.radarr.api.client.gateway;

import cat.hack3.mangrana.radarr.api.schema.command.RefreshMoviesCommand;
import cat.hack3.mangrana.radarr.api.schema.movie.MovieResource;
import cat.hack3.mangrana.radarr.api.schema.queue.Movie;
import cat.hack3.mangrana.radarr.api.schema.queue.QueueResourcePagingResource;
import cat.hack3.mangrana.utils.rest.APIInterface;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * For more information, visit: <a href="https://radarr.video/docs/api/#/Movie/get_api_v3_movie">...</a>
 */
@Path("/api/v3")
public interface RadarrAPIInterface extends APIInterface {

    @GET
    @Path("/queue")
    @Produces({ MediaType.APPLICATION_JSON })
    QueueResourcePagingResource getQueue(@QueryParam("includeMovie") boolean includeMovie, @QueryParam("apikey") String apikey);

    @DELETE
    @Path("/queue/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    void removeQueueItem(@PathParam("id") int itemId, @QueryParam("removeFromClient") boolean removeFromClient, @QueryParam("apikey") String apikey);

    @GET
    @Path("/movie/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    MovieResource getMovieById(@PathParam("id") int movieId, @QueryParam("apikey") String apikey);

    @POST
    @Path("/command")
    @Consumes({ MediaType.APPLICATION_JSON })
    void refreshMoviesCommand(RefreshMoviesCommand command, @QueryParam("apikey") String apikey);

}
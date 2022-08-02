package cat.hack3.mangrana.radarr.api.client.schema.command;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class RefreshMoviesCommand {
    @JsonProperty("name")
    private final String name = "MoviesSearch";

    @JsonProperty("movieIds")
    private final List<Integer> moviesIds;

    public RefreshMoviesCommand(Integer movieId) {
        this.moviesIds = Collections.singletonList(movieId);
    }

    public List<Integer> getMoviesIds() {
        return moviesIds;
    }

    public String getName() {
        return name;
    }
}

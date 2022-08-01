package cat.hack3.mangrana.sonarr.api.schema.command;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshSerieCommand {

    @JsonProperty("name")
    private String name = "RefreshSeries";

    @JsonProperty("seriesId")
    private Integer serieId;

    public RefreshSerieCommand(Integer serieId) {
        this.serieId = serieId;
    }

    public String getName() {
        return name;
    }

    public Integer getSerieId() {
        return serieId;
    }
}


package cat.hack3.mangrana.radarr.api.schema.series;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "previousAiring",
    "episodeFileCount",
    "episodeCount",
    "totalEpisodeCount",
    "sizeOnDisk",
    "releaseGroups",
    "percentOfEpisodes"
})
@Generated("jsonschema2pojo")
public class Statistics {

    @JsonProperty("previousAiring")
    private String previousAiring;
    @JsonProperty("episodeFileCount")
    private Integer episodeFileCount;
    @JsonProperty("episodeCount")
    private Integer episodeCount;
    @JsonProperty("totalEpisodeCount")
    private Integer totalEpisodeCount;
    @JsonProperty("sizeOnDisk")
    private Long sizeOnDisk;
    @JsonProperty("releaseGroups")
    private List<Object> releaseGroups = new ArrayList<Object>();
    @JsonProperty("percentOfEpisodes")
    private Double percentOfEpisodes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("previousAiring")
    public String getPreviousAiring() {
        return previousAiring;
    }

    @JsonProperty("previousAiring")
    public void setPreviousAiring(String previousAiring) {
        this.previousAiring = previousAiring;
    }

    public Statistics withPreviousAiring(String previousAiring) {
        this.previousAiring = previousAiring;
        return this;
    }

    @JsonProperty("episodeFileCount")
    public Integer getEpisodeFileCount() {
        return episodeFileCount;
    }

    @JsonProperty("episodeFileCount")
    public void setEpisodeFileCount(Integer episodeFileCount) {
        this.episodeFileCount = episodeFileCount;
    }

    public Statistics withEpisodeFileCount(Integer episodeFileCount) {
        this.episodeFileCount = episodeFileCount;
        return this;
    }

    @JsonProperty("episodeCount")
    public Integer getEpisodeCount() {
        return episodeCount;
    }

    @JsonProperty("episodeCount")
    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Statistics withEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
        return this;
    }

    @JsonProperty("totalEpisodeCount")
    public Integer getTotalEpisodeCount() {
        return totalEpisodeCount;
    }

    @JsonProperty("totalEpisodeCount")
    public void setTotalEpisodeCount(Integer totalEpisodeCount) {
        this.totalEpisodeCount = totalEpisodeCount;
    }

    public Statistics withTotalEpisodeCount(Integer totalEpisodeCount) {
        this.totalEpisodeCount = totalEpisodeCount;
        return this;
    }

    @JsonProperty("sizeOnDisk")
    public Long getSizeOnDisk() {
        return sizeOnDisk;
    }

    @JsonProperty("sizeOnDisk")
    public void setSizeOnDisk(Long sizeOnDisk) {
        this.sizeOnDisk = sizeOnDisk;
    }

    public Statistics withSizeOnDisk(Long sizeOnDisk) {
        this.sizeOnDisk = sizeOnDisk;
        return this;
    }

    @JsonProperty("releaseGroups")
    public List<Object> getReleaseGroups() {
        return releaseGroups;
    }

    @JsonProperty("releaseGroups")
    public void setReleaseGroups(List<Object> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }

    public Statistics withReleaseGroups(List<Object> releaseGroups) {
        this.releaseGroups = releaseGroups;
        return this;
    }

    @JsonProperty("percentOfEpisodes")
    public Double getPercentOfEpisodes() {
        return percentOfEpisodes;
    }

    @JsonProperty("percentOfEpisodes")
    public void setPercentOfEpisodes(Double percentOfEpisodes) {
        this.percentOfEpisodes = percentOfEpisodes;
    }

    public Statistics withPercentOfEpisodes(Double percentOfEpisodes) {
        this.percentOfEpisodes = percentOfEpisodes;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Statistics withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Statistics.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("previousAiring");
        sb.append('=');
        sb.append(((this.previousAiring == null)?"<null>":this.previousAiring));
        sb.append(',');
        sb.append("episodeFileCount");
        sb.append('=');
        sb.append(((this.episodeFileCount == null)?"<null>":this.episodeFileCount));
        sb.append(',');
        sb.append("episodeCount");
        sb.append('=');
        sb.append(((this.episodeCount == null)?"<null>":this.episodeCount));
        sb.append(',');
        sb.append("totalEpisodeCount");
        sb.append('=');
        sb.append(((this.totalEpisodeCount == null)?"<null>":this.totalEpisodeCount));
        sb.append(',');
        sb.append("sizeOnDisk");
        sb.append('=');
        sb.append(((this.sizeOnDisk == null)?"<null>":this.sizeOnDisk));
        sb.append(',');
        sb.append("releaseGroups");
        sb.append('=');
        sb.append(((this.releaseGroups == null)?"<null>":this.releaseGroups));
        sb.append(',');
        sb.append("percentOfEpisodes");
        sb.append('=');
        sb.append(((this.percentOfEpisodes == null)?"<null>":this.percentOfEpisodes));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.previousAiring == null)? 0 :this.previousAiring.hashCode()));
        result = ((result* 31)+((this.episodeCount == null)? 0 :this.episodeCount.hashCode()));
        result = ((result* 31)+((this.totalEpisodeCount == null)? 0 :this.totalEpisodeCount.hashCode()));
        result = ((result* 31)+((this.releaseGroups == null)? 0 :this.releaseGroups.hashCode()));
        result = ((result* 31)+((this.percentOfEpisodes == null)? 0 :this.percentOfEpisodes.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.sizeOnDisk == null)? 0 :this.sizeOnDisk.hashCode()));
        result = ((result* 31)+((this.episodeFileCount == null)? 0 :this.episodeFileCount.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Statistics) == false) {
            return false;
        }
        Statistics rhs = ((Statistics) other);
        return (((((((((this.previousAiring == rhs.previousAiring)||((this.previousAiring!= null)&&this.previousAiring.equals(rhs.previousAiring)))&&((this.episodeCount == rhs.episodeCount)||((this.episodeCount!= null)&&this.episodeCount.equals(rhs.episodeCount))))&&((this.totalEpisodeCount == rhs.totalEpisodeCount)||((this.totalEpisodeCount!= null)&&this.totalEpisodeCount.equals(rhs.totalEpisodeCount))))&&((this.releaseGroups == rhs.releaseGroups)||((this.releaseGroups!= null)&&this.releaseGroups.equals(rhs.releaseGroups))))&&((this.percentOfEpisodes == rhs.percentOfEpisodes)||((this.percentOfEpisodes!= null)&&this.percentOfEpisodes.equals(rhs.percentOfEpisodes))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.sizeOnDisk == rhs.sizeOnDisk)||((this.sizeOnDisk!= null)&&this.sizeOnDisk.equals(rhs.sizeOnDisk))))&&((this.episodeFileCount == rhs.episodeFileCount)||((this.episodeFileCount!= null)&&this.episodeFileCount.equals(rhs.episodeFileCount))));
    }

}

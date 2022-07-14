
package cat.hack3.mangrana.radarr.api.schema.queue;

import java.util.LinkedHashMap;
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
    "imdb",
    "tmdb",
    "metacritic",
    "rottenTomatoes"
})
@Generated("jsonschema2pojo")
public class Ratings {

    @JsonProperty("imdb")
    private Imdb imdb;
    @JsonProperty("tmdb")
    private Tmdb tmdb;
    @JsonProperty("metacritic")
    private Metacritic metacritic;
    @JsonProperty("rottenTomatoes")
    private RottenTomatoes rottenTomatoes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("imdb")
    public Imdb getImdb() {
        return imdb;
    }

    @JsonProperty("imdb")
    public void setImdb(Imdb imdb) {
        this.imdb = imdb;
    }

    public Ratings withImdb(Imdb imdb) {
        this.imdb = imdb;
        return this;
    }

    @JsonProperty("tmdb")
    public Tmdb getTmdb() {
        return tmdb;
    }

    @JsonProperty("tmdb")
    public void setTmdb(Tmdb tmdb) {
        this.tmdb = tmdb;
    }

    public Ratings withTmdb(Tmdb tmdb) {
        this.tmdb = tmdb;
        return this;
    }

    @JsonProperty("metacritic")
    public Metacritic getMetacritic() {
        return metacritic;
    }

    @JsonProperty("metacritic")
    public void setMetacritic(Metacritic metacritic) {
        this.metacritic = metacritic;
    }

    public Ratings withMetacritic(Metacritic metacritic) {
        this.metacritic = metacritic;
        return this;
    }

    @JsonProperty("rottenTomatoes")
    public RottenTomatoes getRottenTomatoes() {
        return rottenTomatoes;
    }

    @JsonProperty("rottenTomatoes")
    public void setRottenTomatoes(RottenTomatoes rottenTomatoes) {
        this.rottenTomatoes = rottenTomatoes;
    }

    public Ratings withRottenTomatoes(RottenTomatoes rottenTomatoes) {
        this.rottenTomatoes = rottenTomatoes;
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

    public Ratings withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Ratings.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("imdb");
        sb.append('=');
        sb.append(((this.imdb == null)?"<null>":this.imdb));
        sb.append(',');
        sb.append("tmdb");
        sb.append('=');
        sb.append(((this.tmdb == null)?"<null>":this.tmdb));
        sb.append(',');
        sb.append("metacritic");
        sb.append('=');
        sb.append(((this.metacritic == null)?"<null>":this.metacritic));
        sb.append(',');
        sb.append("rottenTomatoes");
        sb.append('=');
        sb.append(((this.rottenTomatoes == null)?"<null>":this.rottenTomatoes));
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
        result = ((result* 31)+((this.metacritic == null)? 0 :this.metacritic.hashCode()));
        result = ((result* 31)+((this.tmdb == null)? 0 :this.tmdb.hashCode()));
        result = ((result* 31)+((this.imdb == null)? 0 :this.imdb.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.rottenTomatoes == null)? 0 :this.rottenTomatoes.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Ratings) == false) {
            return false;
        }
        Ratings rhs = ((Ratings) other);
        return ((((((this.metacritic == rhs.metacritic)||((this.metacritic!= null)&&this.metacritic.equals(rhs.metacritic)))&&((this.tmdb == rhs.tmdb)||((this.tmdb!= null)&&this.tmdb.equals(rhs.tmdb))))&&((this.imdb == rhs.imdb)||((this.imdb!= null)&&this.imdb.equals(rhs.imdb))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.rottenTomatoes == rhs.rottenTomatoes)||((this.rottenTomatoes!= null)&&this.rottenTomatoes.equals(rhs.rottenTomatoes))));
    }

}


package cat.hack3.mangrana.sonarr.api.schema.history;

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
    "version",
    "real",
    "isRepack"
})
@Generated("jsonschema2pojo")
public class Revision {

    @JsonProperty("version")
    private Integer version;
    @JsonProperty("real")
    private Integer real;
    @JsonProperty("isRepack")
    private Boolean isRepack;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    public Revision withVersion(Integer version) {
        this.version = version;
        return this;
    }

    @JsonProperty("real")
    public Integer getReal() {
        return real;
    }

    @JsonProperty("real")
    public void setReal(Integer real) {
        this.real = real;
    }

    public Revision withReal(Integer real) {
        this.real = real;
        return this;
    }

    @JsonProperty("isRepack")
    public Boolean getIsRepack() {
        return isRepack;
    }

    @JsonProperty("isRepack")
    public void setIsRepack(Boolean isRepack) {
        this.isRepack = isRepack;
    }

    public Revision withIsRepack(Boolean isRepack) {
        this.isRepack = isRepack;
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

    public Revision withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Revision.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null)?"<null>":this.version));
        sb.append(',');
        sb.append("real");
        sb.append('=');
        sb.append(((this.real == null)?"<null>":this.real));
        sb.append(',');
        sb.append("isRepack");
        sb.append('=');
        sb.append(((this.isRepack == null)?"<null>":this.isRepack));
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
        result = ((result* 31)+((this.real == null)? 0 :this.real.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.version == null)? 0 :this.version.hashCode()));
        result = ((result* 31)+((this.isRepack == null)? 0 :this.isRepack.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Revision) == false) {
            return false;
        }
        Revision rhs = ((Revision) other);
        return (((((this.real == rhs.real)||((this.real!= null)&&this.real.equals(rhs.real)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.version == rhs.version)||((this.version!= null)&&this.version.equals(rhs.version))))&&((this.isRepack == rhs.isRepack)||((this.isRepack!= null)&&this.isRepack.equals(rhs.isRepack))));
    }

}

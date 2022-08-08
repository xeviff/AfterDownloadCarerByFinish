
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
    "episodeId",
    "seriesId",
    "sourceTitle",
    "language",
    "quality",
    "qualityCutoffNotMet",
    "languageCutoffNotMet",
    "date",
    "downloadId",
    "eventType",
    "data",
    "id"
})
@Generated("jsonschema2pojo")
public class Record {

    @JsonProperty("episodeId")
    private Integer episodeId;
    @JsonProperty("seriesId")
    private Integer seriesId;
    @JsonProperty("sourceTitle")
    private String sourceTitle;
    @JsonProperty("language")
    private Language language;
    @JsonProperty("quality")
    private Quality quality;
    @JsonProperty("qualityCutoffNotMet")
    private Boolean qualityCutoffNotMet;
    @JsonProperty("languageCutoffNotMet")
    private Boolean languageCutoffNotMet;
    @JsonProperty("date")
    private String date;
    @JsonProperty("downloadId")
    private String downloadId;
    @JsonProperty("eventType")
    private String eventType;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("id")
    private Integer id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("episodeId")
    public Integer getEpisodeId() {
        return episodeId;
    }

    @JsonProperty("episodeId")
    public void setEpisodeId(Integer episodeId) {
        this.episodeId = episodeId;
    }

    public Record withEpisodeId(Integer episodeId) {
        this.episodeId = episodeId;
        return this;
    }

    @JsonProperty("seriesId")
    public Integer getSeriesId() {
        return seriesId;
    }

    @JsonProperty("seriesId")
    public void setSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
    }

    public Record withSeriesId(Integer seriesId) {
        this.seriesId = seriesId;
        return this;
    }

    @JsonProperty("sourceTitle")
    public String getSourceTitle() {
        return sourceTitle;
    }

    @JsonProperty("sourceTitle")
    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public Record withSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
        return this;
    }

    @JsonProperty("language")
    public Language getLanguage() {
        return language;
    }

    @JsonProperty("language")
    public void setLanguage(Language language) {
        this.language = language;
    }

    public Record withLanguage(Language language) {
        this.language = language;
        return this;
    }

    @JsonProperty("quality")
    public Quality getQuality() {
        return quality;
    }

    @JsonProperty("quality")
    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Record withQuality(Quality quality) {
        this.quality = quality;
        return this;
    }

    @JsonProperty("qualityCutoffNotMet")
    public Boolean getQualityCutoffNotMet() {
        return qualityCutoffNotMet;
    }

    @JsonProperty("qualityCutoffNotMet")
    public void setQualityCutoffNotMet(Boolean qualityCutoffNotMet) {
        this.qualityCutoffNotMet = qualityCutoffNotMet;
    }

    public Record withQualityCutoffNotMet(Boolean qualityCutoffNotMet) {
        this.qualityCutoffNotMet = qualityCutoffNotMet;
        return this;
    }

    @JsonProperty("languageCutoffNotMet")
    public Boolean getLanguageCutoffNotMet() {
        return languageCutoffNotMet;
    }

    @JsonProperty("languageCutoffNotMet")
    public void setLanguageCutoffNotMet(Boolean languageCutoffNotMet) {
        this.languageCutoffNotMet = languageCutoffNotMet;
    }

    public Record withLanguageCutoffNotMet(Boolean languageCutoffNotMet) {
        this.languageCutoffNotMet = languageCutoffNotMet;
        return this;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    public Record withDate(String date) {
        this.date = date;
        return this;
    }

    @JsonProperty("downloadId")
    public String getDownloadId() {
        return downloadId;
    }

    @JsonProperty("downloadId")
    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public Record withDownloadId(String downloadId) {
        this.downloadId = downloadId;
        return this;
    }

    @JsonProperty("eventType")
    public String getEventType() {
        return eventType;
    }

    @JsonProperty("eventType")
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Record withEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    public Record withData(Data data) {
        this.data = data;
        return this;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    public Record withId(Integer id) {
        this.id = id;
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

    public Record withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Record.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("episodeId");
        sb.append('=');
        sb.append(((this.episodeId == null)?"<null>":this.episodeId));
        sb.append(',');
        sb.append("seriesId");
        sb.append('=');
        sb.append(((this.seriesId == null)?"<null>":this.seriesId));
        sb.append(',');
        sb.append("sourceTitle");
        sb.append('=');
        sb.append(((this.sourceTitle == null)?"<null>":this.sourceTitle));
        sb.append(',');
        sb.append("language");
        sb.append('=');
        sb.append(((this.language == null)?"<null>":this.language));
        sb.append(',');
        sb.append("quality");
        sb.append('=');
        sb.append(((this.quality == null)?"<null>":this.quality));
        sb.append(',');
        sb.append("qualityCutoffNotMet");
        sb.append('=');
        sb.append(((this.qualityCutoffNotMet == null)?"<null>":this.qualityCutoffNotMet));
        sb.append(',');
        sb.append("languageCutoffNotMet");
        sb.append('=');
        sb.append(((this.languageCutoffNotMet == null)?"<null>":this.languageCutoffNotMet));
        sb.append(',');
        sb.append("date");
        sb.append('=');
        sb.append(((this.date == null)?"<null>":this.date));
        sb.append(',');
        sb.append("downloadId");
        sb.append('=');
        sb.append(((this.downloadId == null)?"<null>":this.downloadId));
        sb.append(',');
        sb.append("eventType");
        sb.append('=');
        sb.append(((this.eventType == null)?"<null>":this.eventType));
        sb.append(',');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null)?"<null>":this.data));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
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
        result = ((result* 31)+((this.date == null)? 0 :this.date.hashCode()));
        result = ((result* 31)+((this.languageCutoffNotMet == null)? 0 :this.languageCutoffNotMet.hashCode()));
        result = ((result* 31)+((this.data == null)? 0 :this.data.hashCode()));
        result = ((result* 31)+((this.sourceTitle == null)? 0 :this.sourceTitle.hashCode()));
        result = ((result* 31)+((this.downloadId == null)? 0 :this.downloadId.hashCode()));
        result = ((result* 31)+((this.language == null)? 0 :this.language.hashCode()));
        result = ((result* 31)+((this.eventType == null)? 0 :this.eventType.hashCode()));
        result = ((result* 31)+((this.episodeId == null)? 0 :this.episodeId.hashCode()));
        result = ((result* 31)+((this.seriesId == null)? 0 :this.seriesId.hashCode()));
        result = ((result* 31)+((this.quality == null)? 0 :this.quality.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.qualityCutoffNotMet == null)? 0 :this.qualityCutoffNotMet.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Record) == false) {
            return false;
        }
        Record rhs = ((Record) other);
        return ((((((((((((((this.date == rhs.date)||((this.date!= null)&&this.date.equals(rhs.date)))&&((this.languageCutoffNotMet == rhs.languageCutoffNotMet)||((this.languageCutoffNotMet!= null)&&this.languageCutoffNotMet.equals(rhs.languageCutoffNotMet))))&&((this.data == rhs.data)||((this.data!= null)&&this.data.equals(rhs.data))))&&((this.sourceTitle == rhs.sourceTitle)||((this.sourceTitle!= null)&&this.sourceTitle.equals(rhs.sourceTitle))))&&((this.downloadId == rhs.downloadId)||((this.downloadId!= null)&&this.downloadId.equals(rhs.downloadId))))&&((this.language == rhs.language)||((this.language!= null)&&this.language.equals(rhs.language))))&&((this.eventType == rhs.eventType)||((this.eventType!= null)&&this.eventType.equals(rhs.eventType))))&&((this.episodeId == rhs.episodeId)||((this.episodeId!= null)&&this.episodeId.equals(rhs.episodeId))))&&((this.seriesId == rhs.seriesId)||((this.seriesId!= null)&&this.seriesId.equals(rhs.seriesId))))&&((this.quality == rhs.quality)||((this.quality!= null)&&this.quality.equals(rhs.quality))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.qualityCutoffNotMet == rhs.qualityCutoffNotMet)||((this.qualityCutoffNotMet!= null)&&this.qualityCutoffNotMet.equals(rhs.qualityCutoffNotMet))));
    }

}

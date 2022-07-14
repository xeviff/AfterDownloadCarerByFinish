
package cat.hack3.mangrana.radarr.api.schema.queue;

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
    "movieId",
    "movie",
    "languages",
    "quality",
    "customFormats",
    "size",
    "title",
    "sizeleft",
    "status",
    "trackedDownloadStatus",
    "trackedDownloadState",
    "statusMessages",
    "downloadId",
    "protocol",
    "downloadClient",
    "outputPath",
    "id",
    "indexer"
})
@Generated("jsonschema2pojo")
public class Record {

    @JsonProperty("movieId")
    private Integer movieId;
    @JsonProperty("movie")
    private Movie movie;
    @JsonProperty("languages")
    private List<Language__1> languages = new ArrayList<Language__1>();
    @JsonProperty("quality")
    private Quality quality;
    @JsonProperty("customFormats")
    private List<Object> customFormats = new ArrayList<Object>();
    @JsonProperty("size")
    private Long size;
    @JsonProperty("title")
    private String title;
    @JsonProperty("sizeleft")
    private Long sizeleft;
    @JsonProperty("status")
    private String status;
    @JsonProperty("trackedDownloadStatus")
    private String trackedDownloadStatus;
    @JsonProperty("trackedDownloadState")
    private String trackedDownloadState;
    @JsonProperty("statusMessages")
    private List<StatusMessage> statusMessages = new ArrayList<StatusMessage>();
    @JsonProperty("downloadId")
    private String downloadId;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("downloadClient")
    private String downloadClient;
    @JsonProperty("outputPath")
    private String outputPath;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("indexer")
    private String indexer;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("movieId")
    public Integer getMovieId() {
        return movieId;
    }

    @JsonProperty("movieId")
    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Record withMovieId(Integer movieId) {
        this.movieId = movieId;
        return this;
    }

    @JsonProperty("movie")
    public Movie getMovie() {
        return movie;
    }

    @JsonProperty("movie")
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Record withMovie(Movie movie) {
        this.movie = movie;
        return this;
    }

    @JsonProperty("languages")
    public List<Language__1> getLanguages() {
        return languages;
    }

    @JsonProperty("languages")
    public void setLanguages(List<Language__1> languages) {
        this.languages = languages;
    }

    public Record withLanguages(List<Language__1> languages) {
        this.languages = languages;
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

    @JsonProperty("customFormats")
    public List<Object> getCustomFormats() {
        return customFormats;
    }

    @JsonProperty("customFormats")
    public void setCustomFormats(List<Object> customFormats) {
        this.customFormats = customFormats;
    }

    public Record withCustomFormats(List<Object> customFormats) {
        this.customFormats = customFormats;
        return this;
    }

    @JsonProperty("size")
    public Long getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Long size) {
        this.size = size;
    }

    public Record withSize(Long size) {
        this.size = size;
        return this;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Record withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("sizeleft")
    public Long getSizeleft() {
        return sizeleft;
    }

    @JsonProperty("sizeleft")
    public void setSizeleft(Long sizeleft) {
        this.sizeleft = sizeleft;
    }

    public Record withSizeleft(Long sizeleft) {
        this.sizeleft = sizeleft;
        return this;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public Record withStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("trackedDownloadStatus")
    public String getTrackedDownloadStatus() {
        return trackedDownloadStatus;
    }

    @JsonProperty("trackedDownloadStatus")
    public void setTrackedDownloadStatus(String trackedDownloadStatus) {
        this.trackedDownloadStatus = trackedDownloadStatus;
    }

    public Record withTrackedDownloadStatus(String trackedDownloadStatus) {
        this.trackedDownloadStatus = trackedDownloadStatus;
        return this;
    }

    @JsonProperty("trackedDownloadState")
    public String getTrackedDownloadState() {
        return trackedDownloadState;
    }

    @JsonProperty("trackedDownloadState")
    public void setTrackedDownloadState(String trackedDownloadState) {
        this.trackedDownloadState = trackedDownloadState;
    }

    public Record withTrackedDownloadState(String trackedDownloadState) {
        this.trackedDownloadState = trackedDownloadState;
        return this;
    }

    @JsonProperty("statusMessages")
    public List<StatusMessage> getStatusMessages() {
        return statusMessages;
    }

    @JsonProperty("statusMessages")
    public void setStatusMessages(List<StatusMessage> statusMessages) {
        this.statusMessages = statusMessages;
    }

    public Record withStatusMessages(List<StatusMessage> statusMessages) {
        this.statusMessages = statusMessages;
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

    @JsonProperty("protocol")
    public String getProtocol() {
        return protocol;
    }

    @JsonProperty("protocol")
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Record withProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    @JsonProperty("downloadClient")
    public String getDownloadClient() {
        return downloadClient;
    }

    @JsonProperty("downloadClient")
    public void setDownloadClient(String downloadClient) {
        this.downloadClient = downloadClient;
    }

    public Record withDownloadClient(String downloadClient) {
        this.downloadClient = downloadClient;
        return this;
    }

    @JsonProperty("outputPath")
    public String getOutputPath() {
        return outputPath;
    }

    @JsonProperty("outputPath")
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Record withOutputPath(String outputPath) {
        this.outputPath = outputPath;
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

    @JsonProperty("indexer")
    public String getIndexer() {
        return indexer;
    }

    @JsonProperty("indexer")
    public void setIndexer(String indexer) {
        this.indexer = indexer;
    }

    public Record withIndexer(String indexer) {
        this.indexer = indexer;
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
        sb.append("movieId");
        sb.append('=');
        sb.append(((this.movieId == null)?"<null>":this.movieId));
        sb.append(',');
        sb.append("movie");
        sb.append('=');
        sb.append(((this.movie == null)?"<null>":this.movie));
        sb.append(',');
        sb.append("languages");
        sb.append('=');
        sb.append(((this.languages == null)?"<null>":this.languages));
        sb.append(',');
        sb.append("quality");
        sb.append('=');
        sb.append(((this.quality == null)?"<null>":this.quality));
        sb.append(',');
        sb.append("customFormats");
        sb.append('=');
        sb.append(((this.customFormats == null)?"<null>":this.customFormats));
        sb.append(',');
        sb.append("size");
        sb.append('=');
        sb.append(((this.size == null)?"<null>":this.size));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("sizeleft");
        sb.append('=');
        sb.append(((this.sizeleft == null)?"<null>":this.sizeleft));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("trackedDownloadStatus");
        sb.append('=');
        sb.append(((this.trackedDownloadStatus == null)?"<null>":this.trackedDownloadStatus));
        sb.append(',');
        sb.append("trackedDownloadState");
        sb.append('=');
        sb.append(((this.trackedDownloadState == null)?"<null>":this.trackedDownloadState));
        sb.append(',');
        sb.append("statusMessages");
        sb.append('=');
        sb.append(((this.statusMessages == null)?"<null>":this.statusMessages));
        sb.append(',');
        sb.append("downloadId");
        sb.append('=');
        sb.append(((this.downloadId == null)?"<null>":this.downloadId));
        sb.append(',');
        sb.append("protocol");
        sb.append('=');
        sb.append(((this.protocol == null)?"<null>":this.protocol));
        sb.append(',');
        sb.append("downloadClient");
        sb.append('=');
        sb.append(((this.downloadClient == null)?"<null>":this.downloadClient));
        sb.append(',');
        sb.append("outputPath");
        sb.append('=');
        sb.append(((this.outputPath == null)?"<null>":this.outputPath));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("indexer");
        sb.append('=');
        sb.append(((this.indexer == null)?"<null>":this.indexer));
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
        result = ((result* 31)+((this.movie == null)? 0 :this.movie.hashCode()));
        result = ((result* 31)+((this.languages == null)? 0 :this.languages.hashCode()));
        result = ((result* 31)+((this.customFormats == null)? 0 :this.customFormats.hashCode()));
        result = ((result* 31)+((this.downloadId == null)? 0 :this.downloadId.hashCode()));
        result = ((result* 31)+((this.movieId == null)? 0 :this.movieId.hashCode()));
        result = ((result* 31)+((this.indexer == null)? 0 :this.indexer.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.trackedDownloadState == null)? 0 :this.trackedDownloadState.hashCode()));
        result = ((result* 31)+((this.trackedDownloadStatus == null)? 0 :this.trackedDownloadStatus.hashCode()));
        result = ((result* 31)+((this.sizeleft == null)? 0 :this.sizeleft.hashCode()));
        result = ((result* 31)+((this.quality == null)? 0 :this.quality.hashCode()));
        result = ((result* 31)+((this.statusMessages == null)? 0 :this.statusMessages.hashCode()));
        result = ((result* 31)+((this.protocol == null)? 0 :this.protocol.hashCode()));
        result = ((result* 31)+((this.size == null)? 0 :this.size.hashCode()));
        result = ((result* 31)+((this.outputPath == null)? 0 :this.outputPath.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        result = ((result* 31)+((this.downloadClient == null)? 0 :this.downloadClient.hashCode()));
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
        return ((((((((((((((((((((this.movie == rhs.movie)||((this.movie!= null)&&this.movie.equals(rhs.movie)))&&((this.languages == rhs.languages)||((this.languages!= null)&&this.languages.equals(rhs.languages))))&&((this.customFormats == rhs.customFormats)||((this.customFormats!= null)&&this.customFormats.equals(rhs.customFormats))))&&((this.downloadId == rhs.downloadId)||((this.downloadId!= null)&&this.downloadId.equals(rhs.downloadId))))&&((this.movieId == rhs.movieId)||((this.movieId!= null)&&this.movieId.equals(rhs.movieId))))&&((this.indexer == rhs.indexer)||((this.indexer!= null)&&this.indexer.equals(rhs.indexer))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.trackedDownloadState == rhs.trackedDownloadState)||((this.trackedDownloadState!= null)&&this.trackedDownloadState.equals(rhs.trackedDownloadState))))&&((this.trackedDownloadStatus == rhs.trackedDownloadStatus)||((this.trackedDownloadStatus!= null)&&this.trackedDownloadStatus.equals(rhs.trackedDownloadStatus))))&&((this.sizeleft == rhs.sizeleft)||((this.sizeleft!= null)&&this.sizeleft.equals(rhs.sizeleft))))&&((this.quality == rhs.quality)||((this.quality!= null)&&this.quality.equals(rhs.quality))))&&((this.statusMessages == rhs.statusMessages)||((this.statusMessages!= null)&&this.statusMessages.equals(rhs.statusMessages))))&&((this.protocol == rhs.protocol)||((this.protocol!= null)&&this.protocol.equals(rhs.protocol))))&&((this.size == rhs.size)||((this.size!= null)&&this.size.equals(rhs.size))))&&((this.outputPath == rhs.outputPath)||((this.outputPath!= null)&&this.outputPath.equals(rhs.outputPath))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))))&&((this.downloadClient == rhs.downloadClient)||((this.downloadClient!= null)&&this.downloadClient.equals(rhs.downloadClient))));
    }

}

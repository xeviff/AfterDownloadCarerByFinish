
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
    "indexer",
    "nzbInfoUrl",
    "releaseGroup",
    "age",
    "ageHours",
    "ageMinutes",
    "publishedDate",
    "downloadClient",
    "downloadClientName",
    "size",
    "downloadUrl",
    "guid",
    "tvdbId",
    "tvRageId",
    "protocol",
    "preferredWordScore",
    "torrentInfoHash",
    "fileId",
    "droppedPath",
    "importedPath"
})
@Generated("jsonschema2pojo")
public class Data {

    @JsonProperty("indexer")
    private String indexer;
    @JsonProperty("nzbInfoUrl")
    private String nzbInfoUrl;
    @JsonProperty("releaseGroup")
    private Object releaseGroup;
    @JsonProperty("age")
    private String age;
    @JsonProperty("ageHours")
    private String ageHours;
    @JsonProperty("ageMinutes")
    private String ageMinutes;
    @JsonProperty("publishedDate")
    private String publishedDate;
    @JsonProperty("downloadClient")
    private String downloadClient;
    @JsonProperty("downloadClientName")
    private String downloadClientName;
    @JsonProperty("size")
    private String size;
    @JsonProperty("downloadUrl")
    private String downloadUrl;
    @JsonProperty("guid")
    private String guid;
    @JsonProperty("tvdbId")
    private String tvdbId;
    @JsonProperty("tvRageId")
    private String tvRageId;
    @JsonProperty("protocol")
    private String protocol;
    @JsonProperty("preferredWordScore")
    private String preferredWordScore;
    @JsonProperty("torrentInfoHash")
    private String torrentInfoHash;
    @JsonProperty("fileId")
    private String fileId;
    @JsonProperty("droppedPath")
    private String droppedPath;
    @JsonProperty("importedPath")
    private String importedPath;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("indexer")
    public String getIndexer() {
        return indexer;
    }

    @JsonProperty("indexer")
    public void setIndexer(String indexer) {
        this.indexer = indexer;
    }

    public Data withIndexer(String indexer) {
        this.indexer = indexer;
        return this;
    }

    @JsonProperty("nzbInfoUrl")
    public String getNzbInfoUrl() {
        return nzbInfoUrl;
    }

    @JsonProperty("nzbInfoUrl")
    public void setNzbInfoUrl(String nzbInfoUrl) {
        this.nzbInfoUrl = nzbInfoUrl;
    }

    public Data withNzbInfoUrl(String nzbInfoUrl) {
        this.nzbInfoUrl = nzbInfoUrl;
        return this;
    }

    @JsonProperty("releaseGroup")
    public Object getReleaseGroup() {
        return releaseGroup;
    }

    @JsonProperty("releaseGroup")
    public void setReleaseGroup(Object releaseGroup) {
        this.releaseGroup = releaseGroup;
    }

    public Data withReleaseGroup(Object releaseGroup) {
        this.releaseGroup = releaseGroup;
        return this;
    }

    @JsonProperty("age")
    public String getAge() {
        return age;
    }

    @JsonProperty("age")
    public void setAge(String age) {
        this.age = age;
    }

    public Data withAge(String age) {
        this.age = age;
        return this;
    }

    @JsonProperty("ageHours")
    public String getAgeHours() {
        return ageHours;
    }

    @JsonProperty("ageHours")
    public void setAgeHours(String ageHours) {
        this.ageHours = ageHours;
    }

    public Data withAgeHours(String ageHours) {
        this.ageHours = ageHours;
        return this;
    }

    @JsonProperty("ageMinutes")
    public String getAgeMinutes() {
        return ageMinutes;
    }

    @JsonProperty("ageMinutes")
    public void setAgeMinutes(String ageMinutes) {
        this.ageMinutes = ageMinutes;
    }

    public Data withAgeMinutes(String ageMinutes) {
        this.ageMinutes = ageMinutes;
        return this;
    }

    @JsonProperty("publishedDate")
    public String getPublishedDate() {
        return publishedDate;
    }

    @JsonProperty("publishedDate")
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Data withPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
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

    public Data withDownloadClient(String downloadClient) {
        this.downloadClient = downloadClient;
        return this;
    }

    @JsonProperty("downloadClientName")
    public String getDownloadClientName() {
        return downloadClientName;
    }

    @JsonProperty("downloadClientName")
    public void setDownloadClientName(String downloadClientName) {
        this.downloadClientName = downloadClientName;
    }

    public Data withDownloadClientName(String downloadClientName) {
        this.downloadClientName = downloadClientName;
        return this;
    }

    @JsonProperty("size")
    public String getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(String size) {
        this.size = size;
    }

    public Data withSize(String size) {
        this.size = size;
        return this;
    }

    @JsonProperty("downloadUrl")
    public String getDownloadUrl() {
        return downloadUrl;
    }

    @JsonProperty("downloadUrl")
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Data withDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    @JsonProperty("guid")
    public String getGuid() {
        return guid;
    }

    @JsonProperty("guid")
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Data withGuid(String guid) {
        this.guid = guid;
        return this;
    }

    @JsonProperty("tvdbId")
    public String getTvdbId() {
        return tvdbId;
    }

    @JsonProperty("tvdbId")
    public void setTvdbId(String tvdbId) {
        this.tvdbId = tvdbId;
    }

    public Data withTvdbId(String tvdbId) {
        this.tvdbId = tvdbId;
        return this;
    }

    @JsonProperty("tvRageId")
    public String getTvRageId() {
        return tvRageId;
    }

    @JsonProperty("tvRageId")
    public void setTvRageId(String tvRageId) {
        this.tvRageId = tvRageId;
    }

    public Data withTvRageId(String tvRageId) {
        this.tvRageId = tvRageId;
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

    public Data withProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    @JsonProperty("preferredWordScore")
    public String getPreferredWordScore() {
        return preferredWordScore;
    }

    @JsonProperty("preferredWordScore")
    public void setPreferredWordScore(String preferredWordScore) {
        this.preferredWordScore = preferredWordScore;
    }

    public Data withPreferredWordScore(String preferredWordScore) {
        this.preferredWordScore = preferredWordScore;
        return this;
    }

    @JsonProperty("torrentInfoHash")
    public String getTorrentInfoHash() {
        return torrentInfoHash;
    }

    @JsonProperty("torrentInfoHash")
    public void setTorrentInfoHash(String torrentInfoHash) {
        this.torrentInfoHash = torrentInfoHash;
    }

    public Data withTorrentInfoHash(String torrentInfoHash) {
        this.torrentInfoHash = torrentInfoHash;
        return this;
    }

    @JsonProperty("fileId")
    public String getFileId() {
        return fileId;
    }

    @JsonProperty("fileId")
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Data withFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    @JsonProperty("droppedPath")
    public String getDroppedPath() {
        return droppedPath;
    }

    @JsonProperty("droppedPath")
    public void setDroppedPath(String droppedPath) {
        this.droppedPath = droppedPath;
    }

    public Data withDroppedPath(String droppedPath) {
        this.droppedPath = droppedPath;
        return this;
    }

    @JsonProperty("importedPath")
    public String getImportedPath() {
        return importedPath;
    }

    @JsonProperty("importedPath")
    public void setImportedPath(String importedPath) {
        this.importedPath = importedPath;
    }

    public Data withImportedPath(String importedPath) {
        this.importedPath = importedPath;
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

    public Data withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Data.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("indexer");
        sb.append('=');
        sb.append(((this.indexer == null)?"<null>":this.indexer));
        sb.append(',');
        sb.append("nzbInfoUrl");
        sb.append('=');
        sb.append(((this.nzbInfoUrl == null)?"<null>":this.nzbInfoUrl));
        sb.append(',');
        sb.append("releaseGroup");
        sb.append('=');
        sb.append(((this.releaseGroup == null)?"<null>":this.releaseGroup));
        sb.append(',');
        sb.append("age");
        sb.append('=');
        sb.append(((this.age == null)?"<null>":this.age));
        sb.append(',');
        sb.append("ageHours");
        sb.append('=');
        sb.append(((this.ageHours == null)?"<null>":this.ageHours));
        sb.append(',');
        sb.append("ageMinutes");
        sb.append('=');
        sb.append(((this.ageMinutes == null)?"<null>":this.ageMinutes));
        sb.append(',');
        sb.append("publishedDate");
        sb.append('=');
        sb.append(((this.publishedDate == null)?"<null>":this.publishedDate));
        sb.append(',');
        sb.append("downloadClient");
        sb.append('=');
        sb.append(((this.downloadClient == null)?"<null>":this.downloadClient));
        sb.append(',');
        sb.append("downloadClientName");
        sb.append('=');
        sb.append(((this.downloadClientName == null)?"<null>":this.downloadClientName));
        sb.append(',');
        sb.append("size");
        sb.append('=');
        sb.append(((this.size == null)?"<null>":this.size));
        sb.append(',');
        sb.append("downloadUrl");
        sb.append('=');
        sb.append(((this.downloadUrl == null)?"<null>":this.downloadUrl));
        sb.append(',');
        sb.append("guid");
        sb.append('=');
        sb.append(((this.guid == null)?"<null>":this.guid));
        sb.append(',');
        sb.append("tvdbId");
        sb.append('=');
        sb.append(((this.tvdbId == null)?"<null>":this.tvdbId));
        sb.append(',');
        sb.append("tvRageId");
        sb.append('=');
        sb.append(((this.tvRageId == null)?"<null>":this.tvRageId));
        sb.append(',');
        sb.append("protocol");
        sb.append('=');
        sb.append(((this.protocol == null)?"<null>":this.protocol));
        sb.append(',');
        sb.append("preferredWordScore");
        sb.append('=');
        sb.append(((this.preferredWordScore == null)?"<null>":this.preferredWordScore));
        sb.append(',');
        sb.append("torrentInfoHash");
        sb.append('=');
        sb.append(((this.torrentInfoHash == null)?"<null>":this.torrentInfoHash));
        sb.append(',');
        sb.append("fileId");
        sb.append('=');
        sb.append(((this.fileId == null)?"<null>":this.fileId));
        sb.append(',');
        sb.append("droppedPath");
        sb.append('=');
        sb.append(((this.droppedPath == null)?"<null>":this.droppedPath));
        sb.append(',');
        sb.append("importedPath");
        sb.append('=');
        sb.append(((this.importedPath == null)?"<null>":this.importedPath));
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
        result = ((result* 31)+((this.nzbInfoUrl == null)? 0 :this.nzbInfoUrl.hashCode()));
        result = ((result* 31)+((this.ageMinutes == null)? 0 :this.ageMinutes.hashCode()));
        result = ((result* 31)+((this.tvdbId == null)? 0 :this.tvdbId.hashCode()));
        result = ((result* 31)+((this.ageHours == null)? 0 :this.ageHours.hashCode()));
        result = ((result* 31)+((this.downloadUrl == null)? 0 :this.downloadUrl.hashCode()));
        result = ((result* 31)+((this.torrentInfoHash == null)? 0 :this.torrentInfoHash.hashCode()));
        result = ((result* 31)+((this.importedPath == null)? 0 :this.importedPath.hashCode()));
        result = ((result* 31)+((this.indexer == null)? 0 :this.indexer.hashCode()));
        result = ((result* 31)+((this.droppedPath == null)? 0 :this.droppedPath.hashCode()));
        result = ((result* 31)+((this.protocol == null)? 0 :this.protocol.hashCode()));
        result = ((result* 31)+((this.size == null)? 0 :this.size.hashCode()));
        result = ((result* 31)+((this.tvRageId == null)? 0 :this.tvRageId.hashCode()));
        result = ((result* 31)+((this.preferredWordScore == null)? 0 :this.preferredWordScore.hashCode()));
        result = ((result* 31)+((this.guid == null)? 0 :this.guid.hashCode()));
        result = ((result* 31)+((this.downloadClientName == null)? 0 :this.downloadClientName.hashCode()));
        result = ((result* 31)+((this.publishedDate == null)? 0 :this.publishedDate.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.age == null)? 0 :this.age.hashCode()));
        result = ((result* 31)+((this.releaseGroup == null)? 0 :this.releaseGroup.hashCode()));
        result = ((result* 31)+((this.downloadClient == null)? 0 :this.downloadClient.hashCode()));
        result = ((result* 31)+((this.fileId == null)? 0 :this.fileId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Data) == false) {
            return false;
        }
        Data rhs = ((Data) other);
        return ((((((((((((((((((((((this.nzbInfoUrl == rhs.nzbInfoUrl)||((this.nzbInfoUrl!= null)&&this.nzbInfoUrl.equals(rhs.nzbInfoUrl)))&&((this.ageMinutes == rhs.ageMinutes)||((this.ageMinutes!= null)&&this.ageMinutes.equals(rhs.ageMinutes))))&&((this.tvdbId == rhs.tvdbId)||((this.tvdbId!= null)&&this.tvdbId.equals(rhs.tvdbId))))&&((this.ageHours == rhs.ageHours)||((this.ageHours!= null)&&this.ageHours.equals(rhs.ageHours))))&&((this.downloadUrl == rhs.downloadUrl)||((this.downloadUrl!= null)&&this.downloadUrl.equals(rhs.downloadUrl))))&&((this.torrentInfoHash == rhs.torrentInfoHash)||((this.torrentInfoHash!= null)&&this.torrentInfoHash.equals(rhs.torrentInfoHash))))&&((this.importedPath == rhs.importedPath)||((this.importedPath!= null)&&this.importedPath.equals(rhs.importedPath))))&&((this.indexer == rhs.indexer)||((this.indexer!= null)&&this.indexer.equals(rhs.indexer))))&&((this.droppedPath == rhs.droppedPath)||((this.droppedPath!= null)&&this.droppedPath.equals(rhs.droppedPath))))&&((this.protocol == rhs.protocol)||((this.protocol!= null)&&this.protocol.equals(rhs.protocol))))&&((this.size == rhs.size)||((this.size!= null)&&this.size.equals(rhs.size))))&&((this.tvRageId == rhs.tvRageId)||((this.tvRageId!= null)&&this.tvRageId.equals(rhs.tvRageId))))&&((this.preferredWordScore == rhs.preferredWordScore)||((this.preferredWordScore!= null)&&this.preferredWordScore.equals(rhs.preferredWordScore))))&&((this.guid == rhs.guid)||((this.guid!= null)&&this.guid.equals(rhs.guid))))&&((this.downloadClientName == rhs.downloadClientName)||((this.downloadClientName!= null)&&this.downloadClientName.equals(rhs.downloadClientName))))&&((this.publishedDate == rhs.publishedDate)||((this.publishedDate!= null)&&this.publishedDate.equals(rhs.publishedDate))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.age == rhs.age)||((this.age!= null)&&this.age.equals(rhs.age))))&&((this.releaseGroup == rhs.releaseGroup)||((this.releaseGroup!= null)&&this.releaseGroup.equals(rhs.releaseGroup))))&&((this.downloadClient == rhs.downloadClient)||((this.downloadClient!= null)&&this.downloadClient.equals(rhs.downloadClient))))&&((this.fileId == rhs.fileId)||((this.fileId!= null)&&this.fileId.equals(rhs.fileId))));
    }

}

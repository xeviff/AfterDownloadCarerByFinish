
package cat.hack3.mangrana.sonarr.api.schema.series;

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
    "title",
    "alternateTitles",
    "sortTitle",
    "status",
    "ended",
    "overview",
    "previousAiring",
    "network",
    "airTime",
    "images",
    "seasons",
    "year",
    "path",
    "qualityProfileId",
    "languageProfileId",
    "seasonFolder",
    "monitored",
    "useSceneNumbering",
    "runtime",
    "tvdbId",
    "tvRageId",
    "tvMazeId",
    "firstAired",
    "seriesType",
    "cleanTitle",
    "imdbId",
    "titleSlug",
    "rootFolderPath",
    "genres",
    "tags",
    "added",
    "ratings",
    "statistics",
    "id"
})
@Generated("jsonschema2pojo")
public class SonarrSerie {

    @JsonProperty("title")
    private String title;
    @JsonProperty("alternateTitles")
    private List<Object> alternateTitles = new ArrayList<Object>();
    @JsonProperty("sortTitle")
    private String sortTitle;
    @JsonProperty("status")
    private String status;
    @JsonProperty("ended")
    private Boolean ended;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("previousAiring")
    private String previousAiring;
    @JsonProperty("network")
    private String network;
    @JsonProperty("airTime")
    private String airTime;
    @JsonProperty("images")
    private List<Image> images = new ArrayList<Image>();
    @JsonProperty("seasons")
    private List<Season> seasons = new ArrayList<Season>();
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("path")
    private String path;
    @JsonProperty("qualityProfileId")
    private Integer qualityProfileId;
    @JsonProperty("languageProfileId")
    private Integer languageProfileId;
    @JsonProperty("seasonFolder")
    private Boolean seasonFolder;
    @JsonProperty("monitored")
    private Boolean monitored;
    @JsonProperty("useSceneNumbering")
    private Boolean useSceneNumbering;
    @JsonProperty("runtime")
    private Integer runtime;
    @JsonProperty("tvdbId")
    private Integer tvdbId;
    @JsonProperty("tvRageId")
    private Integer tvRageId;
    @JsonProperty("tvMazeId")
    private Integer tvMazeId;
    @JsonProperty("firstAired")
    private String firstAired;
    @JsonProperty("seriesType")
    private String seriesType;
    @JsonProperty("cleanTitle")
    private String cleanTitle;
    @JsonProperty("imdbId")
    private String imdbId;
    @JsonProperty("titleSlug")
    private String titleSlug;
    @JsonProperty("rootFolderPath")
    private String rootFolderPath;
    @JsonProperty("genres")
    private List<String> genres = new ArrayList<String>();
    @JsonProperty("tags")
    private List<Object> tags = new ArrayList<Object>();
    @JsonProperty("added")
    private String added;
    @JsonProperty("ratings")
    private Ratings ratings;
    @JsonProperty("statistics")
    private Statistics__1 statistics;
    @JsonProperty("id")
    private Integer id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public SonarrSerie withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("alternateTitles")
    public List<Object> getAlternateTitles() {
        return alternateTitles;
    }

    @JsonProperty("alternateTitles")
    public void setAlternateTitles(List<Object> alternateTitles) {
        this.alternateTitles = alternateTitles;
    }

    public SonarrSerie withAlternateTitles(List<Object> alternateTitles) {
        this.alternateTitles = alternateTitles;
        return this;
    }

    @JsonProperty("sortTitle")
    public String getSortTitle() {
        return sortTitle;
    }

    @JsonProperty("sortTitle")
    public void setSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
    }

    public SonarrSerie withSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
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

    public SonarrSerie withStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("ended")
    public Boolean getEnded() {
        return ended;
    }

    @JsonProperty("ended")
    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    public SonarrSerie withEnded(Boolean ended) {
        this.ended = ended;
        return this;
    }

    @JsonProperty("overview")
    public String getOverview() {
        return overview;
    }

    @JsonProperty("overview")
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public SonarrSerie withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    @JsonProperty("previousAiring")
    public String getPreviousAiring() {
        return previousAiring;
    }

    @JsonProperty("previousAiring")
    public void setPreviousAiring(String previousAiring) {
        this.previousAiring = previousAiring;
    }

    public SonarrSerie withPreviousAiring(String previousAiring) {
        this.previousAiring = previousAiring;
        return this;
    }

    @JsonProperty("network")
    public String getNetwork() {
        return network;
    }

    @JsonProperty("network")
    public void setNetwork(String network) {
        this.network = network;
    }

    public SonarrSerie withNetwork(String network) {
        this.network = network;
        return this;
    }

    @JsonProperty("airTime")
    public String getAirTime() {
        return airTime;
    }

    @JsonProperty("airTime")
    public void setAirTime(String airTime) {
        this.airTime = airTime;
    }

    public SonarrSerie withAirTime(String airTime) {
        this.airTime = airTime;
        return this;
    }

    @JsonProperty("images")
    public List<Image> getImages() {
        return images;
    }

    @JsonProperty("images")
    public void setImages(List<Image> images) {
        this.images = images;
    }

    public SonarrSerie withImages(List<Image> images) {
        this.images = images;
        return this;
    }

    @JsonProperty("seasons")
    public List<Season> getSeasons() {
        return seasons;
    }

    @JsonProperty("seasons")
    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public SonarrSerie withSeasons(List<Season> seasons) {
        this.seasons = seasons;
        return this;
    }

    @JsonProperty("year")
    public Integer getYear() {
        return year;
    }

    @JsonProperty("year")
    public void setYear(Integer year) {
        this.year = year;
    }

    public SonarrSerie withYear(Integer year) {
        this.year = year;
        return this;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    public SonarrSerie withPath(String path) {
        this.path = path;
        return this;
    }

    @JsonProperty("qualityProfileId")
    public Integer getQualityProfileId() {
        return qualityProfileId;
    }

    @JsonProperty("qualityProfileId")
    public void setQualityProfileId(Integer qualityProfileId) {
        this.qualityProfileId = qualityProfileId;
    }

    public SonarrSerie withQualityProfileId(Integer qualityProfileId) {
        this.qualityProfileId = qualityProfileId;
        return this;
    }

    @JsonProperty("languageProfileId")
    public Integer getLanguageProfileId() {
        return languageProfileId;
    }

    @JsonProperty("languageProfileId")
    public void setLanguageProfileId(Integer languageProfileId) {
        this.languageProfileId = languageProfileId;
    }

    public SonarrSerie withLanguageProfileId(Integer languageProfileId) {
        this.languageProfileId = languageProfileId;
        return this;
    }

    @JsonProperty("seasonFolder")
    public Boolean getSeasonFolder() {
        return seasonFolder;
    }

    @JsonProperty("seasonFolder")
    public void setSeasonFolder(Boolean seasonFolder) {
        this.seasonFolder = seasonFolder;
    }

    public SonarrSerie withSeasonFolder(Boolean seasonFolder) {
        this.seasonFolder = seasonFolder;
        return this;
    }

    @JsonProperty("monitored")
    public Boolean getMonitored() {
        return monitored;
    }

    @JsonProperty("monitored")
    public void setMonitored(Boolean monitored) {
        this.monitored = monitored;
    }

    public SonarrSerie withMonitored(Boolean monitored) {
        this.monitored = monitored;
        return this;
    }

    @JsonProperty("useSceneNumbering")
    public Boolean getUseSceneNumbering() {
        return useSceneNumbering;
    }

    @JsonProperty("useSceneNumbering")
    public void setUseSceneNumbering(Boolean useSceneNumbering) {
        this.useSceneNumbering = useSceneNumbering;
    }

    public SonarrSerie withUseSceneNumbering(Boolean useSceneNumbering) {
        this.useSceneNumbering = useSceneNumbering;
        return this;
    }

    @JsonProperty("runtime")
    public Integer getRuntime() {
        return runtime;
    }

    @JsonProperty("runtime")
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public SonarrSerie withRuntime(Integer runtime) {
        this.runtime = runtime;
        return this;
    }

    @JsonProperty("tvdbId")
    public Integer getTvdbId() {
        return tvdbId;
    }

    @JsonProperty("tvdbId")
    public void setTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
    }

    public SonarrSerie withTvdbId(Integer tvdbId) {
        this.tvdbId = tvdbId;
        return this;
    }

    @JsonProperty("tvRageId")
    public Integer getTvRageId() {
        return tvRageId;
    }

    @JsonProperty("tvRageId")
    public void setTvRageId(Integer tvRageId) {
        this.tvRageId = tvRageId;
    }

    public SonarrSerie withTvRageId(Integer tvRageId) {
        this.tvRageId = tvRageId;
        return this;
    }

    @JsonProperty("tvMazeId")
    public Integer getTvMazeId() {
        return tvMazeId;
    }

    @JsonProperty("tvMazeId")
    public void setTvMazeId(Integer tvMazeId) {
        this.tvMazeId = tvMazeId;
    }

    public SonarrSerie withTvMazeId(Integer tvMazeId) {
        this.tvMazeId = tvMazeId;
        return this;
    }

    @JsonProperty("firstAired")
    public String getFirstAired() {
        return firstAired;
    }

    @JsonProperty("firstAired")
    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public SonarrSerie withFirstAired(String firstAired) {
        this.firstAired = firstAired;
        return this;
    }

    @JsonProperty("seriesType")
    public String getSeriesType() {
        return seriesType;
    }

    @JsonProperty("seriesType")
    public void setSeriesType(String seriesType) {
        this.seriesType = seriesType;
    }

    public SonarrSerie withSeriesType(String seriesType) {
        this.seriesType = seriesType;
        return this;
    }

    @JsonProperty("cleanTitle")
    public String getCleanTitle() {
        return cleanTitle;
    }

    @JsonProperty("cleanTitle")
    public void setCleanTitle(String cleanTitle) {
        this.cleanTitle = cleanTitle;
    }

    public SonarrSerie withCleanTitle(String cleanTitle) {
        this.cleanTitle = cleanTitle;
        return this;
    }

    @JsonProperty("imdbId")
    public String getImdbId() {
        return imdbId;
    }

    @JsonProperty("imdbId")
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public SonarrSerie withImdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    @JsonProperty("titleSlug")
    public String getTitleSlug() {
        return titleSlug;
    }

    @JsonProperty("titleSlug")
    public void setTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
    }

    public SonarrSerie withTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
        return this;
    }

    @JsonProperty("rootFolderPath")
    public String getRootFolderPath() {
        return rootFolderPath;
    }

    @JsonProperty("rootFolderPath")
    public void setRootFolderPath(String rootFolderPath) {
        this.rootFolderPath = rootFolderPath;
    }

    public SonarrSerie withRootFolderPath(String rootFolderPath) {
        this.rootFolderPath = rootFolderPath;
        return this;
    }

    @JsonProperty("genres")
    public List<String> getGenres() {
        return genres;
    }

    @JsonProperty("genres")
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public SonarrSerie withGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    @JsonProperty("tags")
    public List<Object> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public SonarrSerie withTags(List<Object> tags) {
        this.tags = tags;
        return this;
    }

    @JsonProperty("added")
    public String getAdded() {
        return added;
    }

    @JsonProperty("added")
    public void setAdded(String added) {
        this.added = added;
    }

    public SonarrSerie withAdded(String added) {
        this.added = added;
        return this;
    }

    @JsonProperty("ratings")
    public Ratings getRatings() {
        return ratings;
    }

    @JsonProperty("ratings")
    public void setRatings(Ratings ratings) {
        this.ratings = ratings;
    }

    public SonarrSerie withRatings(Ratings ratings) {
        this.ratings = ratings;
        return this;
    }

    @JsonProperty("statistics")
    public Statistics__1 getStatistics() {
        return statistics;
    }

    @JsonProperty("statistics")
    public void setStatistics(Statistics__1 statistics) {
        this.statistics = statistics;
    }

    public SonarrSerie withStatistics(Statistics__1 statistics) {
        this.statistics = statistics;
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

    public SonarrSerie withId(Integer id) {
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

    public SonarrSerie withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SonarrSerie.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("alternateTitles");
        sb.append('=');
        sb.append(((this.alternateTitles == null)?"<null>":this.alternateTitles));
        sb.append(',');
        sb.append("sortTitle");
        sb.append('=');
        sb.append(((this.sortTitle == null)?"<null>":this.sortTitle));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("ended");
        sb.append('=');
        sb.append(((this.ended == null)?"<null>":this.ended));
        sb.append(',');
        sb.append("overview");
        sb.append('=');
        sb.append(((this.overview == null)?"<null>":this.overview));
        sb.append(',');
        sb.append("previousAiring");
        sb.append('=');
        sb.append(((this.previousAiring == null)?"<null>":this.previousAiring));
        sb.append(',');
        sb.append("network");
        sb.append('=');
        sb.append(((this.network == null)?"<null>":this.network));
        sb.append(',');
        sb.append("airTime");
        sb.append('=');
        sb.append(((this.airTime == null)?"<null>":this.airTime));
        sb.append(',');
        sb.append("images");
        sb.append('=');
        sb.append(((this.images == null)?"<null>":this.images));
        sb.append(',');
        sb.append("seasons");
        sb.append('=');
        sb.append(((this.seasons == null)?"<null>":this.seasons));
        sb.append(',');
        sb.append("year");
        sb.append('=');
        sb.append(((this.year == null)?"<null>":this.year));
        sb.append(',');
        sb.append("path");
        sb.append('=');
        sb.append(((this.path == null)?"<null>":this.path));
        sb.append(',');
        sb.append("qualityProfileId");
        sb.append('=');
        sb.append(((this.qualityProfileId == null)?"<null>":this.qualityProfileId));
        sb.append(',');
        sb.append("languageProfileId");
        sb.append('=');
        sb.append(((this.languageProfileId == null)?"<null>":this.languageProfileId));
        sb.append(',');
        sb.append("seasonFolder");
        sb.append('=');
        sb.append(((this.seasonFolder == null)?"<null>":this.seasonFolder));
        sb.append(',');
        sb.append("monitored");
        sb.append('=');
        sb.append(((this.monitored == null)?"<null>":this.monitored));
        sb.append(',');
        sb.append("useSceneNumbering");
        sb.append('=');
        sb.append(((this.useSceneNumbering == null)?"<null>":this.useSceneNumbering));
        sb.append(',');
        sb.append("runtime");
        sb.append('=');
        sb.append(((this.runtime == null)?"<null>":this.runtime));
        sb.append(',');
        sb.append("tvdbId");
        sb.append('=');
        sb.append(((this.tvdbId == null)?"<null>":this.tvdbId));
        sb.append(',');
        sb.append("tvRageId");
        sb.append('=');
        sb.append(((this.tvRageId == null)?"<null>":this.tvRageId));
        sb.append(',');
        sb.append("tvMazeId");
        sb.append('=');
        sb.append(((this.tvMazeId == null)?"<null>":this.tvMazeId));
        sb.append(',');
        sb.append("firstAired");
        sb.append('=');
        sb.append(((this.firstAired == null)?"<null>":this.firstAired));
        sb.append(',');
        sb.append("seriesType");
        sb.append('=');
        sb.append(((this.seriesType == null)?"<null>":this.seriesType));
        sb.append(',');
        sb.append("cleanTitle");
        sb.append('=');
        sb.append(((this.cleanTitle == null)?"<null>":this.cleanTitle));
        sb.append(',');
        sb.append("imdbId");
        sb.append('=');
        sb.append(((this.imdbId == null)?"<null>":this.imdbId));
        sb.append(',');
        sb.append("titleSlug");
        sb.append('=');
        sb.append(((this.titleSlug == null)?"<null>":this.titleSlug));
        sb.append(',');
        sb.append("rootFolderPath");
        sb.append('=');
        sb.append(((this.rootFolderPath == null)?"<null>":this.rootFolderPath));
        sb.append(',');
        sb.append("genres");
        sb.append('=');
        sb.append(((this.genres == null)?"<null>":this.genres));
        sb.append(',');
        sb.append("tags");
        sb.append('=');
        sb.append(((this.tags == null)?"<null>":this.tags));
        sb.append(',');
        sb.append("added");
        sb.append('=');
        sb.append(((this.added == null)?"<null>":this.added));
        sb.append(',');
        sb.append("ratings");
        sb.append('=');
        sb.append(((this.ratings == null)?"<null>":this.ratings));
        sb.append(',');
        sb.append("statistics");
        sb.append('=');
        sb.append(((this.statistics == null)?"<null>":this.statistics));
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
        result = ((result* 31)+((this.tvMazeId == null)? 0 :this.tvMazeId.hashCode()));
        result = ((result* 31)+((this.previousAiring == null)? 0 :this.previousAiring.hashCode()));
        result = ((result* 31)+((this.alternateTitles == null)? 0 :this.alternateTitles.hashCode()));
        result = ((result* 31)+((this.year == null)? 0 :this.year.hashCode()));
        result = ((result* 31)+((this.rootFolderPath == null)? 0 :this.rootFolderPath.hashCode()));
        result = ((result* 31)+((this.added == null)? 0 :this.added.hashCode()));
        result = ((result* 31)+((this.imdbId == null)? 0 :this.imdbId.hashCode()));
        result = ((result* 31)+((this.languageProfileId == null)? 0 :this.languageProfileId.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.network == null)? 0 :this.network.hashCode()));
        result = ((result* 31)+((this.monitored == null)? 0 :this.monitored.hashCode()));
        result = ((result* 31)+((this.cleanTitle == null)? 0 :this.cleanTitle.hashCode()));
        result = ((result* 31)+((this.path == null)? 0 :this.path.hashCode()));
        result = ((result* 31)+((this.titleSlug == null)? 0 :this.titleSlug.hashCode()));
        result = ((result* 31)+((this.qualityProfileId == null)? 0 :this.qualityProfileId.hashCode()));
        result = ((result* 31)+((this.seriesType == null)? 0 :this.seriesType.hashCode()));
        result = ((result* 31)+((this.genres == null)? 0 :this.genres.hashCode()));
        result = ((result* 31)+((this.ratings == null)? 0 :this.ratings.hashCode()));
        result = ((result* 31)+((this.useSceneNumbering == null)? 0 :this.useSceneNumbering.hashCode()));
        result = ((result* 31)+((this.seasonFolder == null)? 0 :this.seasonFolder.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.airTime == null)? 0 :this.airTime.hashCode()));
        result = ((result* 31)+((this.overview == null)? 0 :this.overview.hashCode()));
        result = ((result* 31)+((this.images == null)? 0 :this.images.hashCode()));
        result = ((result* 31)+((this.seasons == null)? 0 :this.seasons.hashCode()));
        result = ((result* 31)+((this.tvdbId == null)? 0 :this.tvdbId.hashCode()));
        result = ((result* 31)+((this.firstAired == null)? 0 :this.firstAired.hashCode()));
        result = ((result* 31)+((this.runtime == null)? 0 :this.runtime.hashCode()));
        result = ((result* 31)+((this.tags == null)? 0 :this.tags.hashCode()));
        result = ((result* 31)+((this.sortTitle == null)? 0 :this.sortTitle.hashCode()));
        result = ((result* 31)+((this.tvRageId == null)? 0 :this.tvRageId.hashCode()));
        result = ((result* 31)+((this.ended == null)? 0 :this.ended.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        result = ((result* 31)+((this.statistics == null)? 0 :this.statistics.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SonarrSerie) == false) {
            return false;
        }
        SonarrSerie rhs = ((SonarrSerie) other);
        return ((((((((((((((((((((((((((((((((((((this.tvMazeId == rhs.tvMazeId)||((this.tvMazeId!= null)&&this.tvMazeId.equals(rhs.tvMazeId)))&&((this.previousAiring == rhs.previousAiring)||((this.previousAiring!= null)&&this.previousAiring.equals(rhs.previousAiring))))&&((this.alternateTitles == rhs.alternateTitles)||((this.alternateTitles!= null)&&this.alternateTitles.equals(rhs.alternateTitles))))&&((this.year == rhs.year)||((this.year!= null)&&this.year.equals(rhs.year))))&&((this.rootFolderPath == rhs.rootFolderPath)||((this.rootFolderPath!= null)&&this.rootFolderPath.equals(rhs.rootFolderPath))))&&((this.added == rhs.added)||((this.added!= null)&&this.added.equals(rhs.added))))&&((this.imdbId == rhs.imdbId)||((this.imdbId!= null)&&this.imdbId.equals(rhs.imdbId))))&&((this.languageProfileId == rhs.languageProfileId)||((this.languageProfileId!= null)&&this.languageProfileId.equals(rhs.languageProfileId))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.network == rhs.network)||((this.network!= null)&&this.network.equals(rhs.network))))&&((this.monitored == rhs.monitored)||((this.monitored!= null)&&this.monitored.equals(rhs.monitored))))&&((this.cleanTitle == rhs.cleanTitle)||((this.cleanTitle!= null)&&this.cleanTitle.equals(rhs.cleanTitle))))&&((this.path == rhs.path)||((this.path!= null)&&this.path.equals(rhs.path))))&&((this.titleSlug == rhs.titleSlug)||((this.titleSlug!= null)&&this.titleSlug.equals(rhs.titleSlug))))&&((this.qualityProfileId == rhs.qualityProfileId)||((this.qualityProfileId!= null)&&this.qualityProfileId.equals(rhs.qualityProfileId))))&&((this.seriesType == rhs.seriesType)||((this.seriesType!= null)&&this.seriesType.equals(rhs.seriesType))))&&((this.genres == rhs.genres)||((this.genres!= null)&&this.genres.equals(rhs.genres))))&&((this.ratings == rhs.ratings)||((this.ratings!= null)&&this.ratings.equals(rhs.ratings))))&&((this.useSceneNumbering == rhs.useSceneNumbering)||((this.useSceneNumbering!= null)&&this.useSceneNumbering.equals(rhs.useSceneNumbering))))&&((this.seasonFolder == rhs.seasonFolder)||((this.seasonFolder!= null)&&this.seasonFolder.equals(rhs.seasonFolder))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.airTime == rhs.airTime)||((this.airTime!= null)&&this.airTime.equals(rhs.airTime))))&&((this.overview == rhs.overview)||((this.overview!= null)&&this.overview.equals(rhs.overview))))&&((this.images == rhs.images)||((this.images!= null)&&this.images.equals(rhs.images))))&&((this.seasons == rhs.seasons)||((this.seasons!= null)&&this.seasons.equals(rhs.seasons))))&&((this.tvdbId == rhs.tvdbId)||((this.tvdbId!= null)&&this.tvdbId.equals(rhs.tvdbId))))&&((this.firstAired == rhs.firstAired)||((this.firstAired!= null)&&this.firstAired.equals(rhs.firstAired))))&&((this.runtime == rhs.runtime)||((this.runtime!= null)&&this.runtime.equals(rhs.runtime))))&&((this.tags == rhs.tags)||((this.tags!= null)&&this.tags.equals(rhs.tags))))&&((this.sortTitle == rhs.sortTitle)||((this.sortTitle!= null)&&this.sortTitle.equals(rhs.sortTitle))))&&((this.tvRageId == rhs.tvRageId)||((this.tvRageId!= null)&&this.tvRageId.equals(rhs.tvRageId))))&&((this.ended == rhs.ended)||((this.ended!= null)&&this.ended.equals(rhs.ended))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))))&&((this.statistics == rhs.statistics)||((this.statistics!= null)&&this.statistics.equals(rhs.statistics))));
    }

}

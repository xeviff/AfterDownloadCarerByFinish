
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
    "title",
    "originalTitle",
    "originalLanguage",
    "alternateTitles",
    "secondaryYearSourceId",
    "sortTitle",
    "sizeOnDisk",
    "status",
    "overview",
    "inCinemas",
    "physicalRelease",
    "digitalRelease",
    "images",
    "website",
    "year",
    "hasFile",
    "youTubeTrailerId",
    "studio",
    "path",
    "qualityProfileId",
    "monitored",
    "minimumAvailability",
    "isAvailable",
    "folderName",
    "runtime",
    "cleanTitle",
    "imdbId",
    "tmdbId",
    "titleSlug",
    "certification",
    "genres",
    "tags",
    "added",
    "ratings",
    "id",
    "collection"
})
@Generated("jsonschema2pojo")
public class Movie {

    @JsonProperty("title")
    private String title;
    @JsonProperty("originalTitle")
    private String originalTitle;
    @JsonProperty("originalLanguage")
    private OriginalLanguage originalLanguage;
    @JsonProperty("alternateTitles")
    private List<AlternateTitle> alternateTitles = new ArrayList<AlternateTitle>();
    @JsonProperty("secondaryYearSourceId")
    private Integer secondaryYearSourceId;
    @JsonProperty("sortTitle")
    private String sortTitle;
    @JsonProperty("sizeOnDisk")
    private Long sizeOnDisk;
    @JsonProperty("status")
    private String status;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("inCinemas")
    private String inCinemas;
    @JsonProperty("physicalRelease")
    private String physicalRelease;
    @JsonProperty("digitalRelease")
    private String digitalRelease;
    @JsonProperty("images")
    private List<Image> images = new ArrayList<Image>();
    @JsonProperty("website")
    private String website;
    @JsonProperty("year")
    private Integer year;
    @JsonProperty("hasFile")
    private Boolean hasFile;
    @JsonProperty("youTubeTrailerId")
    private String youTubeTrailerId;
    @JsonProperty("studio")
    private String studio;
    @JsonProperty("path")
    private String path;
    @JsonProperty("qualityProfileId")
    private Integer qualityProfileId;
    @JsonProperty("monitored")
    private Boolean monitored;
    @JsonProperty("minimumAvailability")
    private String minimumAvailability;
    @JsonProperty("isAvailable")
    private Boolean isAvailable;
    @JsonProperty("folderName")
    private String folderName;
    @JsonProperty("runtime")
    private Integer runtime;
    @JsonProperty("cleanTitle")
    private String cleanTitle;
    @JsonProperty("imdbId")
    private String imdbId;
    @JsonProperty("tmdbId")
    private Integer tmdbId;
    @JsonProperty("titleSlug")
    private String titleSlug;
    @JsonProperty("certification")
    private String certification;
    @JsonProperty("genres")
    private List<String> genres = new ArrayList<String>();
    @JsonProperty("tags")
    private List<Object> tags = new ArrayList<Object>();
    @JsonProperty("added")
    private String added;
    @JsonProperty("ratings")
    private Ratings ratings;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("collection")
    private Collection collection;
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

    public Movie withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("originalTitle")
    public String getOriginalTitle() {
        return originalTitle;
    }

    @JsonProperty("originalTitle")
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Movie withOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    @JsonProperty("originalLanguage")
    public OriginalLanguage getOriginalLanguage() {
        return originalLanguage;
    }

    @JsonProperty("originalLanguage")
    public void setOriginalLanguage(OriginalLanguage originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public Movie withOriginalLanguage(OriginalLanguage originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    @JsonProperty("alternateTitles")
    public List<AlternateTitle> getAlternateTitles() {
        return alternateTitles;
    }

    @JsonProperty("alternateTitles")
    public void setAlternateTitles(List<AlternateTitle> alternateTitles) {
        this.alternateTitles = alternateTitles;
    }

    public Movie withAlternateTitles(List<AlternateTitle> alternateTitles) {
        this.alternateTitles = alternateTitles;
        return this;
    }

    @JsonProperty("secondaryYearSourceId")
    public Integer getSecondaryYearSourceId() {
        return secondaryYearSourceId;
    }

    @JsonProperty("secondaryYearSourceId")
    public void setSecondaryYearSourceId(Integer secondaryYearSourceId) {
        this.secondaryYearSourceId = secondaryYearSourceId;
    }

    public Movie withSecondaryYearSourceId(Integer secondaryYearSourceId) {
        this.secondaryYearSourceId = secondaryYearSourceId;
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

    public Movie withSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
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

    public Movie withSizeOnDisk(Long sizeOnDisk) {
        this.sizeOnDisk = sizeOnDisk;
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

    public Movie withStatus(String status) {
        this.status = status;
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

    public Movie withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    @JsonProperty("inCinemas")
    public String getInCinemas() {
        return inCinemas;
    }

    @JsonProperty("inCinemas")
    public void setInCinemas(String inCinemas) {
        this.inCinemas = inCinemas;
    }

    public Movie withInCinemas(String inCinemas) {
        this.inCinemas = inCinemas;
        return this;
    }

    @JsonProperty("physicalRelease")
    public String getPhysicalRelease() {
        return physicalRelease;
    }

    @JsonProperty("physicalRelease")
    public void setPhysicalRelease(String physicalRelease) {
        this.physicalRelease = physicalRelease;
    }

    public Movie withPhysicalRelease(String physicalRelease) {
        this.physicalRelease = physicalRelease;
        return this;
    }

    @JsonProperty("digitalRelease")
    public String getDigitalRelease() {
        return digitalRelease;
    }

    @JsonProperty("digitalRelease")
    public void setDigitalRelease(String digitalRelease) {
        this.digitalRelease = digitalRelease;
    }

    public Movie withDigitalRelease(String digitalRelease) {
        this.digitalRelease = digitalRelease;
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

    public Movie withImages(List<Image> images) {
        this.images = images;
        return this;
    }

    @JsonProperty("website")
    public String getWebsite() {
        return website;
    }

    @JsonProperty("website")
    public void setWebsite(String website) {
        this.website = website;
    }

    public Movie withWebsite(String website) {
        this.website = website;
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

    public Movie withYear(Integer year) {
        this.year = year;
        return this;
    }

    @JsonProperty("hasFile")
    public Boolean getHasFile() {
        return hasFile;
    }

    @JsonProperty("hasFile")
    public void setHasFile(Boolean hasFile) {
        this.hasFile = hasFile;
    }

    public Movie withHasFile(Boolean hasFile) {
        this.hasFile = hasFile;
        return this;
    }

    @JsonProperty("youTubeTrailerId")
    public String getYouTubeTrailerId() {
        return youTubeTrailerId;
    }

    @JsonProperty("youTubeTrailerId")
    public void setYouTubeTrailerId(String youTubeTrailerId) {
        this.youTubeTrailerId = youTubeTrailerId;
    }

    public Movie withYouTubeTrailerId(String youTubeTrailerId) {
        this.youTubeTrailerId = youTubeTrailerId;
        return this;
    }

    @JsonProperty("studio")
    public String getStudio() {
        return studio;
    }

    @JsonProperty("studio")
    public void setStudio(String studio) {
        this.studio = studio;
    }

    public Movie withStudio(String studio) {
        this.studio = studio;
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

    public Movie withPath(String path) {
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

    public Movie withQualityProfileId(Integer qualityProfileId) {
        this.qualityProfileId = qualityProfileId;
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

    public Movie withMonitored(Boolean monitored) {
        this.monitored = monitored;
        return this;
    }

    @JsonProperty("minimumAvailability")
    public String getMinimumAvailability() {
        return minimumAvailability;
    }

    @JsonProperty("minimumAvailability")
    public void setMinimumAvailability(String minimumAvailability) {
        this.minimumAvailability = minimumAvailability;
    }

    public Movie withMinimumAvailability(String minimumAvailability) {
        this.minimumAvailability = minimumAvailability;
        return this;
    }

    @JsonProperty("isAvailable")
    public Boolean getIsAvailable() {
        return isAvailable;
    }

    @JsonProperty("isAvailable")
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Movie withIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
        return this;
    }

    @JsonProperty("folderName")
    public String getFolderName() {
        return folderName;
    }

    @JsonProperty("folderName")
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Movie withFolderName(String folderName) {
        this.folderName = folderName;
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

    public Movie withRuntime(Integer runtime) {
        this.runtime = runtime;
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

    public Movie withCleanTitle(String cleanTitle) {
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

    public Movie withImdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    @JsonProperty("tmdbId")
    public Integer getTmdbId() {
        return tmdbId;
    }

    @JsonProperty("tmdbId")
    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    public Movie withTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
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

    public Movie withTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
        return this;
    }

    @JsonProperty("certification")
    public String getCertification() {
        return certification;
    }

    @JsonProperty("certification")
    public void setCertification(String certification) {
        this.certification = certification;
    }

    public Movie withCertification(String certification) {
        this.certification = certification;
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

    public Movie withGenres(List<String> genres) {
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

    public Movie withTags(List<Object> tags) {
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

    public Movie withAdded(String added) {
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

    public Movie withRatings(Ratings ratings) {
        this.ratings = ratings;
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

    public Movie withId(Integer id) {
        this.id = id;
        return this;
    }

    @JsonProperty("collection")
    public Collection getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Movie withCollection(Collection collection) {
        this.collection = collection;
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

    public Movie withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Movie.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("originalTitle");
        sb.append('=');
        sb.append(((this.originalTitle == null)?"<null>":this.originalTitle));
        sb.append(',');
        sb.append("originalLanguage");
        sb.append('=');
        sb.append(((this.originalLanguage == null)?"<null>":this.originalLanguage));
        sb.append(',');
        sb.append("alternateTitles");
        sb.append('=');
        sb.append(((this.alternateTitles == null)?"<null>":this.alternateTitles));
        sb.append(',');
        sb.append("secondaryYearSourceId");
        sb.append('=');
        sb.append(((this.secondaryYearSourceId == null)?"<null>":this.secondaryYearSourceId));
        sb.append(',');
        sb.append("sortTitle");
        sb.append('=');
        sb.append(((this.sortTitle == null)?"<null>":this.sortTitle));
        sb.append(',');
        sb.append("sizeOnDisk");
        sb.append('=');
        sb.append(((this.sizeOnDisk == null)?"<null>":this.sizeOnDisk));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("overview");
        sb.append('=');
        sb.append(((this.overview == null)?"<null>":this.overview));
        sb.append(',');
        sb.append("inCinemas");
        sb.append('=');
        sb.append(((this.inCinemas == null)?"<null>":this.inCinemas));
        sb.append(',');
        sb.append("physicalRelease");
        sb.append('=');
        sb.append(((this.physicalRelease == null)?"<null>":this.physicalRelease));
        sb.append(',');
        sb.append("digitalRelease");
        sb.append('=');
        sb.append(((this.digitalRelease == null)?"<null>":this.digitalRelease));
        sb.append(',');
        sb.append("images");
        sb.append('=');
        sb.append(((this.images == null)?"<null>":this.images));
        sb.append(',');
        sb.append("website");
        sb.append('=');
        sb.append(((this.website == null)?"<null>":this.website));
        sb.append(',');
        sb.append("year");
        sb.append('=');
        sb.append(((this.year == null)?"<null>":this.year));
        sb.append(',');
        sb.append("hasFile");
        sb.append('=');
        sb.append(((this.hasFile == null)?"<null>":this.hasFile));
        sb.append(',');
        sb.append("youTubeTrailerId");
        sb.append('=');
        sb.append(((this.youTubeTrailerId == null)?"<null>":this.youTubeTrailerId));
        sb.append(',');
        sb.append("studio");
        sb.append('=');
        sb.append(((this.studio == null)?"<null>":this.studio));
        sb.append(',');
        sb.append("path");
        sb.append('=');
        sb.append(((this.path == null)?"<null>":this.path));
        sb.append(',');
        sb.append("qualityProfileId");
        sb.append('=');
        sb.append(((this.qualityProfileId == null)?"<null>":this.qualityProfileId));
        sb.append(',');
        sb.append("monitored");
        sb.append('=');
        sb.append(((this.monitored == null)?"<null>":this.monitored));
        sb.append(',');
        sb.append("minimumAvailability");
        sb.append('=');
        sb.append(((this.minimumAvailability == null)?"<null>":this.minimumAvailability));
        sb.append(',');
        sb.append("isAvailable");
        sb.append('=');
        sb.append(((this.isAvailable == null)?"<null>":this.isAvailable));
        sb.append(',');
        sb.append("folderName");
        sb.append('=');
        sb.append(((this.folderName == null)?"<null>":this.folderName));
        sb.append(',');
        sb.append("runtime");
        sb.append('=');
        sb.append(((this.runtime == null)?"<null>":this.runtime));
        sb.append(',');
        sb.append("cleanTitle");
        sb.append('=');
        sb.append(((this.cleanTitle == null)?"<null>":this.cleanTitle));
        sb.append(',');
        sb.append("imdbId");
        sb.append('=');
        sb.append(((this.imdbId == null)?"<null>":this.imdbId));
        sb.append(',');
        sb.append("tmdbId");
        sb.append('=');
        sb.append(((this.tmdbId == null)?"<null>":this.tmdbId));
        sb.append(',');
        sb.append("titleSlug");
        sb.append('=');
        sb.append(((this.titleSlug == null)?"<null>":this.titleSlug));
        sb.append(',');
        sb.append("certification");
        sb.append('=');
        sb.append(((this.certification == null)?"<null>":this.certification));
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
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("collection");
        sb.append('=');
        sb.append(((this.collection == null)?"<null>":this.collection));
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
        result = ((result* 31)+((this.studio == null)? 0 :this.studio.hashCode()));
        result = ((result* 31)+((this.isAvailable == null)? 0 :this.isAvailable.hashCode()));
        result = ((result* 31)+((this.inCinemas == null)? 0 :this.inCinemas.hashCode()));
        result = ((result* 31)+((this.alternateTitles == null)? 0 :this.alternateTitles.hashCode()));
        result = ((result* 31)+((this.year == null)? 0 :this.year.hashCode()));
        result = ((result* 31)+((this.added == null)? 0 :this.added.hashCode()));
        result = ((result* 31)+((this.imdbId == null)? 0 :this.imdbId.hashCode()));
        result = ((result* 31)+((this.youTubeTrailerId == null)? 0 :this.youTubeTrailerId.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.sizeOnDisk == null)? 0 :this.sizeOnDisk.hashCode()));
        result = ((result* 31)+((this.monitored == null)? 0 :this.monitored.hashCode()));
        result = ((result* 31)+((this.cleanTitle == null)? 0 :this.cleanTitle.hashCode()));
        result = ((result* 31)+((this.path == null)? 0 :this.path.hashCode()));
        result = ((result* 31)+((this.titleSlug == null)? 0 :this.titleSlug.hashCode()));
        result = ((result* 31)+((this.tmdbId == null)? 0 :this.tmdbId.hashCode()));
        result = ((result* 31)+((this.qualityProfileId == null)? 0 :this.qualityProfileId.hashCode()));
        result = ((result* 31)+((this.genres == null)? 0 :this.genres.hashCode()));
        result = ((result* 31)+((this.ratings == null)? 0 :this.ratings.hashCode()));
        result = ((result* 31)+((this.digitalRelease == null)? 0 :this.digitalRelease.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.physicalRelease == null)? 0 :this.physicalRelease.hashCode()));
        result = ((result* 31)+((this.overview == null)? 0 :this.overview.hashCode()));
        result = ((result* 31)+((this.images == null)? 0 :this.images.hashCode()));
        result = ((result* 31)+((this.website == null)? 0 :this.website.hashCode()));
        result = ((result* 31)+((this.minimumAvailability == null)? 0 :this.minimumAvailability.hashCode()));
        result = ((result* 31)+((this.runtime == null)? 0 :this.runtime.hashCode()));
        result = ((result* 31)+((this.collection == null)? 0 :this.collection.hashCode()));
        result = ((result* 31)+((this.originalLanguage == null)? 0 :this.originalLanguage.hashCode()));
        result = ((result* 31)+((this.certification == null)? 0 :this.certification.hashCode()));
        result = ((result* 31)+((this.tags == null)? 0 :this.tags.hashCode()));
        result = ((result* 31)+((this.sortTitle == null)? 0 :this.sortTitle.hashCode()));
        result = ((result* 31)+((this.hasFile == null)? 0 :this.hasFile.hashCode()));
        result = ((result* 31)+((this.originalTitle == null)? 0 :this.originalTitle.hashCode()));
        result = ((result* 31)+((this.secondaryYearSourceId == null)? 0 :this.secondaryYearSourceId.hashCode()));
        result = ((result* 31)+((this.folderName == null)? 0 :this.folderName.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Movie) == false) {
            return false;
        }
        Movie rhs = ((Movie) other);
        return ((((((((((((((((((((((((((((((((((((((this.studio == rhs.studio)||((this.studio!= null)&&this.studio.equals(rhs.studio)))&&((this.isAvailable == rhs.isAvailable)||((this.isAvailable!= null)&&this.isAvailable.equals(rhs.isAvailable))))&&((this.inCinemas == rhs.inCinemas)||((this.inCinemas!= null)&&this.inCinemas.equals(rhs.inCinemas))))&&((this.alternateTitles == rhs.alternateTitles)||((this.alternateTitles!= null)&&this.alternateTitles.equals(rhs.alternateTitles))))&&((this.year == rhs.year)||((this.year!= null)&&this.year.equals(rhs.year))))&&((this.added == rhs.added)||((this.added!= null)&&this.added.equals(rhs.added))))&&((this.imdbId == rhs.imdbId)||((this.imdbId!= null)&&this.imdbId.equals(rhs.imdbId))))&&((this.youTubeTrailerId == rhs.youTubeTrailerId)||((this.youTubeTrailerId!= null)&&this.youTubeTrailerId.equals(rhs.youTubeTrailerId))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.sizeOnDisk == rhs.sizeOnDisk)||((this.sizeOnDisk!= null)&&this.sizeOnDisk.equals(rhs.sizeOnDisk))))&&((this.monitored == rhs.monitored)||((this.monitored!= null)&&this.monitored.equals(rhs.monitored))))&&((this.cleanTitle == rhs.cleanTitle)||((this.cleanTitle!= null)&&this.cleanTitle.equals(rhs.cleanTitle))))&&((this.path == rhs.path)||((this.path!= null)&&this.path.equals(rhs.path))))&&((this.titleSlug == rhs.titleSlug)||((this.titleSlug!= null)&&this.titleSlug.equals(rhs.titleSlug))))&&((this.tmdbId == rhs.tmdbId)||((this.tmdbId!= null)&&this.tmdbId.equals(rhs.tmdbId))))&&((this.qualityProfileId == rhs.qualityProfileId)||((this.qualityProfileId!= null)&&this.qualityProfileId.equals(rhs.qualityProfileId))))&&((this.genres == rhs.genres)||((this.genres!= null)&&this.genres.equals(rhs.genres))))&&((this.ratings == rhs.ratings)||((this.ratings!= null)&&this.ratings.equals(rhs.ratings))))&&((this.digitalRelease == rhs.digitalRelease)||((this.digitalRelease!= null)&&this.digitalRelease.equals(rhs.digitalRelease))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.physicalRelease == rhs.physicalRelease)||((this.physicalRelease!= null)&&this.physicalRelease.equals(rhs.physicalRelease))))&&((this.overview == rhs.overview)||((this.overview!= null)&&this.overview.equals(rhs.overview))))&&((this.images == rhs.images)||((this.images!= null)&&this.images.equals(rhs.images))))&&((this.website == rhs.website)||((this.website!= null)&&this.website.equals(rhs.website))))&&((this.minimumAvailability == rhs.minimumAvailability)||((this.minimumAvailability!= null)&&this.minimumAvailability.equals(rhs.minimumAvailability))))&&((this.runtime == rhs.runtime)||((this.runtime!= null)&&this.runtime.equals(rhs.runtime))))&&((this.collection == rhs.collection)||((this.collection!= null)&&this.collection.equals(rhs.collection))))&&((this.originalLanguage == rhs.originalLanguage)||((this.originalLanguage!= null)&&this.originalLanguage.equals(rhs.originalLanguage))))&&((this.certification == rhs.certification)||((this.certification!= null)&&this.certification.equals(rhs.certification))))&&((this.tags == rhs.tags)||((this.tags!= null)&&this.tags.equals(rhs.tags))))&&((this.sortTitle == rhs.sortTitle)||((this.sortTitle!= null)&&this.sortTitle.equals(rhs.sortTitle))))&&((this.hasFile == rhs.hasFile)||((this.hasFile!= null)&&this.hasFile.equals(rhs.hasFile))))&&((this.originalTitle == rhs.originalTitle)||((this.originalTitle!= null)&&this.originalTitle.equals(rhs.originalTitle))))&&((this.secondaryYearSourceId == rhs.secondaryYearSourceId)||((this.secondaryYearSourceId!= null)&&this.secondaryYearSourceId.equals(rhs.secondaryYearSourceId))))&&((this.folderName == rhs.folderName)||((this.folderName!= null)&&this.folderName.equals(rhs.folderName))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}

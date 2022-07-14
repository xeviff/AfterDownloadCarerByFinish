
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
    "page",
    "pageSize",
    "sortKey",
    "sortDirection",
    "totalRecords",
    "records"
})
@Generated("jsonschema2pojo")
public class QueueResourcePagingResource {

    @JsonProperty("page")
    private Integer page;
    @JsonProperty("pageSize")
    private Integer pageSize;
    @JsonProperty("sortKey")
    private String sortKey;
    @JsonProperty("sortDirection")
    private String sortDirection;
    @JsonProperty("totalRecords")
    private Integer totalRecords;
    @JsonProperty("records")
    private List<Record> records = new ArrayList<Record>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("page")
    public Integer getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Integer page) {
        this.page = page;
    }

    public QueueResourcePagingResource withPage(Integer page) {
        this.page = page;
        return this;
    }

    @JsonProperty("pageSize")
    public Integer getPageSize() {
        return pageSize;
    }

    @JsonProperty("pageSize")
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public QueueResourcePagingResource withPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @JsonProperty("sortKey")
    public String getSortKey() {
        return sortKey;
    }

    @JsonProperty("sortKey")
    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public QueueResourcePagingResource withSortKey(String sortKey) {
        this.sortKey = sortKey;
        return this;
    }

    @JsonProperty("sortDirection")
    public String getSortDirection() {
        return sortDirection;
    }

    @JsonProperty("sortDirection")
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public QueueResourcePagingResource withSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }

    @JsonProperty("totalRecords")
    public Integer getTotalRecords() {
        return totalRecords;
    }

    @JsonProperty("totalRecords")
    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public QueueResourcePagingResource withTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
        return this;
    }

    @JsonProperty("records")
    public List<Record> getRecords() {
        return records;
    }

    @JsonProperty("records")
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public QueueResourcePagingResource withRecords(List<Record> records) {
        this.records = records;
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

    public QueueResourcePagingResource withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(QueueResourcePagingResource.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("page");
        sb.append('=');
        sb.append(((this.page == null)?"<null>":this.page));
        sb.append(',');
        sb.append("pageSize");
        sb.append('=');
        sb.append(((this.pageSize == null)?"<null>":this.pageSize));
        sb.append(',');
        sb.append("sortKey");
        sb.append('=');
        sb.append(((this.sortKey == null)?"<null>":this.sortKey));
        sb.append(',');
        sb.append("sortDirection");
        sb.append('=');
        sb.append(((this.sortDirection == null)?"<null>":this.sortDirection));
        sb.append(',');
        sb.append("totalRecords");
        sb.append('=');
        sb.append(((this.totalRecords == null)?"<null>":this.totalRecords));
        sb.append(',');
        sb.append("records");
        sb.append('=');
        sb.append(((this.records == null)?"<null>":this.records));
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
        result = ((result* 31)+((this.sortDirection == null)? 0 :this.sortDirection.hashCode()));
        result = ((result* 31)+((this.totalRecords == null)? 0 :this.totalRecords.hashCode()));
        result = ((result* 31)+((this.sortKey == null)? 0 :this.sortKey.hashCode()));
        result = ((result* 31)+((this.records == null)? 0 :this.records.hashCode()));
        result = ((result* 31)+((this.pageSize == null)? 0 :this.pageSize.hashCode()));
        result = ((result* 31)+((this.page == null)? 0 :this.page.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof QueueResourcePagingResource) == false) {
            return false;
        }
        QueueResourcePagingResource rhs = ((QueueResourcePagingResource) other);
        return ((((((((this.sortDirection == rhs.sortDirection)||((this.sortDirection!= null)&&this.sortDirection.equals(rhs.sortDirection)))&&((this.totalRecords == rhs.totalRecords)||((this.totalRecords!= null)&&this.totalRecords.equals(rhs.totalRecords))))&&((this.sortKey == rhs.sortKey)||((this.sortKey!= null)&&this.sortKey.equals(rhs.sortKey))))&&((this.records == rhs.records)||((this.records!= null)&&this.records.equals(rhs.records))))&&((this.pageSize == rhs.pageSize)||((this.pageSize!= null)&&this.pageSize.equals(rhs.pageSize))))&&((this.page == rhs.page)||((this.page!= null)&&this.page.equals(rhs.page))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))));
    }

}

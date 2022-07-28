package cat.hack3.mangrana.downloads.workers.sonarr.bean;

public class Season {
    private final String title;
    private final Integer seriesId;

    public Season(String title, Integer seriesId) {
        this.title = title;
        this.seriesId = seriesId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getSeriesId() {
        return seriesId;
    }
}

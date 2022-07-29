package cat.hack3.mangrana.downloads.workers.sonarr.bean;

public class Season {
    private final String downloadedFolderName;
    private final Integer seriesId;

    public Season(String downloadedFolderName, Integer seriesId, String downloadPath) {
        this.downloadedFolderName = downloadedFolderName;
        this.seriesId = seriesId;
    }

    public String getDownloadedFolderName() {
        return downloadedFolderName;
    }

    public Integer getSeriesId() {
        return seriesId;
    }
}

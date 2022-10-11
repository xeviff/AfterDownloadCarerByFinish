package tv.mangrana.downloads.workers.sonarr.bean;

import tv.mangrana.sonarr.api.schema.queue.Record;

public class Season {
    private final String downloadedFolderName;
    private final Record rcd;

    public Season(String title, Record rcd) {
        this.downloadedFolderName = title;
        this.rcd = rcd;
    }

    public String getDownloadedFolderName() {
        return downloadedFolderName;
    }

    public Integer getSerieId() {
        return rcd.getSeriesId();
    }

    public Integer getQueueItemId() {
        return rcd.getId();
    }
}

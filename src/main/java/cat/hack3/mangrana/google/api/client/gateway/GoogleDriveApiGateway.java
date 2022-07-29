package cat.hack3.mangrana.google.api.client.gateway;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.commons.collections4.CollectionUtils;
import org.o7planning.googledrive.example.GoogleDriveUtils;

import java.io.IOException;
import java.util.*;

import static cat.hack3.mangrana.utils.Output.log;

public class GoogleDriveApiGateway {

    Drive service;

    public enum GoogleElementType {FOLDER, VIDEO}

    public GoogleDriveApiGateway() throws IOException {
        service = GoogleDriveUtils.getDriveService();
    }

    public File lookupElementByName(String elementName, GoogleElementType type, String relatedTeamDriveId) throws IOException {
        String query = "name = '" + elementName.replace("'","\\'") + "'"
                + " and trashed=false"
                + getTypeFilterQuery(type);

        FileList fileList =
                service.files()
                        .list()
                        .setCorpora("drive")
                        .setTeamDriveId(relatedTeamDriveId)
                        .setIncludeItemsFromAllDrives(true)
                        .setSupportsTeamDrives(true)
                        .setQ(query)
                        .execute();

        List<File> files = Optional.ofNullable(
                        fileList.getFiles())
                .orElseThrow(() -> new NoSuchElementException("element not found by name: " + elementName));

        if (CollectionUtils.isNotEmpty(files)) {
            if (files.size() > 1) {
                log("ups, there are more than one matching element. it's better to take a look :S ");
                files.forEach(fl -> log(fl.toString()));
            }
            return files.get(0);
        } else {
            throw new NoSuchElementException("no elements in the list xO");
        }
    }

    public List<File> getChildrenById(String id, boolean onlyFolders)  {
        List<File> fullFileList = new ArrayList<>();

        String query =
                "trashed=false and "+
                        (onlyFolders ? "mimeType = 'application/vnd.google-apps.folder' and " : "")+
                        "'"+id+"' in parents";

        String pageToken = null;
        do {
            try {
                FileList fileList = service.files()
                        .list()
                        .setQ(query)
                        .setIncludeItemsFromAllDrives(true)
                        .setSupportsTeamDrives(true)
                        .setFields("nextPageToken, files(id, name)")
                        .setPageToken(pageToken)
                        .setOrderBy("name")
                        .execute();

                fullFileList.addAll(fileList.getFiles());
                pageToken = fileList.getNextPageToken();
            } catch (IOException e) {
                log("ERROR during api call");
                e.printStackTrace();
            }
        } while (pageToken != null);

        return fullFileList;
    }

    public void copyFile(String fileId, String destinationFolderId) throws IOException {
        File newFileReference = new File();
        newFileReference.setParents(Collections.singletonList(destinationFolderId));
        service.files()
                .copy(fileId, newFileReference)
                .setSupportsTeamDrives(true)
                .execute();
    }

    public File createFolder(String name, String parentFolderId) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(name);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setParents(Collections.singletonList(parentFolderId));
        return service
                .files()
                .create(fileMetadata)
                .setSupportsTeamDrives(true)
                .execute();
    }

    private String getTypeFilterQuery(GoogleElementType type) {
        switch (type) {
            case VIDEO:
                return " and mimeType contains 'video'";
            case FOLDER:
                return " and mimeType = 'application/vnd.google-apps.folder'";
            default:
                return "";
        }
    }

}

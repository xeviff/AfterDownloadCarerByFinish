package cat.hack3.mangrana.google.api.client;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.RetryEngine;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.utils.Output;
import cat.hack3.mangrana.utils.PathUtils;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.FOLDER;
import static cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;

public class RemoteCopyService {

    ConfigFileLoader configFileLoader;
    GoogleDriveApiGateway googleDriveApiGateway;
    RetryEngine<File> retryEngine;

    public RemoteCopyService(ConfigFileLoader configFileLoader) throws IOException {
        this.configFileLoader = configFileLoader;
        googleDriveApiGateway = new GoogleDriveApiGateway();
    }
    public void setRetryEngine(RetryEngine<File> retryEngine){
        this.retryEngine = retryEngine;
    }

    public void copyVideoFile(String downloadedFileName, String destinationFullPath) throws IOException {
        File videoFile = googleDriveApiGateway
                .lookupElementByName(downloadedFileName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
        if (Objects.nonNull(videoFile)) {
            File destinationFolder = getOrCreateMovieFolderByPath(destinationFullPath);
            googleDriveApiGateway
                    .copyFile(videoFile.getId(), destinationFolder.getId());
            log(">> copied successfully!! :D ");
            log("fileName: "+downloadedFileName);
            log("fileId: "+videoFile.getId());
            log("destinationFolderName: "+destinationFullPath);
            log("destinationFolderId: "+destinationFolder.getId());
        } else {
            log("Video element not found in google drive :( " + downloadedFileName);
        }
    }

    private File getOrCreateMovieFolderByPath(String destinationFullPath) throws IOException {
        String destinationFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        try {
            return searchFolderByName(destinationFolderName);
        } catch (NoSuchElementException e) {
            log("failed finding the folder but we'll try to create it");
            try {
                return createFolderByParentName(PathUtils.getParentFromFullPath(destinationFullPath), destinationFolderName);
            } catch (IOException e2) {
                log("couldn't create the folder as well, so I surrender");
                throw e2;
            }
        }
    }

    public void copySeasonFromDownloadToItsLocation(String downloadedFolderName, String destinationFullPath, String seasonFolderName) throws IOException {
        log("willing for copy season <"+downloadedFolderName+"> to <"+destinationFullPath+"/"+seasonFolderName+">");
        Supplier<File> checkIfFolderExistFunction = () -> {
            try {
                File parentFolder =  googleDriveApiGateway.lookupElementById(configFileLoader.getConfig(DOWNLOADS_SERIES_FOLDER_ID));
                return googleDriveApiGateway.getChildFromParentByName(downloadedFolderName, parentFolder, true);
            } catch (Exception e) {
                log("could not find yet the folder: "+downloadedFolderName);
                return null;
            }
        };
        File downloadedSeasonFolder = Objects.isNull(retryEngine) ? checkIfFolderExistFunction.get() : retryEngine.tryUntilGotDesired(checkIfFolderExistFunction);
        if (Objects.isNull(downloadedSeasonFolder)) throw new NoSuchElementException("definitely, could not retrieve the downloaded folder");

        String destinationFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        File destinationSerieFolder = getOrCreateSerieFolder(destinationFullPath, destinationFolderName);
        File seasonFolder = getOrCreateSeasonFolder(seasonFolderName, destinationSerieFolder);
        List<File> seasonEpisodesGFiles = googleDriveApiGateway.getChildrenFromParent(downloadedSeasonFolder, false);
        seasonEpisodesGFiles.forEach(episodeFile ->
                copySeasonEpisode(episodeFile, seasonFolder.getId()));
    }

    public void copyEpisodeFromDownloadToItsLocation(String downloadedFileName, String destinationFullPath, String seasonFolderName) throws IOException {
        log("willing for copy episode <"+downloadedFileName+"> to <"+destinationFullPath+"/"+seasonFolderName+">");
        Supplier<File> checkIfFileExistFunction = () -> {
            try {
                return googleDriveApiGateway.lookupElementByName(downloadedFileName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
            } catch (Exception e) {
                log("could not find yet the file: "+downloadedFileName);
                return null;
            }
        };
        File downloadedFile = Objects.isNull(retryEngine) ? checkIfFileExistFunction.get() : retryEngine.tryUntilGotDesired(checkIfFileExistFunction);
        if (Objects.isNull(downloadedFile)) throw new NoSuchElementException("definitely, could not retrieve the video file");

        String destinationSerieFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        File destinationSerieFolder = getOrCreateSerieFolder(destinationFullPath, destinationSerieFolderName);
        File seasonFolder = getOrCreateSeasonFolder(seasonFolderName, destinationSerieFolder);
        copySeasonEpisode(downloadedFile, seasonFolder.getId());
    }

    private void copySeasonEpisode(File episodeFile, String destinationSerieFolder) {
        log("copying "+episodeFile.getName()+" to "+destinationSerieFolder);
        try {
            googleDriveApiGateway.copyFile(episodeFile.getId(), destinationSerieFolder);
        } catch (IOException e) {
            log("caution! this file could not been copied!!");
            e.printStackTrace();
        }
    }

    private File getOrCreateSerieFolder(String destinationFullPath, String destinationFolderName) throws IOException {
        File destinationSerieFolder;
        try {
            destinationSerieFolder = googleDriveApiGateway.lookupElementByName(destinationFolderName, FOLDER, configFileLoader.getConfig(SERIES_TEAM_DRIVE_ID));
        } catch (NoSuchElementException e) {
            String parentDirectory = PathUtils.getParentFromFullPath(destinationFullPath);
            File seriesFolderParent = googleDriveApiGateway.lookupElementByName(parentDirectory, FOLDER, configFileLoader.getConfig(SERIES_TEAM_DRIVE_ID));
            destinationSerieFolder = googleDriveApiGateway.createFolder(destinationFolderName, seriesFolderParent.getId());
        }
        return destinationSerieFolder;
    }

    private File getOrCreateSeasonFolder(String seasonFolderName, File destinationSerieFolder) throws IOException {
        File seasonFolder;
        try {
            seasonFolder = googleDriveApiGateway.getChildFromParentByName(seasonFolderName, destinationSerieFolder, true);
        } catch (NoSuchElementException e) {
            seasonFolder = googleDriveApiGateway.createFolder(seasonFolderName, destinationSerieFolder.getId());
        }
        return seasonFolder;
    }

    private File searchFolderByName(String destinationFolderName) throws IOException {
        return googleDriveApiGateway
                .lookupElementByName(destinationFolderName, FOLDER, configFileLoader.getConfig(MOVIES_TEAM_DRIVE_ID));
    }

    private File createFolderByParentName(String parentDirectory, String destinationFolderName) throws IOException {
        File parentFolder = searchFolderByName(parentDirectory);
        return googleDriveApiGateway.createFolder(destinationFolderName, parentFolder.getId());
    }

    private void log (String msg) {
        Output.log("CopyService: "+msg);
    }
}

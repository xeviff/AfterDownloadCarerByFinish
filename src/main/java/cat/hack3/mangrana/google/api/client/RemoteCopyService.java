package cat.hack3.mangrana.google.api.client;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.downloads.workers.RetryEngine;
import cat.hack3.mangrana.exception.NoElementFoundException;
import cat.hack3.mangrana.exception.TooMuchTriesException;
import cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import cat.hack3.mangrana.utils.EasyLogger;
import cat.hack3.mangrana.utils.PathUtils;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static cat.hack3.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.FOLDER;
import static cat.hack3.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;
import static cat.hack3.mangrana.utils.Output.msg;

public class RemoteCopyService {

    private final EasyLogger logger;

    ConfigFileLoader configFileLoader;
    GoogleDriveApiGateway googleDriveApiGateway;
    RetryEngine<File> retryEngine;

    public RemoteCopyService(ConfigFileLoader configFileLoader) throws IOException {
        this.logger = new EasyLogger("CopyService");
        this.configFileLoader = configFileLoader;
        googleDriveApiGateway = new GoogleDriveApiGateway();
    }
    public void setRetryEngine(RetryEngine<File> retryEngine){
        this.retryEngine = retryEngine;
    }

    public void copyVideoFile(String downloadedFileName, String destinationFullPath) throws IOException, NoElementFoundException {
        File videoFile = googleDriveApiGateway
                .lookupElementByName(downloadedFileName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
        if (Objects.nonNull(videoFile)) {
            File destinationFolder = getOrCreateMovieFolderByPath(destinationFullPath);
            googleDriveApiGateway
                    .copyFile(videoFile.getId(), destinationFolder.getId());
            logger.nLog(">> copied successfully!! :D ");
            logger.nLog("fileName: "+downloadedFileName);
            logger.nLog("fileId: "+videoFile.getId());
            logger.nLog("destinationFolderName: "+destinationFullPath);
            logger.nLog("destinationFolderId: "+destinationFolder.getId());
        } else {
            logger.nLog("Video element not found in google drive :( " + downloadedFileName);
        }
    }

    private File getOrCreateMovieFolderByPath(String destinationFullPath) throws IOException, NoElementFoundException {
        String destinationFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        try {
            return searchFolderByName(destinationFolderName);
        } catch (NoElementFoundException e) {
            logger.nLog("failed finding the folder but we'll try to create it");
            try {
                return createFolderByParentName(PathUtils.getParentFromFullPath(destinationFullPath), destinationFolderName);
            } catch (IOException | NoElementFoundException e2) {
                logger.nLog("couldn't create the folder as well, so I surrender");
                throw e2;
            }
        }
    }

    public void copySeasonFromDownloadToItsLocation(String downloadedFolderName, String destinationFullPath, String seasonFolderName) throws IOException, TooMuchTriesException, NoElementFoundException {
        String destinationDescription = msg("<{0}/{1}>",destinationFullPath, seasonFolderName);
        final int[] showedCount = {0};
        Supplier<File> getDownloadedSeasonFolder = () -> {
            try {
                File parentFolder =  googleDriveApiGateway.lookupElementById(configFileLoader.getConfig(DOWNLOADS_SERIES_FOLDER_ID));
                return googleDriveApiGateway.getChildFromParentByName(downloadedFolderName, parentFolder, true);
            } catch (Exception e) {
                if (showedCount[0] ==0)
                    logger.nLog("Could not find yet the folder <{0}>", downloadedFolderName);
                showedCount[0]++;
                return null;
            }
        };
        File downloadedSeasonFolder = Objects.isNull(retryEngine) ? getDownloadedSeasonFolder.get() : retryEngine.tryUntilGotDesired(getDownloadedSeasonFolder);
        if (Objects.isNull(downloadedSeasonFolder))
            throw new NoElementFoundException("SHOULD NOT HAPPEN! definitely, could not retrieve the downloaded folder "+ downloadedFolderName);

        String destinationFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        File destinationSerieFolder = getOrCreateSerieFolder(destinationFullPath, destinationFolderName);
        File seasonFolder = getOrCreateSeasonFolder(seasonFolderName, destinationSerieFolder);
        logger.nLog("Going to copy all season''s episodes to <{0}> ( GDriveId: {1} )", destinationDescription, seasonFolder.getId());
        List<File> seasonEpisodesGFiles = googleDriveApiGateway.getChildrenFromParent(downloadedSeasonFolder, false);
        seasonEpisodesGFiles.forEach(episodeFile ->
                copySeasonEpisode(episodeFile, seasonFolder.getId(), destinationDescription));
    }

    public void copyEpisodeFromDownloadToItsLocation(String downloadedFileName, String destinationFullPath, String seasonFolderName) throws IOException, NoElementFoundException, TooMuchTriesException {
        String destinationDescription = msg("<{0}/{1}>",destinationFullPath, seasonFolderName);
        final int[] showedCount = {0};
        Supplier<File> getDownloadedEpisodeFile = () -> {
            try {
                return googleDriveApiGateway.lookupElementByName(downloadedFileName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
            } catch (Exception e) {
                if (showedCount[0] ==0)
                    logger.nLog("Could not find yet the file " + downloadedFileName);
                showedCount[0]++;
                return null;
            }
        };
        File downloadedFile = Objects.isNull(retryEngine) ? getDownloadedEpisodeFile.get() : retryEngine.tryUntilGotDesired(getDownloadedEpisodeFile);
        if (Objects.isNull(downloadedFile))
            throw new NoElementFoundException("SHOULD NOT HAPPEN! definitely, could not retrieve the video file "+downloadedFileName);

        String destinationSerieFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        File destinationSerieFolder = getOrCreateSerieFolder(destinationFullPath, destinationSerieFolderName);
        File seasonFolder = getOrCreateSeasonFolder(seasonFolderName, destinationSerieFolder);

        copySeasonEpisode(downloadedFile, seasonFolder.getId(), destinationDescription);
    }

    private void copySeasonEpisode(File episodeFile, String destinationSerieFolder, String destinationDescription) {
        String msgIntro = "Episode file <{0}> has been successfully copied";
        try {
            googleDriveApiGateway.copyFile(episodeFile.getId(), destinationSerieFolder);
            if (StringUtils.isNotEmpty(destinationDescription)) {
                logger.nLog(msgIntro + " to <{1}> ( GDrive id: {2} )",
                        episodeFile.getName(), destinationDescription, destinationSerieFolder);
            } else {
                logger.nLog(msgIntro, episodeFile.getName());
            }
        } catch (IOException e) {
            logger.nLog("SHOULD NOT HAPPEN! The <{0}> file could not been copied to <{1}> ( GDrive id: {2} )"
                    ,episodeFile.getName(), destinationDescription, destinationSerieFolder);
            e.printStackTrace();
        }
    }

    private File getOrCreateSerieFolder(String destinationFullPath, String destinationFolderName) throws IOException, NoElementFoundException {
        File destinationSerieFolder;
        try {
            destinationSerieFolder = googleDriveApiGateway.lookupElementByName(destinationFolderName, FOLDER, configFileLoader.getConfig(SERIES_TEAM_DRIVE_ID));
        } catch (NoElementFoundException e) {
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
        } catch (NoElementFoundException e) {
            seasonFolder = googleDriveApiGateway.createFolder(seasonFolderName, destinationSerieFolder.getId());
        }
        return seasonFolder;
    }

    private File searchFolderByName(String destinationFolderName) throws IOException, NoElementFoundException {
        return googleDriveApiGateway
                .lookupElementByName(destinationFolderName, FOLDER, configFileLoader.getConfig(MOVIES_TEAM_DRIVE_ID));
    }

    private File createFolderByParentName(String parentDirectory, String destinationFolderName) throws IOException, NoElementFoundException {
        File parentFolder = searchFolderByName(parentDirectory);
        return googleDriveApiGateway.createFolder(destinationFolderName, parentFolder.getId());
    }

}

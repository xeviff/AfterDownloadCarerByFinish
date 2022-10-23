package tv.mangrana.google.api.client;

import tv.mangrana.config.ConfigFileLoader;
import tv.mangrana.downloads.workers.common.RetryEngine;
import tv.mangrana.exception.NoElementFoundException;
import tv.mangrana.exception.TooMuchTriesException;
import tv.mangrana.google.api.client.gateway.GoogleDriveApiGateway;
import tv.mangrana.utils.EasyLogger;
import tv.mangrana.utils.PathUtils;
import com.google.api.services.drive.model.File;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static tv.mangrana.config.ConfigFileLoader.ProjectConfiguration.*;
import static tv.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.FOLDER;
import static tv.mangrana.google.api.client.gateway.GoogleDriveApiGateway.GoogleElementType.VIDEO;
import static tv.mangrana.utils.Output.msg;

public class RemoteCopyService {

    private final EasyLogger logger;

    ConfigFileLoader configFileLoader;
    GoogleDriveApiGateway googleDriveApiGateway;
    RetryEngine<File> retryEngine;

    private static final int TOO_MUCH_RETRIES_THRESHOLD = 40;

    public RemoteCopyService(ConfigFileLoader configFileLoader) throws IOException {
        this.logger = new EasyLogger("CopyService");
        this.configFileLoader = configFileLoader;
        googleDriveApiGateway = new GoogleDriveApiGateway();
    }
    public void setRetryEngine(RetryEngine<File> retryEngine){
        this.retryEngine = retryEngine;
    }

    public void copyMovieFile(String downloadedFileName, String destinationFullPath) throws IOException, NoElementFoundException, TooMuchTriesException {
        File downloadedFile = getDownloadedVideoFile(downloadedFileName);
        File destinationFolder = getOrCreateMovieFolderByPath(destinationFullPath);
        googleDriveApiGateway.copyFile(downloadedFile.getId(), destinationFolder.getId());
        logger.nLog("Movie file <{0}> has been successfully copied to <{1}> ( GDrive id: {2} )",
                downloadedFileName, destinationFullPath, destinationFolder.getId());
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
        File downloadedSeasonFolder = Objects.isNull(retryEngine) ? getDownloadedSeasonFolder.get() : retryEngine.tryUntilGotDesired(getDownloadedSeasonFolder, TOO_MUCH_RETRIES_THRESHOLD);
        if (Objects.isNull(downloadedSeasonFolder))
            throw new NoElementFoundException("SHOULD NOT HAPPEN! definitely, could not retrieve the downloaded folder "+ downloadedFolderName);

        String destinationFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        File destinationSerieFolder = getOrCreateSerieFolder(destinationFullPath, destinationFolderName);
        File seasonFolder = getOrCreateSeasonFolder(seasonFolderName, destinationSerieFolder);
        logger.nLog("Going to copy all season''s episodes to <{0}> ( GDriveId: {1} )", destinationDescription, seasonFolder.getId());
        List<File> seasonEpisodesGFiles = googleDriveApiGateway.getChildrenFromParent(downloadedSeasonFolder, false);
        seasonEpisodesGFiles.forEach(episodeFile ->
                copySeasonEpisode(episodeFile, seasonFolder.getId(), null));
    }

    public void copyEpisodeFromDownloadToItsLocation(String downloadedFileName, String destinationFullPath, String seasonFolderName) throws IOException, NoElementFoundException, TooMuchTriesException {
        File downloadedFile = getDownloadedVideoFile(downloadedFileName);

        String destinationSerieFolderName = PathUtils.getCurrentFromFullPath(destinationFullPath);
        File destinationSerieFolder = getOrCreateSerieFolder(destinationFullPath, destinationSerieFolderName);
        File seasonFolder = getOrCreateSeasonFolder(seasonFolderName, destinationSerieFolder);

        copySeasonEpisode(downloadedFile, seasonFolder.getId(), msg("<{0}/{1}>",destinationFullPath, seasonFolderName));
    }

    private File getDownloadedVideoFile(String downloadedFileName) throws TooMuchTriesException, NoElementFoundException {
        final int[] showedCount = {0};
        Supplier<File> getDownloadedEpisodeFile = () -> {
            try {
                return googleDriveApiGateway.lookupElementByName(downloadedFileName, VIDEO, configFileLoader.getConfig(DOWNLOADS_TEAM_DRIVE_ID));
            } catch (Exception e) {
                if (showedCount[0] ==0) {
                    logger.nLog("Could not find yet the file <{0}>", downloadedFileName);
                }
                showedCount[0]++;
                return null;
            }
        };
        File downloadedFile = Objects.isNull(retryEngine) ? getDownloadedEpisodeFile.get() : retryEngine.tryUntilGotDesired(getDownloadedEpisodeFile, TOO_MUCH_RETRIES_THRESHOLD);
        if (Objects.isNull(downloadedFile)) {
            throw new NoElementFoundException("SHOULD NOT HAPPEN! definitely, could not retrieve the video file "+ downloadedFileName);
        }
        return downloadedFile;
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
            logger.nHLog("The <{0}> file could not been copied to <{1}> ( GDrive id: {2} )"
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

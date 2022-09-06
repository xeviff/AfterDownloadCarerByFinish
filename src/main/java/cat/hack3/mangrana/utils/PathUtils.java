package cat.hack3.mangrana.utils;


import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import static cat.hack3.mangrana.utils.Output.msg;

public class PathUtils {

    private PathUtils(){}

    public static String getParentFromFullPath(String absolutePath){
        return Paths
                .get(absolutePath)
                .getParent()
                .getFileName()
                .toString();
    }

    public static String getCurrentFromFullPath(String absolutePath) {
        return absolutePath.substring(absolutePath.lastIndexOf('/')+1);
    }

    public static File shiftFileFolder(File jobFile, SonarrJobFile.JobLocation folderOrigin, SonarrJobFile.JobLocation folderDestination) {
        try {
            Path newPath = Files.move(
                    jobFile.toPath()
                    , Paths.get(jobFile.getAbsolutePath()
                            .replaceFirst(folderOrigin.getFolderName(), folderDestination.getFolderName())));
            log(msg("moved job file <{2}> from -{0}- to -{1}-", folderOrigin, folderDestination, jobFile.getAbsolutePath()));
            return newPath.toFile();
        } catch (IOException e) {
            log(msg("COULD NOT MOVE file {2} from -{0}- to -{1}-", folderOrigin, folderDestination, jobFile.getAbsolutePath()));
            return jobFile;
        }
    }

    public static int compareFileCreationDate (File o1, File o2) {
        final String creationTimeAttr = "creationTime";
        int res = 0;
        try {
            FileTime o1Birthday = (FileTime) Files.getAttribute(o1.toPath(), creationTimeAttr);
            FileTime o2Birthday = (FileTime) Files.getAttribute(o2.toPath(), creationTimeAttr);
            res = o1Birthday.compareTo(o2Birthday);
        } catch (IOException e) {
            log("there was a problem trying to compare creation date between "
            + o1.getName() + " and " + o2.getName());
        }
        return res;
    }

    private static void log(String msg){
        Output.log("PathUtils: "+msg);
    }
}

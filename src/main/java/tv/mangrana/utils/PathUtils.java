package tv.mangrana.utils;



import tv.mangrana.downloads.workers.common.jobs.JobFile;
import tv.mangrana.utils.Output;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

public class PathUtils {

    public static final char SEPARATOR = '/';

    private PathUtils(){}

    public static String addSubFolder(String parentFolder, String childFolder) {
        return parentFolder+ SEPARATOR +childFolder;
    }
    public static String rootFolder(String rootFolderName){
        return addSubFolder("",rootFolderName);
    }

    public static String getParentFromFullPath(String absolutePath){
        return Paths
                .get(absolutePath)
                .getParent()
                .getFileName()
                .toString();
    }

    public static String getCurrentFromFullPath(String absolutePath) {
        return absolutePath.substring(absolutePath.lastIndexOf(SEPARATOR)+1);
    }

    public static File shiftFileFolder(File jobFile, JobFile.JobLocation folderOrigin, JobFile.JobLocation folderDestination) {
        try {
            Path newPath = Files.move(
                    jobFile.toPath()
                    , Paths.get(jobFile.getAbsolutePath()
                            .replaceFirst(folderOrigin.getFolderName(), folderDestination.getFolderName())));
            log(Output.msg("moved job file <{2}> from -{0}- to -{1}-", folderOrigin, folderDestination, jobFile.getAbsolutePath()));
            return newPath.toFile();
        } catch (IOException e) {
            log(Output.msg("COULD NOT MOVE file {2} from -{0}- to -{1}-", folderOrigin, folderDestination, jobFile.getAbsolutePath()));
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

    public static String getRootProjectPath() {
        return System.getProperty("user.dir");
    }
}

package cat.hack3.mangrana.utils;


import cat.hack3.mangrana.downloads.workers.sonarr.jobs.SonarrJobFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static cat.hack3.mangrana.utils.Output.log;

public class PathUtils {

    private PathUtils(){}

    public static void main(String[] args) throws IncorrectWorkingReferencesException, IOException {
        SonarrJobFileLoader x = new SonarrJobFileLoader();
        moveJobFileToDoneFolder(x.getFile());
    }

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

    public static void moveJobFileToDoneFolder(File jobFile) throws IOException {
        Files.move(
                jobFile.toPath()
                , Paths.get(jobFile.getParent()
                        + "/done/"+jobFile.getName()));
        log("moved job file to -done- folder");
    }

}

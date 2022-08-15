package cat.hack3.mangrana.utils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import static cat.hack3.mangrana.utils.Output.log;

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

    public static File shiftFileFolder(File jobFile, String folderOrigin, String folderDestination) {
        try {
            Path newPath = Files.move(
                    jobFile.toPath()
                    , Paths.get(jobFile.getAbsolutePath()
                            .replaceFirst(folderOrigin, folderDestination)));
            log(MessageFormat.format("moved job file from -{0}- to -{1}-", folderOrigin, folderDestination));
            return newPath.toFile();
        } catch (IOException e) {
            log(MessageFormat.format("could not move file from -{0}- to -{1}-", folderOrigin, folderDestination));
            return jobFile;
        }
    }

}

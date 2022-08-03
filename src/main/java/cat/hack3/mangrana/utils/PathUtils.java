package cat.hack3.mangrana.utils;

import java.nio.file.Paths;

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

}

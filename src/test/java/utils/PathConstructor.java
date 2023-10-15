package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PathConstructor {

    private PathConstructor() {}

    public static String constructPath(String ... strings){
        StringBuilder path = new StringBuilder();
        for (String string: strings) {
            path.append(File.separator).append(string);
        }
        return path.toString();
    }

    public static Path getCurrentPath(){
        Path currentDirectory;
        try {
            currentDirectory = Path.of(".").toRealPath();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currentDirectory;
    }
}

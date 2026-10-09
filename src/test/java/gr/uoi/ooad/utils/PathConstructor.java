package gr.uoi.ooad.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PathConstructor {

    public static String constructPath(String... strings) {
        return String.join(File.separator, strings);
    }

    public static Path getCurrentPath() {
        try {
            return Path.of(".").toRealPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Utility class. Do not instantiate. */
    private PathConstructor() {}
}

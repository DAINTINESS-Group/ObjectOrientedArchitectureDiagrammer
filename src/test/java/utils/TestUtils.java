package utils;

public class TestUtils {
    public static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\r", "\\r")
                .replace("\n", "\\n\n")
                .replace("\t", "\\t");
    }
}

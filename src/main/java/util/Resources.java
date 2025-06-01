package util;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class Resources {

    public static InputStream loadResourceFile(String relativePath) {
        return Resources.class.getClassLoader().getResourceAsStream(relativePath);
    }

    public static URI getResourceURI(String relativePath) throws URISyntaxException {
        return Resources.class.getClassLoader().getResource(relativePath).toURI();
    }
}

package util;

import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class Resources {

    public static InputStream loadResourceFile(String relativePath) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
    }

    public static URI getResourceURI(String relativePath) throws URISyntaxException {
        return Thread.currentThread().getContextClassLoader().getResource(relativePath).toURI();
    }

}

package util;

import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class Resources {

    public static final String DEFAULT_PROPERTIES_PATH = "styles/smartgraph.properties";
    public static final String DEFAULT_STYLE_PATH = "styles/smartgraph.css";

    public static InputStream loadResourceFile(String relativePath){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
    }

    public static URI getResourceURI(String relativePath)  {
        try {
            return Thread.currentThread().getContextClassLoader().getResource(relativePath).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static SmartGraphProperties getSmartgraphProperties(){
        return new SmartGraphProperties(Resources.loadResourceFile(Resources.DEFAULT_PROPERTIES_PATH));
    }

    public static URI getSmartGraphStyleURI(){
        return Resources.getResourceURI(Resources.DEFAULT_STYLE_PATH);
    }

}

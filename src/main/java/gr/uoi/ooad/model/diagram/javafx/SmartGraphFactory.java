package gr.uoi.ooad.model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import gr.uoi.ooad.util.Resources;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class SmartGraphFactory {

    public static final String DEFAULT_PROPERTIES_PATH = "styles/smartgraph.properties";
    public static final String DEFAULT_STYLE_PATH = "styles/smartgraph.css";
    private static SmartGraphProperties smartGraphProperties = null;
    /**
     * Factory for SmartGraphPanel object creation
     *
     * @param graph
     * @return
     */
    public static SmartGraphPanel<JavaFXUMLNode, String> createGraphView(
            Graph<JavaFXUMLNode, String> graph) {
        try {
            smartGraphProperties = getSmartGraphProperties();
            URI url = getSmartGraphStyleURI();
            URI cssFile = Objects.requireNonNull(url);
            return new SmartGraphPanel<>(
                    graph, smartGraphProperties, new SmartCircularSortedPlacementStrategy(), cssFile);
        } catch (URISyntaxException ignored) {
            // Fallback to default paths.
            return new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        }
    }

    public static SmartGraphProperties getSmartGraphProperties() {
        if (smartGraphProperties == null){
            return createSmartGraphProperties();
        }
        return smartGraphProperties;
    }

    public static SmartGraphProperties createSmartGraphProperties() {
        return new SmartGraphProperties(Resources.loadResourceFile(DEFAULT_PROPERTIES_PATH));
    }

    public static URI getSmartGraphStyleURI() throws URISyntaxException {
        return Resources.getResourceURI(DEFAULT_STYLE_PATH);
    }
}

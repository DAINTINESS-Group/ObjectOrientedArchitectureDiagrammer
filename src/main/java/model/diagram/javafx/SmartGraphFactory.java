package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import util.Resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class SmartGraphFactory {

    public static final String DEFAULT_PROPERTIES_PATH = "styles/smartgraph.properties";
    public static final String DEFAULT_STYLE_PATH = "styles/smartgraph.css";

    /**
     * Factory for SmartGraphPanel object creation
     * @param graph
     * @return
     */
    public static SmartGraphPanel<String, String> createGraphView(Graph<String, String> graph)
    {
        try
        {
            SmartGraphProperties properties = getSmartgraphProperties();
            URI url = getSmartGraphStyleURI();
            URI cssFile = Objects.requireNonNull(url);
            return new SmartGraphPanel<>(graph, properties, new SmartCircularSortedPlacementStrategy(), cssFile);
        }
        catch (URISyntaxException ignored)
        {
            // Fallback to default paths.
            return new SmartGraphPanel<>(graph, new SmartCircularSortedPlacementStrategy());
        }
    }

    public static SmartGraphProperties getSmartgraphProperties() {
        return new SmartGraphProperties(Resources.loadResourceFile(DEFAULT_PROPERTIES_PATH));
    }

    public static URI getSmartGraphStyleURI() throws URISyntaxException {
        return Resources.getResourceURI(DEFAULT_STYLE_PATH);
    }
}

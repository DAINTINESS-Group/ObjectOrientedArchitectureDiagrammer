package model.diagram.javafx;

import java.util.Collection;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public interface JavaFXVisualization {
    SmartGraphPanel<String, String> createGraphView();
    
	Collection<Edge<String, String>> getEdgeCollection();
	
	Collection<Vertex<String>> getVertexCollection();
}

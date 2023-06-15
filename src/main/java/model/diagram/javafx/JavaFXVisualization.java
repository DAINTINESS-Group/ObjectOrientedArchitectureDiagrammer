package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

public interface JavaFXVisualization {
    SmartGraphPanel<String, String> createGraphView();
}

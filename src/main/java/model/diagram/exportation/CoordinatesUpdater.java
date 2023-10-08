package model.diagram.exportation;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import model.diagram.ClassDiagram;
import model.diagram.PackageDiagram;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;

import java.util.Collection;

public class CoordinatesUpdater {

	private PackageDiagram packageDiagram;
	private ClassDiagram   classDiagram;

	public CoordinatesUpdater(PackageDiagram packageDiagram) {
		this.packageDiagram = packageDiagram;
	}

	public CoordinatesUpdater(ClassDiagram classDiagram) {
		this.classDiagram = classDiagram;
	}

	public void updatePackageCoordinates(Collection<Vertex<String>> vertexCollection, SmartGraphPanel<String, String> graphView) {
		for(Vertex<String> vertex : vertexCollection) {
			double x = graphView.getVertexPositionX(vertex);
			double y = graphView.getVertexPositionY(vertex);
			for(PackageVertex packageVertex : packageDiagram.getGraphNodes().keySet()) {
				if(!packageVertex.getName().equals(vertex.element())) {
					continue;
				}
				packageVertex.setCoordinates(x, y);
			}
		}
	}

	public void updateClassCoordinates(Collection<Vertex<String>> vertexCollection, SmartGraphPanel<String, String> graphView) {
		if (vertexCollection == null) return;

		for(Vertex<String> vertex : vertexCollection) {
			double x = graphView.getVertexPositionX(vertex);
			double y = graphView.getVertexPositionY(vertex);
			for(ClassifierVertex classifierVertex : classDiagram.getGraphNodes().keySet()) {
				if (!classifierVertex.getName().equals(vertex.element())) {
					continue;
				}
				classifierVertex.setCoordinates(x, y);
			}
		}
	}

}

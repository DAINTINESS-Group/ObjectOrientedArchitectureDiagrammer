package model.diagram.exportation;

import java.util.Collection;

import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import model.diagram.ClassDiagram;
import model.diagram.PackageDiagram;
import model.graph.ClassifierVertex;
import model.graph.PackageVertex;

public class CoordinatesUpdater {

	private PackageDiagram packageDiagram;
	private ClassDiagram classDiagram;

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
				if(packageVertex.getName().equals(vertex.element())) {
					packageVertex.setCoordinates(x, y);
					break;
				}
			}
		}
	}

	public void updateClassCoordinates(Collection<Vertex<String>> vertexCollection, SmartGraphPanel<String, String> graphView) {
		for(Vertex<String> vertex : vertexCollection) {
			double x = graphView.getVertexPositionX(vertex);
			double y = graphView.getVertexPositionY(vertex);
			for(ClassifierVertex classifierVertex : classDiagram.getGraphNodes().keySet()) {
				if(classifierVertex.getName().equals(vertex.element())) {
					classifierVertex.setCoordinates(x, y);
					break;
				}
			}
		}
	}

}

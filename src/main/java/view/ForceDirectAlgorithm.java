package view;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import javafx.geometry.Point2D;

public class ForceDirectAlgorithm {

	private SmartGraphPanel<String, String> graphView;
	private Collection<Edge<String, String>> edgeCollection;
	private Map<Vertex<String>, Set<Vertex<String>>> neighbours = new HashMap<>();
	private Collection<Vertex<String>> vertexCollection;
	private static final int WIDTH = 914;
    private static final int HEIGHT = 637;
    private static final int ITERATIONS = 10;
    private static final double K = 0.5; // Force constant
    private static final double C = 0.005; // Repulsion factor
    private static final double D = 150.0; // Optimal distance between nodes

	public ForceDirectAlgorithm(SmartGraphPanel<String, String> graphView, Collection<Edge<String, String>> edgeCollection, Collection<Vertex<String>> vertexCollection) {
		this.graphView = graphView;
		this.edgeCollection = edgeCollection;
		this.vertexCollection = vertexCollection;
	}
	
	public SmartGraphPanel<String, String> getUpdatedGraph(){
		fillNeighboursMap();
		Random random = new Random();
        for (Vertex<String> vert : vertexCollection) {
        	double x, y;
        	do {
                x = random.nextDouble() * WIDTH;
                y = random.nextDouble() * HEIGHT;
            } while (x < 0 || x > WIDTH || y < 0 || y > HEIGHT);
        	graphView.setVertexPosition(vert, random.nextDouble() * WIDTH, random.nextDouble() * HEIGHT);
        }

        // Apply force-directed layout algorithm
        for (int i = 0; i < ITERATIONS; i++) {
            // Calculate attractive forces between connected vertices
            for (Vertex<String> vertex : vertexCollection) {
            	System.out.println(vertex.element());
                double vertX = graphView.getVertexPositionX(vertex);
                double vertY = graphView.getVertexPositionY(vertex);
                Point2D vertexPoint = new Point2D(vertX, vertY);
                Point2D totalForce = new Point2D(0, 0);
                for (Vertex<String> neighbor : neighbours.get(vertex)) {
                    double neighborX = graphView.getVertexPositionX(neighbor);
                    double neighborY = graphView.getVertexPositionY(neighbor);
                    Point2D neighborPoint = new Point2D(neighborX, neighborY);
                    Point2D direction = neighborPoint.subtract(vertexPoint);
                    double distance = direction.magnitude();
                    Point2D force = direction.normalize().multiply(distance / D).multiply(K);
                    totalForce = totalForce.add(force);
                }
                Point2D newPoint = vertexPoint.add(totalForce);
                graphView.setVertexPosition(vertex, newPoint.getX(), newPoint.getY());
            }

            // Calculate repulsive forces between all vertices
            for (Vertex<String> vertex : vertexCollection) {
                double vertX = graphView.getVertexPositionX(vertex);
                double vertY = graphView.getVertexPositionY(vertex);
                Point2D vertexPoint = new Point2D(vertX, vertY);
                Point2D totalForce = new Point2D(0, 0);
                for (Vertex<String> other : vertexCollection) {
                    if (other != vertex) {
                        double otherX = graphView.getVertexPositionX(other);
                        double otherY = graphView.getVertexPositionY(other);
                        Point2D otherPoint = new Point2D(otherX, otherY);
                        Point2D direction = vertexPoint.subtract(otherPoint);
                        double distance = direction.magnitude();
                        if (distance > 0) {
                            Point2D force = direction.normalize().multiply(C / Math.pow(distance, 2));
                            totalForce = totalForce.add(force);
                        }
                    }
                }
                Point2D newPoint = vertexPoint.subtract(totalForce);
                graphView.setVertexPosition(vertex, newPoint.getX(), newPoint.getY());
            }
        }

        // Adjust positions to create more spacing between vertices
        double spacingFactor = 50.0;
        adjustVertexPositions(spacingFactor);

        return graphView;
    }

    private void adjustVertexPositions(double spacingFactor) {
        // Calculate the average position of all vertices
        double totalX = 0;
        double totalY = 0;
        for (Vertex<String> vertex : vertexCollection) {
            double x = graphView.getVertexPositionX(vertex);
            double y = graphView.getVertexPositionY(vertex);
            totalX += x;
            totalY += y;
        }
        double averageX = totalX / vertexCollection.size();
        double averageY = totalY / vertexCollection.size();

        // Move each vertex away from the average position
        for (Vertex<String> vertex : vertexCollection) {
            double x = graphView.getVertexPositionX(vertex);
            double y = graphView.getVertexPositionY(vertex);
            double directionX = x - averageX;
            double directionY = y - averageY;
            double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
            double newX = x + (directionX / magnitude) * spacingFactor;
            double newY = y + (directionY / magnitude) * spacingFactor;
            graphView.setVertexPosition(vertex, newX, newY);
        }
    }
	
	private void fillNeighboursMap() {
		for (Edge<String, String> i : edgeCollection){
        	Vertex<String>[] connectedNodes = i.vertices();
        	if (neighbours == null) {
        		neighbours.put(connectedNodes[0], new HashSet<>());
        		neighbours.put(connectedNodes[1], new HashSet<>());
        	}
        	if (!neighbours.containsKey(connectedNodes[0])) {
        		neighbours.put(connectedNodes[0], new HashSet<>());
            }
        	if (!neighbours.containsKey(connectedNodes[1])) {
        		neighbours.put(connectedNodes[1], new HashSet<>());
            }
        	neighbours.get(connectedNodes[0]).add(connectedNodes[1]);
        	neighbours.get(connectedNodes[1]).add(connectedNodes[0]);
        }
	}

}
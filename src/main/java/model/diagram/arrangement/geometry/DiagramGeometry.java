package model.diagram.arrangement.geometry;

import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;

public class DiagramGeometry {
	
	private Map<String, Pair<Double, Double>> geometryMap;
	private final static int MIN_X_WINDOW_VALUE = 25;
	private final static int MIN_Y_WINDOW_VALUE = 25;
	
    public DiagramGeometry() {
        this.geometryMap = new HashMap<>();
    }
	
	public void addGeometry(GeometryNode key, double x, double y) {
        geometryMap.put(key.getNodeName(), new Pair<>(x, y));
    }
	
	public Pair<Double, Double> getVertexGeometry(GeometryNode key) {
        return geometryMap.get(key.getNodeName());
    }
	
	public Pair<Double, Double> getVertexGeometry(String stringKey){
		return geometryMap.get(stringKey);
	}
	
	public boolean containsKey(GeometryNode key) {
        return geometryMap.containsKey(key.getNodeName());
    }
	
	public boolean containsKey(String stringKey) {
		return geometryMap.containsKey(stringKey);
	}
	
	public void correctPositions(double x, double y) {
		// We hold minX and minY coordinates of the system, in order to bring the graph to match the left side and upper side of the window.
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double xDifference;
		double yDifference;
		for(Map.Entry<String, Pair<Double, Double>> entry : geometryMap.entrySet()) {
			double newX = entry.getValue().getValue0() + x;
			double newY = entry.getValue().getValue1() + y;
			geometryMap.put(entry.getKey(), new Pair<>(entry.getValue().getValue0() + x, entry.getValue().getValue1() + y));
			if( newX < minX) {
				minX = newX;
			}
			if( newY < minY) {
				minY = newY;
			}
		}
		xDifference = minX - MIN_X_WINDOW_VALUE;
		yDifference = minY - MIN_Y_WINDOW_VALUE;
		for(Map.Entry<String, Pair<Double, Double>> entry : geometryMap.entrySet()) {
			geometryMap.put(entry.getKey(), new Pair<>(entry.getValue().getValue0() - xDifference, entry.getValue().getValue1() - yDifference));
		}
	}
	
}

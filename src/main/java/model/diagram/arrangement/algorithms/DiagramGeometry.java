package model.diagram.arrangement.algorithms;

import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;

public class DiagramGeometry {
	
    private Map<String, Pair<Double, Double>> geometryMap;
	
	public DiagramGeometry() {
        this.geometryMap = new HashMap<>();
    }
	
	public void addGeometry(String key, double x, double y) {
        geometryMap.put(key, new Pair<>(x, y));
    }
	
	public Pair<Double, Double> getVertexGeometry(String key) {
        return geometryMap.get(key);
    }
	
	public boolean containsKey(String key) {
        return geometryMap.containsKey(key);
    }
	
}

package model.diagram.arrangement.geometry;

import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;

public class DiagramGeometry {
	
    private Map<String, Pair<Double, Double>> geometryMap;
	
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
	
}

package model.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum VertexType {
	CLASS,
	INTERFACE,
	ENUM,
	PACKAGE;

	public static final Map<String, VertexType> VERTEX_TYPE;

	static {
		Map<String, VertexType> map = new HashMap<>();
		for (VertexType vertexType: VertexType.values()) {
			map.put(vertexType.toString().toLowerCase(), vertexType);
		}
		VERTEX_TYPE = Collections.unmodifiableMap(map);
	}

	public static VertexType get(String vertexType) {
		return VERTEX_TYPE.get(vertexType.toLowerCase().trim());
	}
}

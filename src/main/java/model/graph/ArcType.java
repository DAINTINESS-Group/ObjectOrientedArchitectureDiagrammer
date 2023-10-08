package model.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ArcType {
	DEPENDENCY(0),
	EXTENSION(1),
	IMPLEMENTATION(2),
	AGGREGATION(3),
	ASSOCIATION(4);

	private static final Map<String, Integer> DEGREE_MAP;

	private final int degree;

	static {
		Map<String, Integer> map = new HashMap<>();
		for (ArcType arcType: ArcType.values()) {
			map.put(arcType.toString().toLowerCase(), arcType.degree);
		}
		DEGREE_MAP = Collections.unmodifiableMap(map);
	}

	ArcType(int degree) {
		this.degree = degree;
	}

	public static Integer get(String arcTypeName) {
		return DEGREE_MAP.get(arcTypeName);
	}

	public int getDegree() {
		return degree;
	}
}

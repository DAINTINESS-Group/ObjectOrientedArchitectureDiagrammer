package model.graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public enum ArcType implements Comparator<ArcType>
{
	DEPENDENCY	  (0),
	EXTENSION	  (1),
	IMPLEMENTATION(2),
	AGGREGATION   (3),
	ASSOCIATION	  (4);

	private static final Map<String, ArcType> DEGREE_MAP;

	private final int degree;

	static {
		Map<String, ArcType> map = new HashMap<>();
		for (ArcType arcType: ArcType.values()) {
			map.put(arcType.toString().toLowerCase(), arcType);
		}
		DEGREE_MAP = Collections.unmodifiableMap(map);
	}

	ArcType(int degree) {
		this.degree = degree;
	}

	public static ArcType get(String arcTypeName) {
		return DEGREE_MAP.get(arcTypeName.toLowerCase().trim());
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

	@Override
	public int compare(ArcType o1, ArcType o2)
	{
		return Integer.compare(o1.degree, o2.degree);
	}
}

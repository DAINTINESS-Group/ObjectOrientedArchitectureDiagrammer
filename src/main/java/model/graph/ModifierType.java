package model.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ModifierType {
	PRIVATE(),
	PUBLIC(),
	PROTECTED(),
	PACKAGE_PRIVATE();

	private static final Map<String, ModifierType> STRING_MODIFIER_TYPE_MAP;

	static {
		Map<String, ModifierType> temp = new HashMap<>();
		for (ModifierType modifierType: ModifierType.values()) {
			temp.put(modifierType.toString().toLowerCase(), modifierType);
		}
		STRING_MODIFIER_TYPE_MAP = Collections.unmodifiableMap(temp);
	}
	public static ModifierType get(String modifier) {
		return STRING_MODIFIER_TYPE_MAP.get(modifier);
	}
}

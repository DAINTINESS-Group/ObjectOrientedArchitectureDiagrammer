package model.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ModifierType {
	PRIVATE,
	PUBLIC,
	PROTECTED,
	PACKAGE_PRIVATE;

	private static final Map<String, ModifierType> MODIFIER_TYPE;

	static {
		Map<String, ModifierType> temp = new HashMap<>();
		for (ModifierType modifierType: ModifierType.values()) {
			temp.put(modifierType.toString().toLowerCase(), modifierType);
		}
		MODIFIER_TYPE = Collections.unmodifiableMap(temp);
	}

	public static ModifierType get(String modifier) {
		return MODIFIER_TYPE.get(modifier.toLowerCase().trim());
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}

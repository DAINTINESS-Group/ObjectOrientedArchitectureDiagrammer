package parser.tree;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ModifierType {
	PRIVATE,
	PUBLIC,
	PROTECTED,
	PACKAGE_PRIVATE;

	public static final Map<String, ModifierType> MODIFIER_TYPE;

	static {
		Map<String, ModifierType> map = new HashMap<>();
		for (ModifierType modifierType: ModifierType.values()) {
			map.put(modifierType.toString().toLowerCase(), modifierType);
		}
		MODIFIER_TYPE = Collections.unmodifiableMap(map);
	}

	public static ModifierType get(String modifierType) {
		return MODIFIER_TYPE.get(modifierType.toLowerCase().trim());
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}

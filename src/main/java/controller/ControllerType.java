package controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ControllerType {
	UML;

	public static final Map<String, ControllerType> CONTROLLER_TYPE;

	static {
		Map<String, ControllerType> map = new HashMap<>();
		for (ControllerType controllerType: ControllerType.values()) {
			map.put(controllerType.toString().toLowerCase(), controllerType);
		}
		CONTROLLER_TYPE = Collections.unmodifiableMap(map);
	}

	public static ControllerType get(String controllerType) {
		return CONTROLLER_TYPE.get(controllerType.toLowerCase());
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}

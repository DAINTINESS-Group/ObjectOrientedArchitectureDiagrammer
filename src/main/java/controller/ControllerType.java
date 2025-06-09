package controller;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ControllerType {
    UML;

    public static final Map<String, ControllerType> CONTROLLER_TYPE;

    static {
        CONTROLLER_TYPE =
                Arrays.stream(ControllerType.values())
                        .collect(
                                Collectors.toMap(
                                        ControllerType::toString,
                                        controllerType -> controllerType));
    }

    public static ControllerType get(String controllerType) {
        return CONTROLLER_TYPE.get(controllerType.toLowerCase());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

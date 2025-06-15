package model.graph;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import proguard.classfile.AccessConstants;

public enum ModifierType {
    PRIVATE,
    PUBLIC,
    PROTECTED,
    PACKAGE_PRIVATE;

    private static final Map<String, ModifierType> MODIFIER_TYPE;

    static {
        MODIFIER_TYPE =
                Arrays.stream(ModifierType.values())
                        .collect(Collectors.toMap(ModifierType::toString, it -> it));
    }

    public static ModifierType get(String modifier) {
        return MODIFIER_TYPE.get(modifier.toLowerCase().trim());
    }

    public static ModifierType from(int accessFlags) {
        if ((AccessConstants.PUBLIC & accessFlags) != 0) {
            return ModifierType.PUBLIC;
        }

        if ((AccessConstants.PRIVATE & accessFlags) != 0) {
            return ModifierType.PRIVATE;
        }

        if ((AccessConstants.PROTECTED & accessFlags) != 0) {
            return ModifierType.PROTECTED;
        }

        return ModifierType.PACKAGE_PRIVATE;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

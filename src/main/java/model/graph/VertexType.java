package model.graph;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import proguard.classfile.AccessConstants;
import proguard.classfile.Clazz;

public enum VertexType {
    CLASS,
    INTERFACE,
    ENUM,
    PACKAGE;
    // TODO: Records.

    public static final Map<String, VertexType> VERTEX_TYPE;

    static {
        VERTEX_TYPE =
                Arrays.stream(VertexType.values())
                        .collect(Collectors.toMap(VertexType::toString, it -> it));
    }

    public static VertexType get(String vertexType) {
        return VERTEX_TYPE.get(vertexType.toLowerCase().trim());
    }

    public static VertexType from(Clazz clazz) {
        int accessFlags = clazz.getAccessFlags();

        if ((accessFlags & AccessConstants.INTERFACE) != 0) {
            return VertexType.INTERFACE;
        } else if ((accessFlags & AccessConstants.ENUM) != 0) {
            return VertexType.ENUM;
        }

        return VertexType.CLASS;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

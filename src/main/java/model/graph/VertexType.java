package model.graph;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum VertexType {
    CLASS,
    INTERFACE,
    ENUM,
    PACKAGE;

    public static final Map<String, VertexType> VERTEX_TYPE;

    static {
        VERTEX_TYPE =
                Arrays.stream(VertexType.values())
                        .collect(Collectors.toMap(VertexType::toString, it -> it));
    }

    public static VertexType get(String vertexType) {
        return VERTEX_TYPE.get(vertexType.toLowerCase().trim());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

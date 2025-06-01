package manager;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum DiagramType {
    CLASS,
    PACKAGE;

    public static final Map<String, DiagramType> DIAGRAM_TYPE =
            Arrays.stream(DiagramType.values())
                    .collect(Collectors.toMap(DiagramType::toString, it -> it));

    public static DiagramType get(String diagramType) {
        return DIAGRAM_TYPE.get(diagramType.toLowerCase().trim());
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

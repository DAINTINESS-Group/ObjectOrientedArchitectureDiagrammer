package manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DiagramType
{
    CLASS,
    PACKAGE;

    public static final Map<String, DiagramType> DIAGRAM_TYPE;

    static
    {
        Map<String, DiagramType> map = new HashMap<>();
        for (DiagramType diagramType : DiagramType.values())
        {
            map.put(diagramType.toString().toLowerCase(), diagramType);
        }

        DIAGRAM_TYPE = Collections.unmodifiableMap(map);
    }

    public static DiagramType get(String diagramType)
    {
        return DIAGRAM_TYPE.get(diagramType.toLowerCase().trim());
    }


    @Override
    public String toString()
    {
        return super.toString().toLowerCase();
    }

}

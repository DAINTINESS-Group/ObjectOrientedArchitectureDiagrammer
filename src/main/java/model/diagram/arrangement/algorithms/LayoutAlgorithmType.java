package model.diagram.arrangement.algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum LayoutAlgorithmType
{
    SUGIYAMA,
    FRUCHTERMAN_REINGOLD,
    ADVANCED_FRUCHTERMAN_REINGOLD,
    SPRING,
    ADVANCED_SPRING,
    KAMADA_KAWAI;

    private static final Map<String, LayoutAlgorithmType> ALGORITHM_TYPE_MAP;

    static
    {
        Map<String, LayoutAlgorithmType> map = new HashMap<>();
        for (LayoutAlgorithmType layoutAlgorithmType : LayoutAlgorithmType.values())
        {
            map.put(layoutAlgorithmType.toString().toLowerCase(), layoutAlgorithmType);
        }
        ALGORITHM_TYPE_MAP = Collections.unmodifiableMap(map);
    }

    public static LayoutAlgorithmType get(String algorithmType)
    {
        return ALGORITHM_TYPE_MAP.get(algorithmType.toLowerCase());
    }

}

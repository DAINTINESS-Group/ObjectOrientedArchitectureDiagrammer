package model.diagram.arrangement.algorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, LayoutAlgorithmType> map = Arrays.stream(LayoutAlgorithmType.values())
            .collect(Collectors.toMap(LayoutAlgorithmType::toString, it -> it));

        ALGORITHM_TYPE_MAP = Collections.unmodifiableMap(map);
    }

    public static LayoutAlgorithmType get(String algorithmType)
    {
        return ALGORITHM_TYPE_MAP.get(algorithmType);
    }

    @Override
    public String toString()
    {
        return super.toString().toLowerCase();
    }
}

package model.diagram.arrangement.geometry;

import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class DiagramGeometry
{

    private final static int MIN_X_WINDOW_VALUE = 25;
    private final static int MIN_Y_WINDOW_VALUE = 25;

    private final Map<String, Pair<Double, Double>> geometryMap;


    public DiagramGeometry()
    {
        geometryMap = new HashMap<>();
    }


    public void addGeometry(GeometryNode key, double x, double y)
    {
        geometryMap.put(key.nodeName(), new Pair<>(x, y));
    }


    public Pair<Double, Double> getVertexGeometry(String stringKey)
    {
        return geometryMap.get(stringKey);
    }


    public boolean containsKey(String stringKey)
    {
        return geometryMap.containsKey(stringKey);
    }


    public void correctPositions(double x, double y)
    {
        // We store minX and minY coordinates of the system, in order to bring the graph
        // to match the left side and upper side of the window.
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double xDifference;
        double yDifference;
        for (Map.Entry<String, Pair<Double, Double>> entry : geometryMap.entrySet())
        {
            double newX = entry.getValue().getValue0() + x;
            double newY = entry.getValue().getValue1() + y;
            geometryMap.put(entry.getKey(),
                            new Pair<>(entry.getValue().getValue0() + x,
                                       entry.getValue().getValue1() + y));
            minX = Math.min(minX, newX);
            minY = Math.min(minY, newY);
        }
        xDifference = minX - MIN_X_WINDOW_VALUE;
        yDifference = minY - MIN_Y_WINDOW_VALUE;
        geometryMap.replaceAll((k, v) -> new Pair<>(v.getValue0() - xDifference, v.getValue1() - yDifference));
    }

}

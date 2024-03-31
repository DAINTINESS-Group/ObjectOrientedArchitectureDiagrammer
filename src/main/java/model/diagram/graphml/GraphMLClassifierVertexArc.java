package model.diagram.graphml;

import model.diagram.ClassDiagram;
import model.graph.Arc;
import model.graph.ClassifierVertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GraphMLClassifierVertexArc
{

    private static final int EDGE_TYPE         = 0;
    private static final int EDGES_SOURCE_TYPE = 1;
    private static final int EDGES_TARGET_TYPE = 2;

    private final StringBuilder graphMLBuffer;
    private final ClassDiagram  classDiagram;


    public GraphMLClassifierVertexArc(ClassDiagram classDiagram)
    {
        this.classDiagram = classDiagram;
        graphMLBuffer = new StringBuilder();
    }


    public StringBuilder convertSinkVertexArc()
    {
        List<Arc<ClassifierVertex>> arcs = new ArrayList<>();
        for (Set<Arc<ClassifierVertex>> arcSet : classDiagram.getDiagram().values())
        {
            arcs.addAll(arcSet);
        }

        int edgeId = 0;
        for (Arc<ClassifierVertex> arc : arcs)
        {
            graphMLBuffer.append(GraphMLSyntax.getInstance().getGraphMLSinkVertexArcSyntax(getEdgesProperties(arc, edgeId)));
            edgeId++;
        }
        return graphMLBuffer;
    }


    private List<String> getEdgesProperties(Arc<ClassifierVertex> relationship,
                                            int                   edgeId)
    {
        return Arrays.asList(String.valueOf(edgeId), String.valueOf(classDiagram.getGraphNodes().get(relationship.sourceVertex())),
                             String.valueOf(classDiagram.getGraphNodes().get(relationship.targetVertex())), identifyEdgeType(relationship).get(EDGE_TYPE),
                             identifyEdgeType(relationship).get(EDGES_SOURCE_TYPE), identifyEdgeType(relationship).get(EDGES_TARGET_TYPE));
    }


    private List<String> identifyEdgeType(Arc<ClassifierVertex> relationship)
    {
        return switch (relationship.arcType())
        {
            case DEPENDENCY  -> Arrays.asList("dashed", "none", "plain");
            case AGGREGATION -> Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION -> Arrays.asList("line", "none", "standard");
            case EXTENSION   -> Arrays.asList("line", "none", "white_delta");
            default          -> Arrays.asList("dashed", "none", "white_delta");
        };
    }

}

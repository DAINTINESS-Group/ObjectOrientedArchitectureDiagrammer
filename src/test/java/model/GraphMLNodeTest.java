package model;

import manager.ClassDiagramManager;
import manager.PackageDiagramManager;
import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.graphml.GraphMLPackageEdge;
import model.diagram.graphml.GraphMLPackageNode;
import model.graph.Arc;
import model.graph.SinkVertex;
import model.graph.Vertex;
import model.graph.VertexType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphMLNodeTest {

    Path currentDirectory = Path.of(".");

    @Test
    void populateGraphMLNodesTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
                    "CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
                    "LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));

            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();

            List<String> l1 = new ArrayList<>();
            List<String> l2 = new ArrayList<>();
            assertEquals(sourceProject.getVertices().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getSinkVertices().size(), graphNodes.size());

            Iterator<SinkVertex> iter1 = sourceProject.getVertices().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getSinkVertices().iterator();
            Iterator<Map.Entry<SinkVertex, Integer>> iter2 = graphNodes.entrySet().iterator();
            while(iter1.hasNext() || iter2.hasNext()) {
                SinkVertex e1 = iter1.next();
                Map.Entry<SinkVertex, Integer> e2 = iter2.next();
                l1.add(e1.getName());
                l2.add(e2.getKey().getName());
            }
            Collections.sort(l1);
            Collections.sort(l2);
            assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void convertNodesToGraphMLTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
                    "CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
                    "LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));
            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            GraphMLLeafNode graphMLLeafNode = new GraphMLLeafNode(graphNodes, Map.ofEntries(
                    Map.entry(0, Arrays.asList(10.0, 10.0)),
                    Map.entry(1, Arrays.asList(10.0, 10.0)),
                    Map.entry(2, Arrays.asList(10.0, 10.0)),
                    Map.entry(3, Arrays.asList(10.0, 10.0)),
                    Map.entry(4, Arrays.asList(10.0, 10.0)),
                    Map.entry(5, Arrays.asList(10.0, 10.0)),
                    Map.entry(6, Arrays.asList(10.0, 10.0)),
                    Map.entry(7, Arrays.asList(10.0, 10.0)),
                    Map.entry(8, Arrays.asList(10.0, 10.0)),
                    Map.entry(9, Arrays.asList(10.0, 10.0)),
                    Map.entry(10, Arrays.asList(10.0, 10.0))
            ));
            StringBuilder actual = graphMLLeafNode.convertLeafNode();

            StringBuilder expected = new StringBuilder();
            for (SinkVertex leafNode: graphNodes.keySet()) {
                expected.append(String.format("    <node id=\"n%s\">\n" +
                                "      <data key=\"d4\" xml:space=\"preserve\"/>\n" +
                                "      <data key=\"d5\"/>\n" +
                                "      <data key=\"d6\">\n" +
                                "        <y:UMLClassNode>\n" +
                                "          <y:Geometry height=\"100.0\" width=\"150.0\" x=\"%s\" y=\"%s\"/>\n" +
                                "          <y:Fill color=\"%s\" transparent=\"false\"/>\n" +
                                "          <y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>\n" +
                                "          <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"13\" fontStyle=\"bold\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"19.92626953125\" horizontalTextPosition=\"center\" iconTextGap=\"4\" modelName=\"custom\" textColor=\"#000000\" verticalTextPosition=\"bottom\" visible=\"true\" width=\"79.14990234375\" x=\"10.425048828125\" xml:space=\"preserve\" y=\"3.0\">%s<y:LabelModel><y:SmartNodeLabelModel distance=\"4.0\"/></y:LabelModel><y:ModelParameter><y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/></y:ModelParameter></y:NodeLabel>\n" +
                                "          <y:UML clipContent=\"true\" constraint=\"\" hasDetailsColor=\"false\" omitDetails=\"false\" stereotype=\"\" use3DEffect=\"true\">\n" +
                                "            <y:AttributeLabel xml:space=\"preserve\">%s</y:AttributeLabel>\n" +
                                "            <y:MethodLabel xml:space=\"preserve\">%s</y:MethodLabel>\n" +
                                "          </y:UML>\n" +
                                "        </y:UMLClassNode>\n" +
                                "      </data>\n" +
                                "    </node>\n", graphNodes.get(leafNode), 10.0, 10.0, getNodesColor(leafNode), leafNode.getName(),
                        getNodesFields(leafNode), getNodesMethods(leafNode)));
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void populateGraphMLEdgesTest() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
                    "CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
                    "LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();
            List<Arc<SinkVertex>> relationships = new ArrayList<>();

            sourceProject.getVertices().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getSinkVertices()
                    .forEach(sinkVertex -> relationships.addAll(sinkVertex.getArcs()));

            for (Map.Entry<Arc<SinkVertex>, Integer> e: graphEdges.entrySet()) {
                Arc<SinkVertex> arc = relationships.stream().filter(sinkVertexArc ->
                        sinkVertexArc.getSourceVertex().getName().equals(e.getKey().getSourceVertex().getName()) &&
                        sinkVertexArc.getTargetVertex().getName().equals(e.getKey().getTargetVertex().getName()))
                    .findFirst().orElseGet(Assertions::fail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isRelationshipBetweenSamePackages(Map<Path, Vertex> vertices, SinkVertex sinkVertex) throws IOException {
        Optional<SinkVertex> any = vertices.get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                        "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getSinkVertices().stream()
                .filter(sinkVertex1 -> sinkVertex1.getName().equals(sinkVertex.getName())).findAny();
        return any.isEmpty();
    }

    @Test
    void convertEdgesToGraphML() {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            SourceProject sourceProject = classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.createDiagram(List.of("AddLatexCommand", "ChangeVersionsStrategyCommand", "Command", "CommandFactory",
                    "CreateCommand", "DisableVersionsManagementCommand", "EditCommand", "EnableVersionsManagementCommand",
                    "LoadCommand", "RollbackToPreviousVersionCommand", "SaveCommand"));
            Map<SinkVertex, Integer> graphNodes = classDiagramManager.getDiagram().getGraphNodes();
            Map<Arc<SinkVertex>, Integer> graphEdges = classDiagramManager.getDiagram().getGraphEdges();

            GraphMLLeafEdge graphMLLeafEdge = new GraphMLLeafEdge(graphNodes);
            StringBuilder actual = graphMLLeafEdge.convertLeafEdge(graphEdges);

            StringBuilder expected = new StringBuilder();
            List<Arc<SinkVertex>> relationships = new ArrayList<>();
            for (SinkVertex l: sourceProject.getVertices().get(Paths.get(currentDirectory.toRealPath().normalize().toString(),
                    "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands")).getSinkVertices()) {
                for (Arc<SinkVertex> relationship: l.getArcs()) {
                    if (isRelationshipBetweenSamePackages(sourceProject.getVertices(), relationship.getTargetVertex())) {
                        continue;
                    }
                    relationships.add(relationship);
                }
            }
            for (Map.Entry<Arc<SinkVertex>, Integer> e: graphEdges.entrySet()) {
                String edgesStart = e.getKey().getSourceVertex().getName();
                String edgesEnd = e.getKey().getTargetVertex().getName();
                for (Arc<SinkVertex> relationship: relationships) {
                    if (relationship.getSourceVertex().getName().equals(edgesStart) && relationship.getTargetVertex().getName().equals(edgesEnd)) {
                        expected.append(String.format("<edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
                                        "      <data key=\"d10\">\n" +
                                        "        <y:PolyLineEdge>\n" +
                                        "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                                        "          <y:LineStyle color=\"#000000\" type=\"%s\" width=\"1.0\"/>\n" +
                                        "          <y:Arrows source=\"%s\" target=\"%s\"/>\n" +
                                        "          <y:BendStyle smoothed=\"false\"/>\n" +
                                        "        </y:PolyLineEdge>\n" +
                                        "      </data>\n" +
                                        "    </edge>\n", e.getValue(), graphNodes.get(e.getKey().getSourceVertex()),
                                graphNodes.get(e.getKey().getTargetVertex()), getEdgesDescription(relationship).get(0),
                                getEdgesDescription(relationship).get(1),getEdgesDescription(relationship).get(2)));
                    }
                }
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void populateGraphMLPackageNodeTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.createDiagram(List.of(
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));
            Map<Vertex, Integer> graphNodes = packageDiagramManager.getDiagram().getGraphNodes();

            Map<Path, Vertex> packageNodes = sourceProject.getVertices();
            packageNodes.remove(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            assertEquals(packageNodes.size(), graphNodes.size());
            Iterator<Map.Entry<Path, Vertex>> iter1 = packageNodes.entrySet().iterator();
            Iterator<Map.Entry<Vertex, Integer>> iter2 = graphNodes.entrySet().iterator();

            List<String> l1 = new ArrayList<>();
            List<String> l2 = new ArrayList<>();
            while (iter1.hasNext() || iter2.hasNext()) {
                Map.Entry<Path, Vertex> e1 = iter1.next();
                Map.Entry<Vertex, Integer> e2 = iter2.next();
                l1.add(e1.getValue().getName());
                l2.add(e2.getKey().getName());
            }
            Collections.sort(l1);
            Collections.sort(l2);
            assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void convertPackageNodesToGraphMLTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.createDiagram(List.of(
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));
            Map<Vertex, Integer> graphNodes = packageDiagramManager.getDiagram().getGraphNodes();

            GraphMLPackageNode graphMLPackageNode = new GraphMLPackageNode(graphNodes, Map.ofEntries(
                    Map.entry(0, Arrays.asList(10.0, 10.0)),
                    Map.entry(1, Arrays.asList(10.0, 10.0)),
                    Map.entry(2, Arrays.asList(10.0, 10.0)),
                    Map.entry(3, Arrays.asList(10.0, 10.0)),
                    Map.entry(4, Arrays.asList(10.0, 10.0)),
                    Map.entry(5, Arrays.asList(10.0, 10.0))
            ));
            StringBuilder actual = graphMLPackageNode.convertPackageNode();

            StringBuilder expected = new StringBuilder();
            for (Vertex packageNode : graphNodes.keySet()) {
                expected.append(String.format("    <node id=\"n%s\">\n" +
                                "      <data key=\"d4\" xml:space=\"preserve\"/>\n" +
                                "      <data key=\"d6\">\n" +
                                "        <y:GenericNode configuration=\"ShinyPlateNode3\">\n" +
                                "          <y:Geometry height=\"52.0\" width=\"127.0\" x=\"%s\" y=\"%s\"/>\n" +
                                "          <y:Fill color=\"#FF9900\" color2=\"#FFCC00\" transparent=\"false\"/>\n" +
                                "          <y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>\n" +
                                "          <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"18.701171875\" horizontalTextPosition=\"center\" iconTextGap=\"0\" modelName=\"custom\" textColor=\"#000000\" verticalTextPosition=\"bottom\" visible=\"true\" width=\"57.373046875\" x=\"34.8134765625\" xml:space=\"preserve\" y=\"16.6494140625\">%s<y:LabelModel><y:SmartNodeLabelModel distance=\"4.0\"/></y:LabelModel><y:ModelParameter><y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/></y:ModelParameter></y:NodeLabel>\n" +
                                "        </y:GenericNode>\n" +
                                "      </data>\n" +
                                "    </node>\n", graphNodes.get(packageNode), 10.0,
                        10.0, packageNode.getName()));
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void populateGraphMLPackageEdgesTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.createDiagram(List.of(
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));
            Map<Arc<Vertex>, Integer> graphEdges = packageDiagramManager.getDiagram().getGraphEdges();

            List<Arc<Vertex>> relationships = new ArrayList<>();
            for (Vertex packageNode : sourceProject.getVertices().values()) {
                relationships.addAll(packageNode.getArcs());
            }
            for (Map.Entry<Arc<Vertex>, Integer> e : graphEdges.entrySet()) {
                String edgesStart = e.getKey().getSourceVertex().getName();
                String edgesEnd = e.getKey().getTargetVertex().getName();
                boolean foundBranch = false;
                for (Arc<Vertex> b : relationships) {
                    if (b.getSourceVertex().getName().equals(edgesStart) && b.getTargetVertex().getName().equals(edgesEnd)) {
                        foundBranch = true;
                    }
                }
                assertTrue(foundBranch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void convertPackageEdgesToGraphMLTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.createDiagram(List.of(
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\view",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\model\\strategies",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller\\commands",
                    currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src\\controller"));
            Map<Vertex, Integer> graphNodes = packageDiagramManager.getDiagram().getGraphNodes();
            Map<Arc<Vertex>, Integer> graphEdges = packageDiagramManager.getDiagram().getGraphEdges();
            GraphMLPackageEdge graphMLPackageEdge = new GraphMLPackageEdge(graphNodes);
            StringBuilder actual = graphMLPackageEdge.convertPackageEdge(graphEdges);

            StringBuilder expected = new StringBuilder();
            List<Arc<Vertex>> relationships = new ArrayList<>();
            for (Vertex packageNode : sourceProject.getVertices().values()) {
                relationships.addAll(packageNode.getArcs());
            }

            for (Map.Entry<Arc<Vertex>, Integer> e : graphEdges.entrySet()) {
                String edgesStart = e.getKey().getSourceVertex().getName();
                String edgesEnd = e.getKey().getTargetVertex().getName();
                for (Arc<Vertex> relationship : relationships) {
                    if (relationship.getSourceVertex().getName().equals(edgesStart) && relationship.getTargetVertex().getName().equals(edgesEnd)) {
                        expected.append(String.format("    <edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
                                        "      <data key=\"d9\"/>\n" +
                                        "      <data key=\"d10\">\n" +
                                        "        <y:PolyLineEdge>\n" +
                                        "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                                        "          <y:LineStyle color=\"#000000\" type=\"dashed\" width=\"1.0\"/>\n" +
                                        "          <y:Arrows source=\"none\" target=\"plain\"/>\n" +
                                        "          <y:BendStyle smoothed=\"false\"/>\n" +
                                        "        </y:PolyLineEdge>\n" +
                                        "      </data>\n" +
                                        "    </edge>", e.getValue(), graphNodes.get(e.getKey().getSourceVertex()),
                                graphNodes.get(e.getKey().getTargetVertex())));
                    }
                }
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<String> getEdgesDescription(Arc<SinkVertex> relationship) {
        return Arrays.asList(identifyEdgeType(relationship).get(0),
                identifyEdgeType(relationship).get(1), identifyEdgeType(relationship).get(2));
    }
    private List<String> identifyEdgeType(Arc<SinkVertex> relationship){
        return switch (relationship.getArcType()) {
            case DEPENDENCY -> Arrays.asList("dashed", "none", "plain");
            case AGGREGATION -> Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION -> Arrays.asList("line", "none", "standard");
            case EXTENSION -> Arrays.asList("line", "none", "white_delta");
            default -> Arrays.asList("dashed", "none", "white_delta");
        };
    }
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (!Objects.equals(value, entry.getValue())) {
                continue;
            }
            return entry.getKey();
        }
        return null;
    }
    private String getNodesColor(SinkVertex l) {
        if (l.getVertexType().equals(VertexType.INTERFACE)) {
            return "#3366FF";
        }
        return "#FF9900";
    }

    private String getNodesMethods(SinkVertex l) {
        if ((l).getMethods().size() == 0) {
            return "";
        }
        StringBuilder methods = new StringBuilder();
        for (SinkVertex.Method method: l.getMethods()) {
            methods.append(method.getReturnType()).append(" ").append(method.getName()).append("\n");
        }
        return methods.deleteCharAt(methods.length() - 1).toString();
    }

    private String getNodesFields(SinkVertex l) {
        if (l.getFields().size() == 0) {
            return "";
        }
        StringBuilder fields = new StringBuilder();
        for (SinkVertex.Field field: l.getFields()) {
            fields.append(field.getType()).append(" ").append(field.getName()).append("\n");
        }
        return fields.deleteCharAt(fields.length() - 1).toString();
    }
}
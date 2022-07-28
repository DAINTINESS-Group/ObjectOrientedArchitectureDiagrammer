package manager.diagram;

import com.google.common.base.Preconditions;
import model.diagram.*;
import model.diagram.graphml.GraphMLLeafEdge;
import model.diagram.graphml.GraphMLLeafNode;
import model.diagram.graphml.GraphMLPackageEdge;
import model.diagram.graphml.GraphMLPackageNode;
import model.tree.*;
import org.junit.jupiter.api.Test;
import parser.Parser;
import parser.ProjectParser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphMLConverterTest {
    @Test
    void populateGraphMLNodesTest(){
        GraphNodeCollection graphMLNode = new GraphMLLeafNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        Preconditions.checkState(parser.getPackageNodes().get("commands").getLeafNodes().size() == graphMLNode.getGraphNodes().size());
        Iterator<Map.Entry<String, LeafNode>> iter1 = parser.getPackageNodes().get("commands").getLeafNodes().entrySet().iterator();
        Iterator<Map.Entry<Node, Integer>> iter2 = graphMLNode.getGraphNodes().entrySet().iterator();
        while(iter1.hasNext() || iter2.hasNext()) {
            Map.Entry<String, LeafNode> e1 = iter1.next();
            Map.Entry<Node, Integer> e2 = iter2.next();
            l1.add(e1.getValue().getName());
            l2.add(e2.getKey().getName());
        }
        Collections.sort(l1);
        Collections.sort(l2);
        assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
    }
    @Test
    void convertNodesToGraphMLTest() {
        GraphNodeCollection graphMLNode = new GraphMLLeafNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        StringBuilder expected = new StringBuilder();
        graphMLNode.convertNodesToGraphML(Map.ofEntries(
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
        for (Node leafNode: graphMLNode.getGraphNodes().keySet()) {
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
                            "    </node>\n", graphMLNode.getGraphNodes().get(leafNode), 10.0, 10.0, getNodesColor(leafNode), leafNode.getName(),
                    getNodesFields(leafNode), getNodesMethods(leafNode)));
        }
        assertEquals(expected.toString(), graphMLNode.getGraphMLBuffer());
    }

    @Test
    void populateGraphMLEdgesTest(){
        GraphNodeCollection graphMLNode = new GraphMLLeafNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        GraphMLLeafEdge graphMLEdge = new GraphMLLeafEdge();
        graphMLEdge.setGraphNodes(graphMLNode.getGraphNodes());
        graphMLEdge.populateGraphEdges(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        List<Relationship> relationships = new ArrayList<>();

        for (LeafNode l: parser.getPackageNodes().get("commands").getLeafNodes().values()) {
            for (Relationship relationship: l.getNodeRelationships()) {
                if (isRelationshipBetweenDifferentPackages(parser, relationship.getEndingNode())) {
                    continue;
                }
                relationships.add(relationship);
            }
        }
        for (Map.Entry<Relationship, Integer> e: graphMLEdge.getGraphEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            boolean foundBranch = false;
            for (Relationship b: relationships) {
                if (b.getStartingNode().getName().equals(edgesStart) && b.getEndingNode().getName().equals(edgesEnd)) {
                    foundBranch = true;
                }
            }
            assertTrue(foundBranch);
        }
    }

    private boolean isRelationshipBetweenDifferentPackages(Parser parser, Node relationship) {
        return !parser.getPackageNodes().get("commands").getLeafNodes().containsKey(relationship.getName());
    }

    @Test
    void convertEdgesToGraphML(){
        GraphNodeCollection graphMLNode = new GraphMLLeafNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        GraphMLLeafEdge graphMLEdge = new GraphMLLeafEdge();
        graphMLEdge.setGraphNodes(graphMLNode.getGraphNodes());
        graphMLEdge.populateGraphEdges(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        graphMLEdge.convertEdgesToGraphML();
        StringBuilder expected = new StringBuilder();
        List<Relationship> relationships = new ArrayList<>();
        for (LeafNode l: parser.getPackageNodes().get("commands").getLeafNodes().values()) {
            for (Relationship relationship: l.getNodeRelationships()) {
                if (isRelationshipBetweenDifferentPackages(parser, relationship.getEndingNode())) {
                    continue;
                }
                relationships.add(relationship);
            }
        }
        for (Map.Entry<Relationship, Integer> e: graphMLEdge.getGraphEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            for (Relationship relationship: relationships) {
                if (relationship.getStartingNode().getName().equals(edgesStart) && relationship.getEndingNode().getName().equals(edgesEnd)) {
                    expected.append(String.format("<edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
                                    "      <data key=\"d10\">\n" +
                                    "        <y:PolyLineEdge>\n" +
                                    "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                                    "          <y:LineStyle color=\"#000000\" type=\"%s\" width=\"1.0\"/>\n" +
                                    "          <y:Arrows source=\"%s\" target=\"%s\"/>\n" +
                                    "          <y:BendStyle smoothed=\"false\"/>\n" +
                                    "        </y:PolyLineEdge>\n" +
                                    "      </data>\n" +
                                    "    </edge>\n", e.getValue(), graphMLNode.getGraphNodes().get(e.getKey().getStartingNode()),
                            graphMLNode.getGraphNodes().get(e.getKey().getEndingNode()), getEdgesDescription(relationship).get(0),
                            getEdgesDescription(relationship).get(1),getEdgesDescription(relationship).get(2)));
                }
            }
        }
        assertEquals(expected.toString(), graphMLEdge.getGraphMLBuffer());
    }

    @Test
    void populateGraphMLPackageNodeTest(){
        GraphNodeCollection graphMLPackageNode = new GraphMLPackageNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLPackageNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().values()));

        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();

        Preconditions.checkState(parser.getPackageNodes().size() == graphMLPackageNode.getGraphNodes().size());
        Iterator<Map.Entry<String, PackageNode>> iter1 = parser.getPackageNodes().entrySet().iterator();
        Iterator<Map.Entry<Node, Integer>> iter2 = graphMLPackageNode.getGraphNodes().entrySet().iterator();

        while(iter1.hasNext() || iter2.hasNext()) {
            Map.Entry<String, PackageNode> e1 = iter1.next();
            Map.Entry<Node, Integer> e2 = iter2.next();
            l1.add(e1.getValue().getName());
            l2.add(e2.getKey().getName());
        }
        Collections.sort(l1);
        Collections.sort(l2);
        assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
    }

    @Test
    void convertPackageNodesToGraphMLTest(){
        GraphNodeCollection graphMLPackageNode = new GraphMLPackageNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLPackageNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().values()));

        StringBuilder expected = new StringBuilder();
        graphMLPackageNode.convertNodesToGraphML(Map.ofEntries(
                Map.entry(0, Arrays.asList(10.0, 10.0)),
                Map.entry(1, Arrays.asList(10.0, 10.0)),
                Map.entry(2, Arrays.asList(10.0, 10.0)),
                Map.entry(3, Arrays.asList(10.0, 10.0)),
                Map.entry(4, Arrays.asList(10.0, 10.0)),
                Map.entry(5, Arrays.asList(10.0, 10.0))
        ));

        for (Node packageNode: graphMLPackageNode.getGraphNodes().keySet()) {
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
                            "    </node>\n", graphMLPackageNode.getGraphNodes().get(packageNode), 10.0,
                    10.0, packageNode.getName()));
        }
        assertEquals(expected.toString(), graphMLPackageNode.getGraphMLBuffer());
    }

    @Test
    void populateGraphMLPackageEdgesTest() {
        GraphNodeCollection graphMLPackageNode = new GraphMLPackageNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLPackageNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().values()));

        GraphMLPackageEdge graphMLPackageEdge = new GraphMLPackageEdge();
        graphMLPackageEdge.setGraphNodes(graphMLPackageNode.getGraphNodes());
        graphMLPackageEdge.populateGraphEdges(new ArrayList<>(parser.getPackageNodes().values()));

        List<Relationship> relationships = new ArrayList<>();

        for (PackageNode packageNode: parser.getPackageNodes().values()) {
            relationships.addAll(packageNode.getNodeRelationships());
        }


        for (Map.Entry<Relationship, Integer> e: graphMLPackageEdge.getGraphEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            boolean foundBranch = false;
            for (Relationship b: relationships) {
                if (b.getStartingNode().getName().equals(edgesStart) && b.getEndingNode().getName().equals(edgesEnd)) {
                    foundBranch = true;
                }
            }
            assertTrue(foundBranch);
        }
    }

    @Test
    void convertPackageEdgesToGraphMLTest(){
        GraphNodeCollection graphMLPackageNode = new GraphMLPackageNode();
        Parser parser = new ProjectParser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        StringBuilder expected = new StringBuilder();
        graphMLPackageNode.populateGraphNodes(new ArrayList<>(parser.getPackageNodes().values()));

        GraphMLPackageEdge graphMLPackageEdge = new GraphMLPackageEdge();
        graphMLPackageEdge.setGraphNodes(graphMLPackageNode.getGraphNodes());
        graphMLPackageEdge.populateGraphEdges(new ArrayList<>(parser.getPackageNodes().values()));
        graphMLPackageEdge.convertEdgesToGraphML();

        List<Relationship> relationships = new ArrayList<>();

        for (PackageNode packageNode: parser.getPackageNodes().values()) {
            relationships.addAll(packageNode.getNodeRelationships());
        }


        for (Map.Entry<Relationship, Integer> e: graphMLPackageEdge.getGraphEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            for (Relationship relationship: relationships) {
                if (relationship.getStartingNode().getName().equals(edgesStart) && relationship.getEndingNode().getName().equals(edgesEnd)) {
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
                            "    </edge>", e.getValue(), graphMLPackageNode.getGraphNodes().get(e.getKey().getStartingNode()),
                            graphMLPackageNode.getGraphNodes().get(e.getKey().getEndingNode())));
                }
            }
        }
        assertEquals(expected.toString(), graphMLPackageEdge.getGraphMLBuffer());
    }


    private List<String> getEdgesDescription(Relationship relationship) {
        return Arrays.asList(identifyEdgeType(relationship).get(0),
                identifyEdgeType(relationship).get(1), identifyEdgeType(relationship).get(2));
    }
    private List<String> identifyEdgeType(Relationship relationship){
        switch (relationship.getRelationshipType()) {
            case DEPENDENCY:
                return Arrays.asList("dashed", "none", "plain");
            case AGGREGATION:
                return Arrays.asList("line", "white_diamond", "none");
            case ASSOCIATION:
                return Arrays.asList("line", "none", "standard");
            case EXTENSION:
                return Arrays.asList("line", "none", "white_delta");
            default:
                return Arrays.asList("dashed", "none", "white_delta");
        }
    }
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
    private String getNodesColor(Node l) {
        if (l.getType().equals(NodeType.INTERFACE)) {
            return "#3366FF";
        }
        return "#FF9900";
    }

    private String getNodesMethods(Node l) {
        if (((LeafNode)l).getMethods().size() == 0) {
            return "";
        }
        StringBuilder methods = new StringBuilder();
        for(Map.Entry<String, String> entry: ((LeafNode)l).getMethods().entrySet()) {
            methods.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return methods.deleteCharAt(methods.length() - 1).toString();
    }

    private String getNodesFields(Node l) {
        if (((LeafNode)l).getFields().size() == 0) {
            return "";
        }
        StringBuilder fields = new StringBuilder();
        for(Map.Entry<String, String> entry: ((LeafNode)l).getFields().entrySet()) {
            fields.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return fields.deleteCharAt(fields.length() - 1).toString();
    }
}
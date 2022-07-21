package manager.diagram;

import com.google.common.base.Preconditions;
import model.diagram.*;
import model.tree.LeafNode;
import model.tree.LeafNodeType;
import model.tree.PackageNode;
import model.tree.Relationship;
import org.junit.jupiter.api.Test;
import parser.PackageParser;
import parser.Parser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphMLConverterTest {
    @Test
    void populateGraphMLNodesTest(){
        GraphMLNode<LeafNode> graphMLNode = new GraphMLLeafNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();
        Preconditions.checkState(parser.getPackageNodes().get("commands").getLeafNodes().size() == graphMLNode.getGraphMLNodes().size());
        Iterator<Map.Entry<String, LeafNode>> iter1 = parser.getPackageNodes().get("commands").getLeafNodes().entrySet().iterator();
        Iterator<Map.Entry<LeafNode, Integer>> iter2 = graphMLNode.getGraphMLNodes().entrySet().iterator();
        while(iter1.hasNext() || iter2.hasNext()) {
            Map.Entry<String, LeafNode> e1 = iter1.next();
            Map.Entry<LeafNode, Integer> e2 = iter2.next();
            l1.add(e1.getValue().getName());
            System.out.println(e1.getValue().getName());
            System.out.println(e2.getKey().getName());
            l2.add(e2.getKey().getName());
        }
        Collections.sort(l1);
        Collections.sort(l2);
        System.out.println(l1.size());
        assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
    }
    @Test
    void convertNodesToGraphMLTest() {
        GraphMLNode<LeafNode> graphMLNode = new GraphMLLeafNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
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
        for (LeafNode leafNode: graphMLNode.getGraphMLNodes().keySet()) {
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
                            "    </node>\n", graphMLNode.getGraphMLNodes().get(leafNode), 10.0, 10.0, getNodesColor(leafNode), leafNode.getName(),
                    getNodesFields(leafNode), getNodesMethods(leafNode)));
        }
        assertEquals(expected.toString(), graphMLNode.getGraphMLBuffer());
    }

    @Test
    void populateGraphMLEdgesTest(){
        GraphMLNode<LeafNode> graphMLNode = new GraphMLLeafNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        GraphMLLeafEdge graphMLEdge = new GraphMLLeafEdge();
        graphMLEdge.setGraphMLNodes(graphMLNode.getGraphMLNodes());
        graphMLEdge.populateGraphMLEdges(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        List<Relationship<?>> relationships = new ArrayList<>();

        for (LeafNode l: parser.getPackageNodes().get("commands").getLeafNodes().values()) {
            for (Relationship<?> relationship: l.getLeafNodeRelationships()) {
                if (isRelationshipBetweenDifferentPackages(parser, (LeafNode) (relationship.getEndingNode()))) {
                    continue;
                }
                relationships.add(relationship);
            }
        }
        for (Map.Entry<Relationship<LeafNode>, Integer> e: graphMLEdge.getGraphMLEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            boolean foundBranch = false;
            for (Relationship<?> b: relationships) {
                if (((LeafNode)(b.getStartingNode())).getName().equals(edgesStart) && ((LeafNode)(b.getEndingNode())).getName().equals(edgesEnd)) {
                    foundBranch = true;
                }
            }
            assertTrue(foundBranch);
        }
    }

    private boolean isRelationshipBetweenDifferentPackages(PackageParser parser, LeafNode relationship) {
        return !parser.getPackageNodes().get("commands").getLeafNodes().containsKey(relationship.getName());
    }

    @Test
    void convertEdgesToGraphML(){
        GraphMLNode<LeafNode> graphMLNode = new GraphMLLeafNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        GraphMLLeafEdge graphMLEdge = new GraphMLLeafEdge();
        graphMLEdge.setGraphMLNodes(graphMLNode.getGraphMLNodes());
        graphMLEdge.populateGraphMLEdges(new ArrayList<>(parser.getPackageNodes().get("commands").getLeafNodes().values()));
        graphMLEdge.convertEdgesToGraphML();
        StringBuilder expected = new StringBuilder();
        List<Relationship<LeafNode>> relationships = new ArrayList<>();
        for (LeafNode l: parser.getPackageNodes().get("commands").getLeafNodes().values()) {
            for (Relationship<LeafNode> relationship: l.getLeafNodeRelationships()) {
                if (isRelationshipBetweenDifferentPackages(parser, relationship.getEndingNode())) {
                    continue;
                }
                relationships.add(relationship);
            }
        }
        for (Map.Entry<Relationship<LeafNode>, Integer> e: graphMLEdge.getGraphMLEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            for (Relationship<LeafNode> relationship: relationships) {
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
                                    "    </edge>\n", e.getValue(), graphMLNode.getGraphMLNodes().get(e.getKey().getStartingNode()),
                            graphMLNode.getGraphMLNodes().get(e.getKey().getEndingNode()), getEdgesDescription(relationship).get(0),
                            getEdgesDescription(relationship).get(1),getEdgesDescription(relationship).get(2)));
                }
            }
        }
        assertEquals(expected.toString(), graphMLEdge.getGraphMLBuffer());
    }

    @Test
    void populateGraphMLPackageNodeTest(){
        GraphMLNode<PackageNode> graphMLPackageNode = new GraphMLPackageNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLPackageNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().values()));

        List<String> l1 = new ArrayList<>();
        List<String> l2 = new ArrayList<>();

        Preconditions.checkState(parser.getPackageNodes().size() == graphMLPackageNode.getGraphMLNodes().size());
        Iterator<Map.Entry<String, PackageNode>> iter1 = parser.getPackageNodes().entrySet().iterator();
        Iterator<Map.Entry<PackageNode, Integer>> iter2 = graphMLPackageNode.getGraphMLNodes().entrySet().iterator();

        while(iter1.hasNext() || iter2.hasNext()) {
            Map.Entry<String, PackageNode> e1 = iter1.next();
            Map.Entry<PackageNode, Integer> e2 = iter2.next();
            l1.add(e1.getValue().getName());
            System.out.println(e1.getValue().getName());
            System.out.println(e2.getKey().getName());
            l2.add(e2.getKey().getName());
        }
        Collections.sort(l1);
        Collections.sort(l2);
        System.out.println(l1.size());
        assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
    }

    @Test
    void convertPackageNodesToGraphMLTest(){
        GraphMLNode<PackageNode> graphMLPackageNode = new GraphMLPackageNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLPackageNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().values()));

        StringBuilder expected = new StringBuilder();
        graphMLPackageNode.convertNodesToGraphML(Map.ofEntries(
                Map.entry(0, Arrays.asList(10.0, 10.0)),
                Map.entry(1, Arrays.asList(10.0, 10.0)),
                Map.entry(2, Arrays.asList(10.0, 10.0)),
                Map.entry(3, Arrays.asList(10.0, 10.0)),
                Map.entry(4, Arrays.asList(10.0, 10.0)),
                Map.entry(5, Arrays.asList(10.0, 10.0))
        ));

        for (PackageNode packageNode: graphMLPackageNode.getGraphMLNodes().keySet()) {
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
                            "    </node>\n", graphMLPackageNode.getGraphMLNodes().get(packageNode), 10.0,
                    10.0, packageNode.getName()));
        }
        assertEquals(expected.toString(), graphMLPackageNode.getGraphMLBuffer());
    }

    @Test
    void populateGraphMLPackageEdgesTest() {
        GraphMLNode<PackageNode> graphMLPackageNode = new GraphMLPackageNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLPackageNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().values()));

        GraphMLPackageEdge graphMLPackageEdge = new GraphMLPackageEdge();
        graphMLPackageEdge.setGraphMLNodes(graphMLPackageNode.getGraphMLNodes());
        graphMLPackageEdge.populateGraphMLEdges(new ArrayList<>(parser.getPackageNodes().values()));

        List<Relationship<PackageNode>> relationships = new ArrayList<>();

        for (PackageNode packageNode: parser.getPackageNodes().values()) {
            relationships.addAll(packageNode.getPackageNodeRelationships());
        }


        for (Map.Entry<Relationship<PackageNode>, Integer> e: graphMLPackageEdge.getGraphMLEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            boolean foundBranch = false;
            for (Relationship<PackageNode> b: relationships) {
                if (b.getStartingNode().getName().equals(edgesStart) && b.getEndingNode().getName().equals(edgesEnd)) {
                    foundBranch = true;
                }
            }
            assertTrue(foundBranch);
        }
    }

    @Test
    void convertPackageEdgesToGraphMLTest(){
        GraphMLNode<PackageNode> graphMLPackageNode = new GraphMLPackageNode<>();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        StringBuilder expected = new StringBuilder();
        graphMLPackageNode.populateGraphMLNodes(new ArrayList<>(parser.getPackageNodes().values()));

        GraphMLPackageEdge graphMLPackageEdge = new GraphMLPackageEdge();
        graphMLPackageEdge.setGraphMLNodes(graphMLPackageNode.getGraphMLNodes());
        graphMLPackageEdge.populateGraphMLEdges(new ArrayList<>(parser.getPackageNodes().values()));
        graphMLPackageEdge.convertEdgesToGraphML();

        List<Relationship<PackageNode>> relationships = new ArrayList<>();

        for (PackageNode packageNode: parser.getPackageNodes().values()) {
            relationships.addAll(packageNode.getPackageNodeRelationships());
        }


        for (Map.Entry<Relationship<PackageNode>, Integer> e: graphMLPackageEdge.getGraphMLEdges().entrySet()) {
            String edgesStart = e.getKey().getStartingNode().getName();
            String edgesEnd = e.getKey().getEndingNode().getName();
            for (Relationship<PackageNode> relationship: relationships) {
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
                            "    </edge>", e.getValue(), graphMLPackageNode.getGraphMLNodes().get(e.getKey().getStartingNode()),
                            graphMLPackageNode.getGraphMLNodes().get(e.getKey().getEndingNode())));
                }
            }
        }
        assertEquals(expected.toString(), graphMLPackageEdge.getGraphMLBuffer());
    }


    private List<String> getEdgesDescription(Relationship<?> branch) {
        return Arrays.asList(identifyEdgeType(branch).get(0),
                identifyEdgeType(branch).get(1), identifyEdgeType(branch).get(2));
    }
    private List<String> identifyEdgeType(Relationship<?> branch){
        switch (branch.getRelationshipType()) {
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
    private String getNodesColor(LeafNode l) {
        if (l.getType().equals(LeafNodeType.INTERFACE)) {
            return "#3366FF";
        }
        return "#FF9900";
    }

    private String getNodesMethods(LeafNode l) {
        if (l.getMethods().size() == 0) {
            return "";
        }
        StringBuilder methods = new StringBuilder();
        for(Map.Entry<String, String> entry: l.getMethods().entrySet()) {
            methods.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return methods.deleteCharAt(methods.length() - 1).toString();
    }

    private String getNodesFields(LeafNode l) {
        if (l.getFields().size() == 0) {
            return "";
        }
        StringBuilder fields = new StringBuilder();
        for(Map.Entry<String, String> entry: l.getFields().entrySet()) {
            fields.append(entry.getValue()).append(" ").append(entry.getKey()).append("\n");
        }
        return fields.deleteCharAt(fields.length() - 1).toString();
    }
}
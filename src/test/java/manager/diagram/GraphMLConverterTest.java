package manager.diagram;

import com.google.common.base.Preconditions;
import model.LeafNode;
import model.LeafNodeType;
import model.Relationship;
import org.junit.jupiter.api.Test;
import parser.PackageParser;
import parser.Parser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphMLConverterTest {
    @Test
    void populateGraphMLNodesTest(){
        GraphMLNode graphMLNode = new GraphMLNode();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes((List<LeafNode>) parser.getPackageNodes().get("commands").getLeafNodes().values());
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
        GraphMLNode graphMLNode = new GraphMLNode();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes((List<LeafNode>) parser.getPackageNodes().get("commands").getLeafNodes().values());
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
        for (LeafNode l: graphMLNode.getGraphMLNodes().keySet()) {
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
                            "    </node>\n", graphMLNode.getGraphMLNodes().get(l), 10.0, 10.0, getNodesColor(l), l.getName(),
                    getNodesFields(l), getNodesMethods(l)));
        }
        assertEquals(expected.toString(), graphMLNode.getGraphMLBuffer());
    }

    @Test
    void populateGraphMLEdgesTest(){
        GraphMLNode graphMLNode = new GraphMLNode();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes((List<LeafNode>) parser.getPackageNodes().get("commands").getLeafNodes().values());
        GraphMLEdge graphMLEdge = new GraphMLEdge();
        graphMLEdge.populateGraphMLEdges((List<LeafNode>) parser.getPackageNodes().get("commands").getLeafNodes().values(), graphMLNode.getGraphMLNodes());
        List<Relationship<?>> branches = new ArrayList<>();

        for (LeafNode l: parser.getPackageNodes().get("commands").getLeafNodes().values()) {
            for (Relationship<?> b: l.getLeafNodeRelationships()) {
                if (!parser.getPackageNodes().get("commands").getLeafNodes().containsKey(((LeafNode)(b.getEndingNode())).getName())) {
                    continue;
                }
                branches.add(b);
            }
        }
        for (Map.Entry<Relationship<?>, Integer> e: graphMLEdge.getGraphMLEdges().entrySet()) {
            String edgesStart = ((LeafNode)(e.getKey().getStartingNode())).getName();
            String edgesEnd = ((LeafNode)(e.getKey().getEndingNode())).getName();
            boolean foundBranch = false;
            for (Relationship<?> b: branches) {
                if (((LeafNode)(b.getStartingNode())).getName().equals(edgesStart) && ((LeafNode)(b.getEndingNode())).getName().equals(edgesEnd)) {
                    foundBranch = true;
                }
            }
            assertTrue(foundBranch);
        }
    }

    @Test
    void convertEdgesToGraphML(){
        GraphMLNode graphMLNode = new GraphMLNode();
        PackageParser parser = new Parser();
        parser.parseSourcePackage("src\\test\\resources\\LatexEditor\\src");
        graphMLNode.populateGraphMLNodes((List<LeafNode>) parser.getPackageNodes().get("commands").getLeafNodes().values());
        GraphMLEdge graphMLEdge = new GraphMLEdge();
        graphMLEdge.populateGraphMLEdges((List<LeafNode>) parser.getPackageNodes().get("commands").getLeafNodes().values(), graphMLNode.getGraphMLNodes());
        StringBuilder expected = new StringBuilder();
        int counter = 0;
        List<Relationship<?>> branches = new ArrayList<>();
        for (LeafNode l: parser.getPackageNodes().get("commands").getLeafNodes().values()) {
            for (Relationship<?> b: l.getLeafNodeRelationships()) {
                if (!parser.getPackageNodes().get("commands").getLeafNodes().containsKey(((LeafNode)(b.getEndingNode())).getName())) {
                    continue;
                }
                branches.add(b);
            }
        }
        for (Map.Entry<Relationship<?>, Integer> e: graphMLEdge.getGraphMLEdges().entrySet()) {
            String edgesStart = ((LeafNode)(e.getKey().getStartingNode())).getName();
            String edgesEnd = ((LeafNode)(e.getKey().getEndingNode())).getName();
            for (Relationship<?> b: branches) {
                if (((LeafNode)(b.getStartingNode())).getName().equals(edgesStart) && ((LeafNode)(b.getEndingNode())).getName().equals(edgesEnd)) {
                    expected.append(String.format("<edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
                                    "      <data key=\"d10\">\n" +
                                    "        <y:PolyLineEdge>\n" +
                                    "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                                    "          <y:LineStyle color=\"#000000\" type=\"%s\" width=\"1.0\"/>\n" +
                                    "          <y:Arrows source=\"%s\" target=\"%s\"/>\n" +
                                    "          <y:BendStyle smoothed=\"false\"/>\n" +
                                    "        </y:PolyLineEdge>\n" +
                                    "      </data>\n" +
                                    "    </edge>\n", counter, e.getKey(), e.getValue(),
                            getEdgesDescription(b).get(0), getEdgesDescription(b).get(1),getEdgesDescription(b).get(2)));
                    counter++;
                }
            }
        }
        assertEquals(expected.toString(), graphMLEdge.getGraphMLBuffer());
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
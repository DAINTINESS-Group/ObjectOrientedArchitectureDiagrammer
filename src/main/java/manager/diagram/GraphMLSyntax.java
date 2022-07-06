package manager.diagram;

import java.util.List;

public class GraphMLSyntax {

    private static GraphMLSyntax instance;

    public GraphMLSyntax(){

    }

    public String getGraphMLPrefix(){
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:java=\"http://www.yworks.com/xml/yfiles-common/1.0/java\" xmlns:sys=\"http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0\" xmlns:x=\"http://www.yworks.com/xml/yfiles-common/markup/2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xmlns:yed=\"http://www.yworks.com/xml/yed/3\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd\">\n" +
                "  <!--Created by yEd 3.22-->\n" +
                "  <key attr.name=\"Description\" attr.type=\"string\" for=\"graph\" id=\"d0\"/>\n" +
                "  <key for=\"port\" id=\"d1\" yfiles.type=\"portgraphics\"/>\n" +
                "  <key for=\"port\" id=\"d2\" yfiles.type=\"portgeometry\"/>\n" +
                "  <key for=\"port\" id=\"d3\" yfiles.type=\"portuserdata\"/>\n" +
                "  <key attr.name=\"url\" attr.type=\"string\" for=\"node\" id=\"d4\"/>\n" +
                "  <key attr.name=\"description\" attr.type=\"string\" for=\"node\" id=\"d5\"/>\n" +
                "  <key for=\"node\" id=\"d6\" yfiles.type=\"nodegraphics\"/>\n" +
                "  <key for=\"graphml\" id=\"d7\" yfiles.type=\"resources\"/>\n" +
                "  <key attr.name=\"url\" attr.type=\"string\" for=\"edge\" id=\"d8\"/>\n" +
                "  <key attr.name=\"description\" attr.type=\"string\" for=\"edge\" id=\"d9\"/>\n" +
                "  <key for=\"edge\" id=\"d10\" yfiles.type=\"edgegraphics\"/>\n" +
                "  <graph edgedefault=\"directed\" id=\"G\">\n" +
                "    <data key=\"d0\" xml:space=\"preserve\"/>\n";
    }

    public String getGraphMLSuffix() {
        return "</graph>\n" +
                "  <data key=\"d7\">\n" +
                "    <y:Resources/>\n" +
                "  </data>\n" +
                "</graphml>";
    }

    public String getGraphMLNodesSyntax(List<String> nodesDescription) {
        return java.text.MessageFormat.format("    <node id=\"n{0}\">\n" +
                "      <data key=\"d4\" xml:space=\"preserve\"/>\n" +
                "      <data key=\"d5\"/>\n" +
                "      <data key=\"d6\">\n" +
                "        <y:UMLClassNode>\n" +
                "          <y:Geometry height=\"100.0\" width=\"150.0\" x=\"100.0\" y=\"100.0\"/>\n" +
                "          <y:Fill color=\"{1}\" transparent=\"false\"/>\n" +
                "          <y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>\n" +
                "          <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"13\" fontStyle=\"bold\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"19.92626953125\" horizontalTextPosition=\"center\" iconTextGap=\"4\" modelName=\"custom\" textColor=\"#000000\" verticalTextPosition=\"bottom\" visible=\"true\" width=\"79.14990234375\" x=\"10.425048828125\" xml:space=\"preserve\" y=\"3.0\">{2}<y:LabelModel><y:SmartNodeLabelModel distance=\"4.0\"/></y:LabelModel><y:ModelParameter><y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/></y:ModelParameter></y:NodeLabel>\n" +
                "          <y:UML clipContent=\"true\" constraint=\"\" hasDetailsColor=\"false\" omitDetails=\"false\" stereotype=\"\" use3DEffect=\"true\">\n" +
                "            <y:AttributeLabel xml:space=\"preserve\">{3}</y:AttributeLabel>\n" +
                "            <y:MethodLabel xml:space=\"preserve\">{4}</y:MethodLabel>\n" +
                "          </y:UML>\n" +
                "        </y:UMLClassNode>\n" +
                "      </data>\n" +
                "    </node>\n", new String[]{nodesDescription.get(0), nodesDescription.get(1), nodesDescription.get(2),
                nodesDescription.get(3), nodesDescription.get(4)});
    }

    public String getGraphMLEdgesSyntax(List<String> edgesDescription) {
        return java.text.MessageFormat.format("<edge id=\"e{0}\" source=\"n{1}\" target=\"n{2}\">\n" +
                "      <data key=\"d10\">\n" +
                "        <y:PolyLineEdge>\n" +
                "          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
                "          <y:LineStyle color=\"#000000\" type=\"{3}\" width=\"1.0\"/>\n" +
                "          <y:Arrows source=\"{4}\" target=\"{5}\"/>\n" +
                "          <y:BendStyle smoothed=\"false\"/>\n" +
                "        </y:PolyLineEdge>\n" +
                "      </data>\n" +
                "    </edge>\n", new String[]{edgesDescription.get(0), edgesDescription.get(1), edgesDescription.get(2),
                edgesDescription.get(3), edgesDescription.get(4)});
    }

    public static GraphMLSyntax getInstance(){
        if (instance == null) {
            instance = new GraphMLSyntax();
        }
        return instance;
    }

}

package model.diagram.graphml;

import java.util.List;

public class GraphMLSyntax {

	private static final int NODE_ID = 0;
	private static final int NODE_COLOR = 1;
	private static final int NODE_NAME = 2;
	private static final int NODE_FIELDS = 3;
	private static final int NODE_METHODS = 4;
	private static final int NODE_X_COORDINATE = 5;
	private static final int NODE_Y_COORDINATE = 6;
	private static final int EDGE_ID = 0;
	private static final int EDGE_SOURCE = 1;
	private static final int EDGE_TARGET = 2;
	private static final int EDGE_TYPE = 3;
	private static final int EDGES_SOURCE_TYPE = 4;
	private static final int EDGES_TARGET_TYPE = 5;
	private static final int PACKAGE_X_COORDINATE = 2;
	private static final int PACKAGE_Y_COORDINATE = 3;
	private static final int PACKAGE_NAME = 1;

	private static GraphMLSyntax instance;

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

	public String getGraphMLSinkVertexSyntax(List<String> sinkVertexDescription) {
		return String.format("    <node id=\"n%s\">\n" +
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
				"    </node>\n", sinkVertexDescription.get(NODE_ID), sinkVertexDescription.get(NODE_X_COORDINATE),
				sinkVertexDescription.get(NODE_Y_COORDINATE), sinkVertexDescription.get(NODE_COLOR), sinkVertexDescription.get(NODE_NAME),
				sinkVertexDescription.get(NODE_FIELDS), sinkVertexDescription.get(NODE_METHODS));
	}

	public String getGraphMLVertexSyntax(List<String> vertexDescription) {
		return String.format("    <node id=\"n%s\">\n" +
				"      <data key=\"d4\" xml:space=\"preserve\"/>\n" +
				"      <data key=\"d6\">\n" +
				"        <y:GenericNode configuration=\"ShinyPlateNode3\">\n" +
				"          <y:Geometry height=\"52.0\" width=\"127.0\" x=\"%s\" y=\"%s\"/>\n" +
				"          <y:Fill color=\"#FF9900\" color2=\"#FFCC00\" transparent=\"false\"/>\n" +
				"          <y:BorderStyle color=\"#000000\" type=\"line\" width=\"1.0\"/>\n" +
				"          <y:NodeLabel alignment=\"center\" autoSizePolicy=\"content\" fontFamily=\"Dialog\" fontSize=\"12\" fontStyle=\"plain\" hasBackgroundColor=\"false\" hasLineColor=\"false\" height=\"18.701171875\" horizontalTextPosition=\"center\" iconTextGap=\"0\" modelName=\"custom\" textColor=\"#000000\" verticalTextPosition=\"bottom\" visible=\"true\" width=\"57.373046875\" x=\"34.8134765625\" xml:space=\"preserve\" y=\"16.6494140625\">%s<y:LabelModel><y:SmartNodeLabelModel distance=\"4.0\"/></y:LabelModel><y:ModelParameter><y:SmartNodeLabelModelParameter labelRatioX=\"0.0\" labelRatioY=\"0.0\" nodeRatioX=\"0.0\" nodeRatioY=\"0.0\" offsetX=\"0.0\" offsetY=\"0.0\" upX=\"0.0\" upY=\"-1.0\"/></y:ModelParameter></y:NodeLabel>\n" +
				"        </y:GenericNode>\n" +
				"      </data>\n" +
				"    </node>\n", vertexDescription.get(NODE_ID), vertexDescription.get(PACKAGE_X_COORDINATE),
				vertexDescription.get(PACKAGE_Y_COORDINATE), vertexDescription.get(PACKAGE_NAME));
	}

	public String getGraphMLSinkVertexArcSyntax(List<String> sinkVertexArcDescription) {
		return String.format("<edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
				"      <data key=\"d10\">\n" +
				"        <y:PolyLineEdge>\n" +
				"          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
				"          <y:LineStyle color=\"#000000\" type=\"%s\" width=\"1.0\"/>\n" +
				"          <y:Arrows source=\"%s\" target=\"%s\"/>\n" +
				"          <y:BendStyle smoothed=\"false\"/>\n" +
				"        </y:PolyLineEdge>\n" +
				"      </data>\n" +
				"    </edge>\n", sinkVertexArcDescription.get(EDGE_ID), sinkVertexArcDescription.get(EDGE_SOURCE), sinkVertexArcDescription.get(EDGE_TARGET),
				sinkVertexArcDescription.get(EDGE_TYPE), sinkVertexArcDescription.get(EDGES_SOURCE_TYPE), sinkVertexArcDescription.get(EDGES_TARGET_TYPE));
	}

	public String getGraphMLVertexArcSyntax(List<String> vertexArcDescription) {
		return String.format("    <edge id=\"e%s\" source=\"n%s\" target=\"n%s\">\n" +
				"      <data key=\"d9\"/>\n" +
				"      <data key=\"d10\">\n" +
				"        <y:PolyLineEdge>\n" +
				"          <y:Path sx=\"0.0\" sy=\"0.0\" tx=\"0.0\" ty=\"0.0\"/>\n" +
				"          <y:LineStyle color=\"#000000\" type=\"dashed\" width=\"1.0\"/>\n" +
				"          <y:Arrows source=\"none\" target=\"plain\"/>\n" +
				"          <y:BendStyle smoothed=\"false\"/>\n" +
				"        </y:PolyLineEdge>\n" +
				"      </data>\n" +
				"    </edge>", vertexArcDescription.get(EDGE_ID), vertexArcDescription.get(EDGE_SOURCE), vertexArcDescription.get(EDGE_TARGET));
	}

	public static GraphMLSyntax getInstance(){
		if (instance == null) {
			instance = new GraphMLSyntax();
		}
		return instance;
	}

}

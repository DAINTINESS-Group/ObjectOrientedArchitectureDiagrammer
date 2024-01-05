package model.diagram.graphml;

import java.util.List;

public class GraphMLSyntax {

	private static GraphMLSyntax instance;

	private GraphMLSyntax() {}

	public String getGraphMLPrefix(){
		return """
               <?xml version="1.0" encoding="UTF-8" standalone="no"?>
               <graphml xmlns="http://graphml.graphdrawing.org/xmlns" xmlns:java="http://www.yworks.com/xml/yfiles-common/1.0/java" xmlns:sys="http://www.yworks.com/xml/yfiles-common/markup/primitives/2.0" xmlns:x="http://www.yworks.com/xml/yfiles-common/markup/2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:y="http://www.yworks.com/xml/graphml" xmlns:yed="http://www.yworks.com/xml/yed/3" xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns http://www.yworks.com/xml/schema/graphml/1.1/ygraphml.xsd">
                 <!--Created by yEd 3.22-->
                 <key attr.name="Description" attr.type="string" for="graph" id="d0"/>
                 <key for="port" id="d1" yfiles.type="portgraphics"/>
                 <key for="port" id="d2" yfiles.type="portgeometry"/>
                 <key for="port" id="d3" yfiles.type="portuserdata"/>
                 <key attr.name="url" attr.type="string" for="node" id="d4"/>
                 <key attr.name="description" attr.type="string" for="node" id="d5"/>
                 <key for="node" id="d6" yfiles.type="nodegraphics"/>
                 <key for="graphml" id="d7" yfiles.type="resources"/>
                 <key attr.name="url" attr.type="string" for="edge" id="d8"/>
                 <key attr.name="description" attr.type="string" for="edge" id="d9"/>
                 <key for="edge" id="d10" yfiles.type="edgegraphics"/>
                 <graph edgedefault="directed" id="G">
                   <data key="d0" xml:space="preserve"/>
               """;
	}

	public String getGraphMLSuffix() {
		return """
               </graph>
                 <data key="d7">
                   <y:Resources/>
                 </data>
               </graphml>
               """;
	}

	public String getGraphMLSinkVertexSyntax(List<String> sinkVertexDescription) {
		return String.format("""
                                 <node id="n%s">
                                   <data key="d4" xml:space="preserve"/>
                                   <data key="d5"/>
                                   <data key="d6">
                                     <y:UMLClassNode>
                                       <y:Geometry height="100.0" width="150.0" x="%s" y="%s"/>
                                       <y:Fill color="%s" transparent="false"/>
                                       <y:BorderStyle color="#000000" type="line" width="1.0"/>
                                       <y:NodeLabel alignment="center" autoSizePolicy="content" fontFamily="Dialog" fontSize="13" fontStyle="bold" hasBackgroundColor="false" hasLineColor="false" height="19.92626953125" horizontalTextPosition="center" iconTextGap="4" modelName="custom" textColor="#000000" verticalTextPosition="bottom" visible="true" width="79.14990234375" x="10.425048828125" xml:space="preserve" y="3.0">%s<y:LabelModel><y:SmartNodeLabelModel distance="4.0"/></y:LabelModel><y:ModelParameter><y:SmartNodeLabelModelParameter labelRatioX="0.0" labelRatioY="0.0" nodeRatioX="0.0" nodeRatioY="0.0" offsetX="0.0" offsetY="0.0" upX="0.0" upY="-1.0"/></y:ModelParameter></y:NodeLabel>
                                       <y:UML clipContent="true" constraint="" hasDetailsColor="false" omitDetails="false" stereotype="" use3DEffect="true">
                                         <y:AttributeLabel xml:space="preserve">%s</y:AttributeLabel>
                                         <y:MethodLabel xml:space="preserve">%s</y:MethodLabel>
                                       </y:UML>
                                     </y:UMLClassNode>
                                   </data>
                                 </node>
                             """,
							 sinkVertexDescription.get(GraphMLSyntaxIds.NODE_ID.getId()),
							 sinkVertexDescription.get(GraphMLSyntaxIds.NODE_X_COORDINATE.getId()),
							 sinkVertexDescription.get(GraphMLSyntaxIds.NODE_Y_COORDINATE.getId()),
							 sinkVertexDescription.get(GraphMLSyntaxIds.NODE_COLOR.getId()),
							 sinkVertexDescription.get(GraphMLSyntaxIds.NODE_NAME.getId()),
							 sinkVertexDescription.get(GraphMLSyntaxIds.NODE_FIELDS.getId()),
							 sinkVertexDescription.get(GraphMLSyntaxIds.NODE_METHODS.getId()));
	}

	public String getGraphMLVertexSyntax(List<String> vertexDescription) {
		return String.format("""
                                 <node id="n%s">
                                   <data key="d4" xml:space="preserve"/>
                                   <data key="d6">
                                     <y:GenericNode configuration="ShinyPlateNode3">
                                       <y:Geometry height="52.0" width="127.0" x="%s" y="%s"/>
                                       <y:Fill color="#FF9900" color2="#FFCC00" transparent="false"/>
                                       <y:BorderStyle color="#000000" type="line" width="1.0"/>
                                       <y:NodeLabel alignment="center" autoSizePolicy="content" fontFamily="Dialog" fontSize="12" fontStyle="plain" hasBackgroundColor="false" hasLineColor="false" height="18.701171875" horizontalTextPosition="center" iconTextGap="0" modelName="custom" textColor="#000000" verticalTextPosition="bottom" visible="true" width="57.373046875" x="34.8134765625" xml:space="preserve" y="16.6494140625">%s<y:LabelModel><y:SmartNodeLabelModel distance="4.0"/></y:LabelModel><y:ModelParameter><y:SmartNodeLabelModelParameter labelRatioX="0.0" labelRatioY="0.0" nodeRatioX="0.0" nodeRatioY="0.0" offsetX="0.0" offsetY="0.0" upX="0.0" upY="-1.0"/></y:ModelParameter></y:NodeLabel>
                                     </y:GenericNode>
                                   </data>
                                 </node>
                             """,
							 vertexDescription.get(GraphMLSyntaxIds.NODE_ID.getId()),
							 vertexDescription.get(GraphMLSyntaxIds.PACKAGE_X_COORDINATE.getId()),
							 vertexDescription.get(GraphMLSyntaxIds.PACKAGE_Y_COORDINATE.getId()),
							 vertexDescription.get(GraphMLSyntaxIds.PACKAGE_NAME.getId()));
	}

	public String getGraphMLSinkVertexArcSyntax(List<String> sinkVertexArcDescription) {
		return String.format("""
                             <edge id="e%s" source="n%s" target="n%s">
                                   <data key="d10">
                                     <y:PolyLineEdge>
                                       <y:Path sx="0.0" sy="0.0" tx="0.0" ty="0.0"/>
                                       <y:LineStyle color="#000000" type="%s" width="1.0"/>
                                       <y:Arrows source="%s" target="%s"/>
                                       <y:BendStyle smoothed="false"/>
                                     </y:PolyLineEdge>
                                   </data>
                                 </edge>
                             """,
							 sinkVertexArcDescription.get(GraphMLSyntaxIds.EDGE_ID.getId()),
							 sinkVertexArcDescription.get(GraphMLSyntaxIds.EDGE_SOURCE.getId()),
							 sinkVertexArcDescription.get(GraphMLSyntaxIds.EDGE_TARGET.getId()),
							 sinkVertexArcDescription.get(GraphMLSyntaxIds.EDGE_TYPE.getId()),
							 sinkVertexArcDescription.get(GraphMLSyntaxIds.EDGES_SOURCE_TYPE.getId()),
							 sinkVertexArcDescription.get(GraphMLSyntaxIds.EDGES_TARGET_TYPE.getId()));
	}

	public String getGraphMLVertexArcSyntax(List<String> vertexArcDescription) {
		return String.format("""
                             <edge id="e%s" source="n%s" target="n%s">
                               <data key="d9"/>
                               <data key="d10">
                                 <y:PolyLineEdge>
                                   <y:Path sx="0.0" sy="0.0" tx="0.0" ty="0.0"/>
                                   <y:LineStyle color="#000000" type="dashed" width="1.0"/>
                                   <y:Arrows source="none" target="plain"/>
                                   <y:BendStyle smoothed="false"/>
                                 </y:PolyLineEdge>
                               </data>
                             </edge>\
                         """,
							 vertexArcDescription.get(GraphMLSyntaxIds.EDGE_ID.getId()),
							 vertexArcDescription.get(GraphMLSyntaxIds.EDGE_SOURCE.getId()),
							 vertexArcDescription.get(GraphMLSyntaxIds.EDGE_TARGET.getId()));
	}

	public static GraphMLSyntax getInstance(){
		if (instance == null) {
			instance = new GraphMLSyntax();
		}
		return instance;
	}

}

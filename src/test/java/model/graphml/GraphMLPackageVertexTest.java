package model.graphml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import manager.PackageDiagramManager;
import model.diagram.graphml.GraphMLPackageVertex;
import model.graph.PackageVertex;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

class GraphMLPackageVertexTest {

    @Test
    void convertVerticesToGraphMLTest() {
        PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
        packageDiagramManager.createSourceProject(LatexEditor.SRC.path);
        packageDiagramManager.convertTreeToDiagram(
                List.of(
                        "src.view",
                        "src.model",
                        "src.model.strategies",
                        "src.controller.commands",
                        "src.controller"));
        packageDiagramManager
                .getPackageDiagram()
                .setGraphMLDiagramGeometry(
                        Map.ofEntries(
                                Map.entry(0, new Pair<>(10.0, 10.0)),
                                Map.entry(1, new Pair<>(10.0, 10.0)),
                                Map.entry(2, new Pair<>(10.0, 10.0)),
                                Map.entry(3, new Pair<>(10.0, 10.0)),
                                Map.entry(4, new Pair<>(10.0, 10.0)),
                                Map.entry(5, new Pair<>(10.0, 10.0))));
        GraphMLPackageVertex graphMLPackageVertex = new GraphMLPackageVertex();
        StringBuilder actual =
                graphMLPackageVertex.convertVertex(packageDiagramManager.getPackageDiagram());

        StringBuilder expected = new StringBuilder();
        for (PackageVertex packageNode :
                packageDiagramManager.getPackageDiagram().getGraphNodes().keySet()) {
            expected.append(
                    String.format(
                            """
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
                            packageDiagramManager
                                    .getPackageDiagram()
                                    .getGraphNodes()
                                    .get(packageNode),
                            10.0,
                            10.0,
                            packageNode.getName()));
        }
        assertEquals(expected.toString(), actual.toString());
    }
}

package model.graphml;

import manager.PackageDiagramManager;
import manager.SourceProject;
import model.diagram.graphml.GraphMLPackageVertex;
import model.graph.PackageVertex;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphMLPackageVertexTest {

    Path currentDirectory = Path.of(".");

    @Test
    void convertVerticesToGraphMLTest() {
        try {
            PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
            SourceProject sourceProject = packageDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            packageDiagramManager.convertTreeToDiagram(List.of(
                "src.view",
                "src.model",
                "src.model.strategies",
                "src.controller.commands",
                "src.controller"
            ));
            packageDiagramManager.getPackageDiagram().setGraphMLDiagramGeometry(Map.ofEntries(
                    Map.entry(0, new Pair<>(10.0, 10.0)),
                    Map.entry(1, new Pair<>(10.0, 10.0)),
                    Map.entry(2, new Pair<>(10.0, 10.0)),
                    Map.entry(3, new Pair<>(10.0, 10.0)),
                    Map.entry(4, new Pair<>(10.0, 10.0)),
                    Map.entry(5, new Pair<>(10.0, 10.0))
            ));
            GraphMLPackageVertex graphMLPackageVertex = new GraphMLPackageVertex(packageDiagramManager.getPackageDiagram());
            StringBuilder actual = graphMLPackageVertex.convertVertex();

            StringBuilder expected = new StringBuilder();
            for (PackageVertex packageNode : packageDiagramManager.getPackageDiagram().getGraphNodes().keySet()) {
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
                                "    </node>\n", packageDiagramManager.getPackageDiagram().getGraphNodes().get(packageNode), 10.0,
                        10.0, packageNode.getName()));
            }
            assertEquals(expected.toString(), actual.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
package model.graphml;

import manager.PackageDiagramManager;
import manager.SourceProject;
import model.diagram.graphml.GraphMLVertex;
import model.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphMLVertexTest {

    Path currentDirectory = Path.of(".");

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

            GraphMLVertex graphMLVertex = new GraphMLVertex(graphNodes, Map.ofEntries(
                    Map.entry(0, Arrays.asList(10.0, 10.0)),
                    Map.entry(1, Arrays.asList(10.0, 10.0)),
                    Map.entry(2, Arrays.asList(10.0, 10.0)),
                    Map.entry(3, Arrays.asList(10.0, 10.0)),
                    Map.entry(4, Arrays.asList(10.0, 10.0)),
                    Map.entry(5, Arrays.asList(10.0, 10.0))
            ));
            StringBuilder actual = graphMLVertex.convertPackageNode();

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

}
package model;

import manager.ClassDiagramManager;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.ShadowCleaner;
import model.graph.Arc;
import model.graph.ArcType;
import model.graph.ClassifierVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShadowCleanerTest {

    Path currentDirectory = Path.of(".");

    @Test
    void shadowWeakRelationshipsTest () {
        try {
            ClassDiagramManager classDiagramManager = new ClassDiagramManager();
            List<String> chosenFiles = Arrays.asList("MainWindow", "LatexEditorView", "ChooseTemplate", "LatexEditorController",
                "VersionsManager", "DocumentManager", "Document");
            classDiagramManager.createSourceProject(Paths.get(currentDirectory.toRealPath() + "\\src\\test\\resources\\LatexEditor\\src"));
            classDiagramManager.convertTreeToDiagram(chosenFiles);
            Map<ClassifierVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();

            GraphClassDiagramConverter graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
            Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> shadowedDiagram = graphClassDiagramConverter.convertGraphToClassDiagram();
            classDiagramManager.getClassDiagram().setDiagram(shadowedDiagram);
            int numberOfArcsBeforeShadowCleaner = 0;
            for (Set<Arc<ClassifierVertex>> arcs: shadowedDiagram.values()) {
                numberOfArcsBeforeShadowCleaner += arcs.size();
            }
            ShadowCleaner shadowCleaner = new ShadowCleaner(classDiagramManager.getClassDiagram());
            Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> diagram = shadowCleaner.shadowWeakRelationships();
            int numberOfArcsAfterShadowCleaner = 0;
            for (Set<Arc<ClassifierVertex>> arcs: diagram.values()) {
                numberOfArcsAfterShadowCleaner += arcs.size();
            }
            assertEquals(numberOfArcsAfterShadowCleaner + 7, numberOfArcsBeforeShadowCleaner);

            ClassifierVertex chooseTemplate = diagram.keySet().stream().filter(sinkVertex -> sinkVertex.getName().equals("ChooseTemplate")).findFirst().orElseGet(Assertions::fail);
            List<Arc<ClassifierVertex>> chooseTemplateLatexEditorViewArc = diagram.get(chooseTemplate).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().getName().equals("LatexEditorView"))
                .collect(Collectors.toList());
            assertEquals(1, chooseTemplateLatexEditorViewArc.size());
            assertEquals(ArcType.ASSOCIATION, chooseTemplateLatexEditorViewArc.get(0).getArcType());

            ClassifierVertex latexEditorView = diagram.keySet().stream().filter(sinkVertex -> sinkVertex.getName().equals("LatexEditorView")).findFirst().orElseGet(Assertions::fail);
            List<Arc<ClassifierVertex>> latexEditorViewVersionsManagerArc = diagram.get(latexEditorView).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().getName().equals("VersionsManager"))
                .collect(Collectors.toList());
        assertEquals(1, latexEditorViewVersionsManagerArc.size());
            assertEquals(ArcType.ASSOCIATION, latexEditorViewVersionsManagerArc.get(0).getArcType());

            List<Arc<ClassifierVertex>> LatexEditorViewLatexEditorControllerArc = diagram.get(latexEditorView).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().getName().equals("LatexEditorController"))
                .collect(Collectors.toList());
            assertEquals(1, LatexEditorViewLatexEditorControllerArc.size());
            assertEquals(ArcType.ASSOCIATION, LatexEditorViewLatexEditorControllerArc.get(0).getArcType());

            List<Arc<ClassifierVertex>> LatexEditorViewDocumentArc = diagram.get(latexEditorView).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().getName().equals("Document"))
                .collect(Collectors.toList());
            assertEquals(1, LatexEditorViewDocumentArc.size());
            assertEquals(ArcType.ASSOCIATION, LatexEditorViewDocumentArc.get(0).getArcType());

            ClassifierVertex mainWindow = diagram.keySet().stream().filter(sinkVertex -> sinkVertex.getName().equals("MainWindow")).findFirst().orElseGet(Assertions::fail);
            List<Arc<ClassifierVertex>> mainWindowLatexEditorViewArc = diagram.get(mainWindow).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().getName().equals("LatexEditorView"))
                .collect(Collectors.toList());
            assertEquals(1, mainWindowLatexEditorViewArc.size());
            assertEquals(ArcType.ASSOCIATION, mainWindowLatexEditorViewArc.get(0).getArcType());

            ClassifierVertex versionsManager = diagram.keySet().stream().filter(sinkVertex -> sinkVertex.getName().equals("VersionsManager")).findFirst().orElseGet(Assertions::fail);
            List<Arc<ClassifierVertex>> versionsManagerLatexEditorViewArc = diagram.get(versionsManager).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().getName().equals("LatexEditorView"))
                .collect(Collectors.toList());
            assertEquals(1, versionsManagerLatexEditorViewArc.size());
            assertEquals(ArcType.ASSOCIATION, versionsManagerLatexEditorViewArc.get(0).getArcType());

            ClassifierVertex documentManager = diagram.keySet().stream().filter(sinkVertex -> sinkVertex.getName().equals("DocumentManager")).findFirst().orElseGet(Assertions::fail);
            List<Arc<ClassifierVertex>> documentManagerDocumentArc = diagram.get(documentManager).stream()
                .filter(sinkVertexArc ->
                    sinkVertexArc.getTargetVertex().getName().equals("Document"))
                .collect(Collectors.toList());
            assertEquals(1, documentManagerDocumentArc.size());
            assertEquals(ArcType.AGGREGATION, documentManagerDocumentArc.get(0).getArcType());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

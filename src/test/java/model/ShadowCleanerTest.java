package model;

import manager.ClassDiagramManager;
import model.diagram.GraphClassDiagramConverter;
import model.diagram.ShadowCleaner;
import model.graph.Arc;
import model.graph.ClassifierVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.PathTemplate.LatexEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static model.graph.ArcType.AGGREGATION;
import static model.graph.ArcType.ASSOCIATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShadowCleanerTest
{

    @Test
    void shadowWeakRelationshipsTest()
    {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        List<String> chosenFiles = Arrays.asList("MainWindow",
                                                 "LatexEditorView",
                                                 "ChooseTemplate",
                                                 "LatexEditorController",
                                                 "VersionsManager",
                                                 "DocumentManager",
                                                 "Document");

        classDiagramManager.createSourceProject(LatexEditor.SRC.path);
        classDiagramManager.convertTreeToDiagram(chosenFiles);
        Map<ClassifierVertex, Integer> graphNodes = classDiagramManager.getClassDiagram().getGraphNodes();

        GraphClassDiagramConverter                        graphClassDiagramConverter = new GraphClassDiagramConverter(graphNodes.keySet());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> shadowedDiagram            = graphClassDiagramConverter.convertGraphToClassDiagram();
        classDiagramManager.getClassDiagram().setDiagram(shadowedDiagram);

        long numberOfArcsBeforeShadowCleaner = shadowedDiagram.values().stream()
            .mapToLong(Collection::size)
            .sum();

        ShadowCleaner                                     shadowCleaner                  = new ShadowCleaner(classDiagramManager.getClassDiagram());
        Map<ClassifierVertex, Set<Arc<ClassifierVertex>>> diagram                        = shadowCleaner.shadowWeakRelationships();

        long numberOfArcsAfterShadowCleaner = diagram.values().stream()
            .mapToLong(Collection::size)
            .sum();

        assertEquals(numberOfArcsAfterShadowCleaner + 7, numberOfArcsBeforeShadowCleaner);

        ClassifierVertex chooseTemplate = diagram.keySet().stream()
            .filter(it -> it.getName()
                .equals("ChooseTemplate"))
            .findFirst()
            .orElseGet(Assertions::fail);

        List<Arc<ClassifierVertex>> chooseTemplateLatexEditorViewArc = diagram.get(chooseTemplate).stream()
            .filter(it -> it.targetVertex().getName().equals("LatexEditorView"))
            .toList();

        assertEquals(1, chooseTemplateLatexEditorViewArc.size());
        assertEquals(ASSOCIATION, chooseTemplateLatexEditorViewArc.get(0).arcType());

        ClassifierVertex latexEditorView = diagram.keySet().stream()
            .filter(it -> it.getName().equals("LatexEditorView"))
            .findFirst()
            .orElseGet(Assertions::fail);

        List<Arc<ClassifierVertex>> latexEditorViewVersionsManagerArc = diagram.get(latexEditorView)
            .stream()
            .filter(it -> it.targetVertex().getName().equals("VersionsManager"))
            .toList();

        assertEquals(1, latexEditorViewVersionsManagerArc.size());
        assertEquals(ASSOCIATION, latexEditorViewVersionsManagerArc.get(0).arcType());

        List<Arc<ClassifierVertex>> LatexEditorViewLatexEditorControllerArc = diagram.get(latexEditorView)
            .stream()
            .filter(it -> it.targetVertex().getName().equals("LatexEditorController"))
            .toList();

        assertEquals(1, LatexEditorViewLatexEditorControllerArc.size());
        assertEquals(ASSOCIATION, LatexEditorViewLatexEditorControllerArc.get(0).arcType());

        List<Arc<ClassifierVertex>> LatexEditorViewDocumentArc = diagram.get(latexEditorView).stream()
            .filter(it -> it.targetVertex().getName().equals("Document"))
            .collect(Collectors.toCollection(ArrayList::new));

        assertEquals(1, LatexEditorViewDocumentArc.size());
        assertEquals(ASSOCIATION, LatexEditorViewDocumentArc.get(0).arcType());

        ClassifierVertex mainWindow = diagram
            .keySet()
            .stream()
            .filter(it -> it.getName().equals("MainWindow"))
            .findFirst()
            .orElseGet(Assertions::fail);

        List<Arc<ClassifierVertex>> mainWindowLatexEditorViewArc = diagram
            .get(mainWindow)
            .stream()
            .filter(it -> it.targetVertex().getName().equals("LatexEditorView"))
            .toList();

        assertEquals(1, mainWindowLatexEditorViewArc.size());
        assertEquals(ASSOCIATION, mainWindowLatexEditorViewArc.get(0).arcType());

        ClassifierVertex versionsManager = diagram.keySet().stream()
            .filter(it -> it.getName().equals("VersionsManager"))
            .findFirst()
            .orElseGet(Assertions::fail);

        List<Arc<ClassifierVertex>> versionsManagerLatexEditorViewArc = diagram.get(versionsManager).stream()
            .filter(it -> it.targetVertex().getName().equals("LatexEditorView"))
            .toList();

        assertEquals(1, versionsManagerLatexEditorViewArc.size());
        assertEquals(ASSOCIATION, versionsManagerLatexEditorViewArc.get(0).arcType());

        ClassifierVertex documentManager = diagram.keySet().stream()
            .filter(it -> it.getName().equals("DocumentManager"))
            .findFirst()
            .orElseGet(Assertions::fail);

        List<Arc<ClassifierVertex>> documentManagerDocumentArc = diagram.get(documentManager).stream()
            .filter(it -> it.targetVertex().getName().equals("Document"))
            .toList();

        assertEquals(1, documentManagerDocumentArc.size());
        assertEquals(AGGREGATION, documentManagerDocumentArc.get(0).arcType());
    }
}

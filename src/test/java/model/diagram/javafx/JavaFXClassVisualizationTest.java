package model.diagram.javafx;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.scene.control.Tooltip;
import manager.ClassDiagramManager;
import manager.SourceProject;
import model.diagram.ClassDiagram;
import model.graph.ClassifierVertex;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import utils.PathTemplate;

import javax.tools.Tool;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

class JavaFXClassVisualizationTest {

    ClassDiagram theClassDiagram;
    MockedStatic<SmartGraphFactory> mockedStatic;
    MockedStatic<ShapeFactory> mockedShapeFactory;

    @BeforeEach
    public void setup(){
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        ClassDiagram classDiagram = classDiagramManager.getClassDiagram();

        SourceProject sourceProject = new SourceProject();
        Map<Path, ClassifierVertex> vertices =
                sourceProject.createClassGraph(PathTemplate.LatexEditor.SRC.path, classDiagram);
        Set<String> chosenFilesNames =
                Set.of(
                        "AddLatexCommand",
                        "Command",
                        "VersionsManager");

        Map<ClassifierVertex, Integer> graphNodes =
                classDiagramManager.getClassDiagram().getGraphNodes();
        classDiagramManager.convertTreeToDiagram(new ArrayList<>(chosenFilesNames));
        theClassDiagram = classDiagramManager.getClassDiagram();

        mockJavaFXCalls();
        mockShapeFactory();
    }

    private void mockJavaFXCalls() {
        SmartGraphProperties mockProperties = Mockito.spy(SmartGraphProperties.class);
        Mockito.when(mockProperties.getUseEdgeTooltip()).thenReturn(false);
        Mockito.when(mockProperties.getUseVertexTooltip()).thenReturn(false);
        mockedStatic = Mockito.mockStatic(SmartGraphFactory.class);
        mockedStatic.when(() -> SmartGraphFactory.createGraphView(Mockito.any())).thenCallRealMethod();
        mockedStatic.when(SmartGraphFactory::getSmartGraphStyleURI).thenCallRealMethod();
        mockedStatic.when(SmartGraphFactory::getSmartgraphProperties).thenReturn(mockProperties);
    }

    private void mockShapeFactory(){
        mockedShapeFactory = Mockito.mockStatic(ShapeFactory.class);
        mockedShapeFactory.when(() -> ShapeFactory.create(anyString(), anyDouble(), anyDouble(), anyDouble())).thenCallRealMethod();
    }

    @AfterEach
    public void tearDown(){
        mockedStatic.close();
        mockedShapeFactory.close();
    }

    @Test
    void createGraphView() {
        assertNotNull(theClassDiagram);
        assertEquals(3, theClassDiagram.getGraphNodes().keySet().size());
        JavaFXClassVisualization classVisualization = new JavaFXClassVisualization(theClassDiagram);
        SmartGraphPanel<String, String> graphPanel = classVisualization.createGraphView();
        assertNotNull(graphPanel);
        Collection<SmartGraphVertex<String>> smartVertices = graphPanel.getSmartVertices();
        assertEquals(3, smartVertices.size());
        // verify that 3 circles are created
        mockedShapeFactory.verify(() -> ShapeFactory.create(eq("circle"), anyDouble(), anyDouble(), anyDouble()),times(3));
    }

}
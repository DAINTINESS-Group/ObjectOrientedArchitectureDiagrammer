package gr.uoi.ooad.model.diagram.javafx;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

import com.brunomnsilva.smartgraph.graphview.*;
import gr.uoi.ooad.manager.ClassDiagramManager;
import gr.uoi.ooad.manager.SourceProject;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.utils.PathTemplate;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class JavaFXClassVisualizationTest {

    ClassDiagram theClassDiagram;
    MockedStatic<SmartGraphFactory> mockedStatic;
    MockedStatic<ShapeFactory> mockedShapeFactory;

    @BeforeEach
    public void setup() {
        ClassDiagramManager classDiagramManager = new ClassDiagramManager();
        ClassDiagram classDiagram = classDiagramManager.getClassDiagram();

        SourceProject sourceProject = new SourceProject();
        Map<Path, ClassifierVertex> vertices =
                sourceProject.createClassGraph(PathTemplate.LatexEditor.SRC.path, classDiagram);
        Set<String> chosenFilesNames = Set.of("AddLatexCommand", "Command", "VersionsManager");

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
        mockedStatic
                .when(() -> SmartGraphFactory.createGraphView(Mockito.any()))
                .thenCallRealMethod();
        mockedStatic.when(SmartGraphFactory::getSmartGraphStyleURI).thenCallRealMethod();
        mockedStatic.when(SmartGraphFactory::createSmartGraphProperties).thenReturn(mockProperties);
    }

    private void mockShapeFactory() {
        mockedShapeFactory = Mockito.mockStatic(ShapeFactory.class);
        mockedShapeFactory
                .when(() -> ShapeFactory.create(anyString(), anyDouble(), anyDouble(), anyDouble()))
                .thenCallRealMethod();
    }

    @AfterEach
    public void tearDown() {
        mockedStatic.close();
        mockedShapeFactory.close();
    }

    @Test
    void verifyCorrectShapeOfClassesInterfaces() {
        assertNotNull(theClassDiagram);
        assertEquals(3, theClassDiagram.getGraphNodes().keySet().size());
        JavaFXClassVisualization classVisualization = new JavaFXClassVisualization(theClassDiagram);
        SmartGraphPanel<JavaFXUMLNode, String> graphPanel = classVisualization.createGraphView();
        assertNotNull(graphPanel);
        Collection<SmartGraphVertex<JavaFXUMLNode>> smartVertices = graphPanel.getSmartVertices();
        assertEquals(3, smartVertices.size());
        // verify that 3 circles are created
        mockedShapeFactory.verify(
                () -> ShapeFactory.create(eq("class"), anyDouble(), anyDouble(), anyDouble()),
                times(2));
        mockedShapeFactory.verify(
                () -> ShapeFactory.create(eq("interface"), anyDouble(), anyDouble(), anyDouble()),
                times(1));
    }
}

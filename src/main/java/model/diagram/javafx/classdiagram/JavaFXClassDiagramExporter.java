package model.diagram.javafx.classdiagram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.diagram.javafx.JavaFXDiagramExporter;
import model.graph.Arc;
import model.graph.SinkVertex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class JavaFXClassDiagramExporter implements JavaFXDiagramExporter {

    private final Path graphSavePath;
    private final Map<SinkVertex, Set<Arc<SinkVertex>>> diagram;

    public JavaFXClassDiagramExporter(Path graphSavePath, Map<SinkVertex, Set<Arc<SinkVertex>>> diagram) {
        this.diagram = diagram;
        this.graphSavePath = graphSavePath;
    }

    @Override
    public File saveDiagram(){
        File graphSaveFile = graphSavePath.toFile();
        try (FileWriter fileWriter = new FileWriter(graphSaveFile)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(SinkVertex.class, new SinkVertexSerializer()).create();
            String json = gson.toJson(diagram.keySet());
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graphSaveFile;
    }

}

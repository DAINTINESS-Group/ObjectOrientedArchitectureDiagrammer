package model.diagram.javafx.packagediagram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.diagram.DiagramExporter;
import model.graph.Arc;
import model.graph.Vertex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class JavaFXPackageDiagramExporter implements DiagramExporter {
    private final Map<Vertex, Set<Arc<Vertex>>> diagram;

    public JavaFXPackageDiagramExporter(Map<Vertex, Set<Arc<Vertex>>> diagram) {
        this.diagram = diagram;
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File graphSaveFile = exportPath.toFile();
        try (FileWriter fileWriter = new FileWriter(graphSaveFile)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(Vertex.class, new VertexSerializer()).create();
            String json = gson.toJson(diagram.keySet());
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graphSaveFile;
    }

}

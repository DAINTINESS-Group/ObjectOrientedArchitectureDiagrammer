package gr.uoi.ooad.model.diagram.exportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.uoi.ooad.model.diagram.ClassDiagram;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JavaFXClassDiagramExporter implements DiagramExporter {
    private static final Logger logger = LogManager.getLogger(JavaFXClassDiagramExporter.class);

    private final ClassDiagram classDiagram;

    public JavaFXClassDiagramExporter(ClassDiagram diagram) {
        this.classDiagram = diagram;
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File graphSaveFile = exportPath.toFile();
        try (FileWriter fileWriter = new FileWriter(graphSaveFile)) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(
                                    ClassifierVertex.class, new ClassifierVertexSerializer())
                            .create();

            String json = gson.toJson(classDiagram.getDiagram().keySet());
            fileWriter.write(json);
        } catch (IOException e) {
            logger.error("Failed to write json to file with path: {}", exportPath.toAbsolutePath());
            throw new RuntimeException(e);
        }

        return graphSaveFile;
    }
}

package gr.uoi.ooad.model.diagram.exportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gr.uoi.ooad.model.diagram.PackageDiagram;
import gr.uoi.ooad.model.graph.PackageVertex;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JavaFXPackageDiagramExporter implements DiagramExporter {
    private static final Logger logger = LogManager.getLogger(JavaFXPackageDiagramExporter.class);

    private final PackageDiagram packageDiagram;

    public JavaFXPackageDiagramExporter(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File graphSaveFile = exportPath.toFile();
        try (FileWriter fileWriter = new FileWriter(graphSaveFile)) {
            Gson gson =
                    new GsonBuilder()
                            .registerTypeAdapter(PackageVertex.class, new PackageVertexSerializer())
                            .create();
            String json = gson.toJson(packageDiagram.getDiagram().keySet());
            fileWriter.write(json);
        } catch (IOException e) {
            logger.error("Failed to write json to file with path: {}", exportPath.toAbsolutePath());
            throw new RuntimeException(e);
        }

        return graphSaveFile;
    }
}

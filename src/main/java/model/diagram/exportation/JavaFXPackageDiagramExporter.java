package model.diagram.exportation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.diagram.PackageDiagram;
import model.graph.PackageVertex;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class JavaFXPackageDiagramExporter implements DiagramExporter {

    private final PackageDiagram packageDiagram;

    public JavaFXPackageDiagramExporter(PackageDiagram packageDiagram) {
        this.packageDiagram = packageDiagram;
    }

    @Override
    public File exportDiagram(Path exportPath) {
        File graphSaveFile = exportPath.toFile();
        try (FileWriter fileWriter = new FileWriter(graphSaveFile)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(PackageVertex.class, new PackageVertexSerializer()).create();
            String json = gson.toJson(packageDiagram.getDiagram().keySet());
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graphSaveFile;
    }

}

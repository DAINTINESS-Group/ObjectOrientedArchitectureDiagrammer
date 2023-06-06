package model.diagram;

import java.io.File;
import java.nio.file.Path;

public interface DiagramExporter {

    File exportDiagram(Path exportPath);
}

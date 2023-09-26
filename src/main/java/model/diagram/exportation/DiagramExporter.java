package model.diagram.exportation;

import java.io.File;
import java.nio.file.Path;

public interface DiagramExporter {

	File exportDiagram(Path exportPath);
}

package model.diagram.exportation;

import java.io.File;
import java.nio.file.Path;

public interface DiagramExporter {

	/**
	 * This method is responsible for exporting the corresponding diagram type.
	 *
	 * @param exportPath  the path of the exported file
	 * @return 			  the created File
	 */
	File exportDiagram(Path exportPath);
}

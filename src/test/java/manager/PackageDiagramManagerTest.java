package manager;

import model.graph.PackageVertex;
import org.junit.jupiter.api.Test;
import utils.PathConstructor;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageDiagramManagerTest {

	@Test
	void populateGraphMLPackageNodeTest() {
		PackageDiagramManager packageDiagramManager = new PackageDiagramManager();
		SourceProject 		  sourceProject 		= packageDiagramManager.createSourceProject(Paths.get(String.format("%s%s%s",
																														PathConstructor.getCurrentPath(),
																														File.separator,
																														PathConstructor.constructPath("src",
																																					  "test",
																																					  "resources",
																																					  "LatexEditor",
																																					  "src"))));
		packageDiagramManager.convertTreeToDiagram(List.of("src.view",
														   "src.model",
														   "src.model.strategies",
														   "src.controller.commands",
														   "src.controller"));
		Map<PackageVertex, Integer> graphNodes = packageDiagramManager.getPackageDiagram().getGraphNodes();
		Map<Path, PackageVertex> packageNodes = sourceProject.getInterpreter().getVertices();
		packageNodes.remove(Paths.get(String.format("%s%s%s",
													PathConstructor.getCurrentPath(),
													File.separator,
													PathConstructor.constructPath("src",
																				  "test",
																				  "resources",
																				  "LatexEditor",
																				  "src"))));
		assertEquals(packageNodes.size(), graphNodes.size());
		Iterator<Map.Entry<Path, PackageVertex>> iter1 = packageNodes.entrySet().iterator();
		Iterator<Map.Entry<PackageVertex, Integer>> iter2 = graphNodes.entrySet().iterator();

		List<String> l1 = new ArrayList<>();
		List<String> l2 = new ArrayList<>();
		while (iter1.hasNext() || iter2.hasNext()) {
			Map.Entry<Path, PackageVertex> e1 = iter1.next();
			Map.Entry<PackageVertex, Integer> e2 = iter2.next();
			l1.add(e1.getValue().getName());
			l2.add(e2.getKey().getName());
		}
		Collections.sort(l1);
		Collections.sort(l2);
		assertTrue(l1.size() == l2.size() && l1.containsAll(l2) && l2.containsAll(l1));
	}
}

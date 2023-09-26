package parser;

import parser.tree.PackageNode;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PackageNodeCleaner {
	private final Map<Path, PackageNode> packageNodes;

	public PackageNodeCleaner(Map<Path, PackageNode> packageNodes) {
		this.packageNodes = packageNodes;
	}

	public Map<Path, PackageNode> removeNonPackageNodes() {
		Map<Path, PackageNode> validPackageNodes = new HashMap<>();
		for (PackageNode packageNode: packageNodes.values()) {
			if (isPackageNodeValid(packageNode)) {
				validPackageNodes.put(packageNode.getPackageNodesPath(), packageNode);
				continue;
			}
			PackageNode parentNode = packageNode.getParentNode();
			if (parentNode.getPackageNodesPath().toString().equals("")) {
				continue;
			}
			parentNode.getSubNodes().remove(packageNode.getPackageNodesPath());
		}

		return validPackageNodes;
	}

	private boolean isPackageNodeValid(PackageNode packageNode) {
		if (packageNode.getSubNodes().size() == 0) {
			return packageNode.isValid();
		}

		boolean result = false;
		for (PackageNode childNode: packageNode.getSubNodes().values()) {
			result = isPackageNodeValid(childNode);
			if (result) {
				break;
			}
		}

		return result || packageNode.isValid();
	}
}

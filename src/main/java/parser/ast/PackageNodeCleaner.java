package parser.ast;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import parser.ast.tree.PackageNode;

public class PackageNodeCleaner {

    public static Map<Path, PackageNode> removeNonPackageNodes(
            Map<Path, PackageNode> packageNodes) {
        Map<Path, PackageNode> validPackageNodes = new HashMap<>();
        for (PackageNode packageNode : packageNodes.values()) {
            if (isPackageNodeValid(packageNode)) {
                validPackageNodes.put(packageNode.getPath(), packageNode);
                continue;
            }

            PackageNode parentNode = packageNode.getParentNode();
            if (parentNode.getPath().toString().isEmpty()) continue;

            parentNode.getSubNodes().remove(packageNode.getPath());
        }

        return validPackageNodes;
    }

    private static boolean isPackageNodeValid(PackageNode packageNode) {
        if (packageNode.getSubNodes().isEmpty()) return packageNode.isValid();

        boolean flag = false;
        for (PackageNode childNode : packageNode.getSubNodes().values()) {
            flag = isPackageNodeValid(childNode);

            if (flag) break;
        }

        return flag || packageNode.isValid();
    }
}

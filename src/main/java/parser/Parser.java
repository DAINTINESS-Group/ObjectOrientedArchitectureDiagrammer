package parser;

import model.tree.PackageNode;

import java.util.Map;

public interface Parser {

    void parseSourcePackage(String sourcePackagePath);

    Map<String, PackageNode> getPackageNodes();
}

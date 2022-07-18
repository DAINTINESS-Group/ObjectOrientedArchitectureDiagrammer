package parser;

import model.PackageNode;

import java.util.Map;

public interface PackageParser {

    void parseSourcePackage(String sourcePackagePath);

    Map<String, PackageNode> getPackageNodes();
}

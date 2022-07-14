package parser;

import model.PackageNode;

import java.util.Map;

public interface PackageParser {

    public void parseSourcePackage(String sourcePackagePath);

    public Map<String, PackageNode> getPackageNodes();
}

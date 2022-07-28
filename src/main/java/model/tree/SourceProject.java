package model.tree;

import parser.Parser;
import parser.ProjectParser;

import java.util.HashMap;
import java.util.Map;

public class SourceProject {

    private Map<String, PackageNode> packageNodes;
    private PackageNode rootPackage;
    private int numberOfPackages;
    private int numberOfLeafNodes;
    private final Map<String, LeafNode> projectsLeafNodes;
    private final String sourcePackagePath;

    public SourceProject(String sourcePackagePath) {
        this.sourcePackagePath = sourcePackagePath;
        this.projectsLeafNodes = new HashMap<>();
    }

    /**
     * This method is responsible for the creation of tree by parsing the source folder, using ProjectParser's
     * parseSourcePackage method
     * @return the root of the tree
     */
    public PackageNode parseSourceProject() {
        Parser packageParser = new ProjectParser();
        this.rootPackage = packageParser.parseSourcePackage(sourcePackagePath);
        this.packageNodes = packageParser.getPackageNodes();
        setProjectsProperties();
        return rootPackage;
    }

    /**
     * This method returns the PackageNodes created by the Parser when parsing the project
     * @return the PackageNodes
     */
    public Map<String, PackageNode> getPackageNodes() {
        return packageNodes;
    }

    private void setProjectsProperties() {
        this.numberOfPackages = packageNodes.size();
        parseProjectsLeafNodes();
    }

    private void parseProjectsLeafNodes(){
        int leafNodeCounter = 0;
        for (PackageNode packageNode: packageNodes.values()) {
            projectsLeafNodes.putAll(packageNode.getLeafNodes());
            leafNodeCounter += packageNode.getLeafNodes().size();
        }
        this.numberOfLeafNodes = leafNodeCounter;
    }

}

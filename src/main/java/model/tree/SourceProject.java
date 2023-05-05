package model.tree;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SourceProject {

    private Map<Path, PackageNode> packageNodes;
    private int numberOfPackages;
    private int numberOfLeafNodes;
    private final Map<String, LeafNode> projectsLeafNodes;

    public SourceProject() {
        this.projectsLeafNodes = new HashMap<>();
    }

    /**
     * This method is responsible for setting the project's package nodes
     * @param packageNodes the project's package nodes
     * @return the number of package nodes
     */
    public int setPackageNodes(Map<Path, PackageNode> packageNodes) {
    	this.packageNodes = packageNodes;
        this.numberOfPackages = packageNodes.size();
    	return this.numberOfPackages;
    }

    public void setProjectsProperties() {
        int leafNodeCounter = 0;
        for (PackageNode packageNode: packageNodes.values()) {
            projectsLeafNodes.putAll(packageNode.getLeafNodes());
            leafNodeCounter += packageNode.getLeafNodes().size();
        }
        this.numberOfLeafNodes = leafNodeCounter;
    }

    /**
     * This method returns the PackageNodes created by the Parser when parsing the project
     * @return the package nodes
     */
    public Map<Path, PackageNode> getPackageNodes() {
        return packageNodes;
    }

	public int getNumberOfLeafNodes() {
		return numberOfLeafNodes;
	}

	public int getNumberOfPackages() {
		return numberOfPackages;
	}

}

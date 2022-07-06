package manager.diagram;

import model.PackageNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;

public class DiagramManager {

    private GraphMLNode graphMLNode;
    private GraphMLEdge graphMLEdge;
    private Map<String, PackageNode> packages;
    private String graphMLFile;

    public DiagramManager (Map<String, PackageNode> packageNodes) {
        this.packages = packageNodes;
        parsePackages();
    }

    private void parsePackages() {
        /*
        for (PackageNode p: packages.values()) {
            if (!p.isValid()) {
                continue;
            }
            createGraphMLFile();
            parseLeafNodes(p.getLeafNodes());
            writeSuffixToFile();
            printFile();
            break;
        }
         */
        try {
            createGraphMLFile(packages.get("commands"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createGraphMLFile(PackageNode currentPackage) throws IOException {
        new File(String.format("%s.graphml", currentPackage.getName()));
        FileWriter graphMLWriter = new FileWriter(String.format("%s.graphml", currentPackage.getName()));
        graphMLFile = "";
        graphMLFile += GraphMLSyntax.getInstance().getGraphMLPrefix();
        convertToGraphML(currentPackage);
        graphMLFile += GraphMLSyntax.getInstance().getGraphMLSuffix();
        graphMLWriter.write(graphMLFile);
        graphMLWriter.close();

        printFile();
    }

    private void convertToGraphML(PackageNode currentPackage) {
        graphMLNode = new GraphMLNode(currentPackage, graphMLFile);
        graphMLFile = graphMLNode.getGraphMLFile();

        graphMLEdge = new GraphMLEdge(currentPackage, graphMLFile, graphMLNode.getGraphMLNodes());
        graphMLFile = graphMLEdge.getGraphMLFile();
    }

    private void printFile() {
        System.out.println(graphMLFile);
    }
}
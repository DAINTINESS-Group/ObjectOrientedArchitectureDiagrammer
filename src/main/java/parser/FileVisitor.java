package parser;



import java.io.File;
import model.tree.node.LeafNode;




public interface FileVisitor {

    /** This method is responsible for the creation of the AST
     * @param file the Java source file
     * @param leafNode the leaf node representing the Java source file
     */
    void createAST(File file, LeafNode leafNode);


}
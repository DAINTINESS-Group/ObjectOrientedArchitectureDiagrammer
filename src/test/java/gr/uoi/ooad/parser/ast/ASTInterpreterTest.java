package gr.uoi.ooad.parser.ast;

import static gr.uoi.ooad.utils.assertions.UMLModelAssertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gr.uoi.ooad.model.graph.Arc;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.parser.ast.tree.LeafNode;
import gr.uoi.ooad.parser.ast.tree.PackageNode;
import gr.uoi.ooad.parser.ast.tree.Relationship;
import gr.uoi.ooad.utils.PathTemplate;
import gr.uoi.ooad.utils.PathTemplate.LatexEditor;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ASTInterpreterTest {

    @Test
    public void convertTreeToGraphTest() {
        ASTInterpreter interpreter = interpreterFor(PathTemplate.LatexEditor.SRC.path);
        Map<Path, PackageNode> packageNodes = interpreter.getPackageNodes();
        List<PackageVertex> vertices = interpreter.getVertices();
        assertEquals(packageNodes.size(), vertices.size());

        for (Map.Entry<Path, PackageNode> packageNodeEntry : packageNodes.entrySet()) {
            PackageNode packageNode = packageNodeEntry.getValue();
            PackageVertex vertex =
                    vertices.stream()
                            .filter(it -> it.getPath().equals(packageNodeEntry.getKey()))
                            .findFirst()
                            .orElseGet(Assertions::fail);
            assertEquals(packageNode.getNodeType().toString(), vertex.getVertexType().toString());
            assertEquals(packageNode.getNodeName(), vertex.getName());

            Map<Path, PackageNode> subNodes = packageNode.getSubNodes();
            Set<PackageVertex> neighbours = vertex.getNeighborVertices();
            assertEquals(subNodes.size(), neighbours.size());
            for (Map.Entry<Path, PackageNode> subNode : subNodes.entrySet()) {
                assertTrue(neighbours.stream().anyMatch(getPackageVertexPredicate(subNode)));
            }

            Map<PackageNode, Set<Relationship<PackageNode>>> packageNodeRelationships =
                    interpreter.getPackageNodeRelationships();

            if (!packageNodeRelationships.containsKey(packageNode)) {
                continue;
            }

            Set<Relationship<PackageNode>> relationships =
                    packageNodeRelationships.get(packageNode);
            Set<Arc<PackageVertex>> arcs = vertex.getArcs();
            assertEquals(relationships.size(), arcs.size());
            for (Relationship<PackageNode> relationship : relationships) {
                assertTrue(arcs.stream().anyMatch(getArcPredicate(relationship)));
            }

            Map<String, LeafNode> leafNodes = packageNode.getLeafNodes();
            Set<ClassifierVertex> sinkVertices = vertex.getSinkVertices();
            assertEquals(leafNodes.size(), sinkVertices.size());
            for (Map.Entry<String, LeafNode> leafNodeEntry : leafNodes.entrySet()) {
                ClassifierVertex classifierVertex =
                        sinkVertices.stream()
                                .filter(it -> it.getName().equals(leafNodeEntry.getKey()))
                                .findAny()
                                .orElseGet(Assertions::fail);

                List<LeafNode.Method> leafMethods = leafNodeEntry.getValue().methods();
                Set<ClassifierVertex.Method> vertexMethods = classifierVertex.getMethods();
                for (LeafNode.Method leafMethod : leafMethods) {
                    assertTrue(vertexMethods.stream().anyMatch(getMethodPredicate(leafMethod)));
                }

                List<LeafNode.Field> leafFields = leafNodeEntry.getValue().fields();
                Set<ClassifierVertex.Field> vertexFields = classifierVertex.getFields();
                for (LeafNode.Field leafField : leafFields) {
                    assertTrue(vertexFields.stream().anyMatch(getFieldPredicate(leafField)));
                }

                Map<LeafNode, Set<Relationship<LeafNode>>> leafNodeRelationships =
                        interpreter.getLeafNodeRelationships();

                if (!leafNodeRelationships.containsKey(leafNodeEntry.getValue())) {
                    continue;
                }

                Set<Relationship<LeafNode>> subNodeRelationships =
                        leafNodeRelationships.get(leafNodeEntry.getValue());
                Set<Arc<ClassifierVertex>> sinkVertexArcs = classifierVertex.getArcs();
                assertEquals(subNodeRelationships.size(), sinkVertexArcs.size());
                for (Relationship<LeafNode> relationship : subNodeRelationships) {
                    assertTrue(sinkVertexArcs.stream().anyMatch(getPredicate(relationship)));
                }
            }
        }
    }

    @NotNull
    private static Predicate<PackageVertex> getPackageVertexPredicate(
            Map.Entry<Path, PackageNode> subNode) {
        return it ->
                it.getPath().toString().equals(subNode.getKey().toString())
                        && it.getName().equals(subNode.getValue().getNodeName())
                        && it.getVertexType()
                        .toString()
                        .equals(subNode.getValue().getNodeType().toString());
    }

    @NotNull
    private static Predicate<Arc<ClassifierVertex>> getPredicate(
            Relationship<LeafNode> relationship) {
        return it ->
                it.arcType().toString().equals(relationship.relationshipType().toString())
                        && it.sourceVertex().getPath().equals(relationship.startingNode().path())
                        && it.targetVertex().getPath().equals(relationship.endingNode().path());
    }

    @NotNull
    private static Predicate<ClassifierVertex.Field> getFieldPredicate(LeafNode.Field leafField) {
        return it ->
                it.name().equals(leafField.name())
                        && it.type().equals(leafField.fieldType())
                        && it.modifier().toString().equals(leafField.modifierType().toString());
    }

    @NotNull
    private static Predicate<Arc<PackageVertex>> getArcPredicate(
            Relationship<PackageNode> relationship) {
        return it ->
                it.arcType().toString().equals(relationship.relationshipType().toString())
                        && it.sourceVertex().getPath().equals(relationship.startingNode().getPath())
                        && it.targetVertex().getPath().equals(relationship.endingNode().getPath());
    }

    @NotNull
    private static Predicate<ClassifierVertex.Method> getMethodPredicate(
            LeafNode.Method leafMethod) {
        return it ->
                it.name().equals(leafMethod.name())
                        && it.parameters().size() == leafMethod.parameters().size()
                        && it.parameters().containsAll(leafMethod.parameters().values())
                        && leafMethod.parameters().values().containsAll(it.parameters())
                        && it.returnType().equals(leafMethod.returnType())
                        && it.modifier().toString().equals(leafMethod.modifierType().toString());
    }

    /**
     * Creates a new interpreter, parses the given project and converts it to a graph.
     *
     * @param projectPath the source folder of the project to parse
     * @return the interpreter, ready to be passed to {@code given(...)}
     */
    private static ASTInterpreter interpreterFor(Path projectPath) {
        ASTInterpreter interpreter = new ASTInterpreter();
        interpreter.parseProject(projectPath);
        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());

        return interpreter;
    }

    @Test
    void packageDependenciesForLatexEditor() {
        given(interpreterFor(LatexEditor.SRC.path))
                .assertThatPackage("src.controller")
                .hasDependencyTo("src.model")
                .also()
                .assertThatPackage("src.view")
                .hasDependencyTo("src.controller")
                .and()
                .hasDependencyTo("src.model");
    }

    @Test
    void latexEditorImplementationsTest(){
        given(interpreterFor(PathTemplate.LatexEditor.SRC.path))
                .assertThatInterface("Command") // checks if Command is Interface
                .also()
                .assertThatInterface("VersionsStrategy") // checks if VersionsStrategy is Interface
                .also()
                .assertThatClass("AddLatexCommand")
                .implementsInterface("Command")
                .also()
                .assertThatClass("StableVersionsStrategy")
                .implementsInterface("VersionsStrategy");
    }


    @Test
    void classRelationshipArcsArePresentForDefaultPackageTest() {
        ASTInterpreter interpreter = interpreterFor(PathTemplate.ParserTesting.SRC.path);

        given(interpreter)
                .assertThatClass("ImplementingClass")
                .extendsClass("ExtensionClass")
                .and()
                .implementsInterface("TestingInterface")
                .and()
                .implementsInterface("TestingInterface2");


        given(interpreter)
                .assertThatClass("ObjectCreationSample")
                .hasAssociationTo("ExtensionClass")
                .and()
                .hasDependencyTo("TestingInterface")
                .and()
                .hasDependencyTo("ImplementingClass");

    }

    @Test
    void bookstoreInheritanceTest(){
        given(interpreterFor(PathTemplate.BookstoreAdvanced.SRC.path))
                .assertThatClass("Book")
                .extendsClass("Item")
                .also()
                .assertThatClass("CD")
                .extendsClass("Item");
    }

    @Test
    void bookstoreNoFalseInheritanceTest() {
        given(interpreterFor(PathTemplate.BookstoreAdvanced.SRC.path))
                .assertThatClass("Item")
                .doesNotExtendClass("Book")
                .also()
                .assertThatClass("Book")
                .doesNotExtendClass("CD");
    }

    @Disabled("Parser limitation: ASTRelationshipIdentifier.isExtension attributes the extends of inner class to outer class"
    + " (see ParserTesting/InnerClassSample.java:5)")
    @Test
    void parserTestingInnerClassExtensionTest() {
        given(interpreterFor(PathTemplate.ParserTesting.SRC.path))
                .assertThatClass("InnerClassSample")
                .doesNotExtendClass("ObjectCreationSample");
    }

    @Test
    void bookstoreAssociationsTest(){
        given(interpreterFor(PathTemplate.BookstoreAdvanced.SRC.path))
                .assertThatClass("Gui")
                .hasAssociationTo("ItemManager")
                .and()
                .hasAssociationTo("ItemFactory")
                .also()
                .assertThatClass("CartController")
                .hasAssociationTo("ShoppingCart")
                .also()
                .assertThatClass("GuiEventHandler")
                .hasAssociationTo("ShoppingCart");
    }

    @Test
    void bookstoreDependenciesTest(){
        given(interpreterFor(PathTemplate.BookstoreAdvanced.SRC.path))
                .assertThatClass("ItemFactory")
                .hasDependencyTo("Book")
                .and()
                .hasDependencyTo("CD")
                .also()
                .assertThatClass("CartViewer")
                .hasDependencyTo("ShoppingCart")
                .and()
                .hasDependencyTo("CartController");
    }

    @Test
    void bookstorePackageDependenciesTest(){
        given(interpreterFor(PathTemplate.BookstoreAdvanced.SRC.path))
                .assertThatPackage("src.gui")
                .hasDependencyTo("src.bookstore");
    }

    @Disabled("Parser limitation: FileVisitor.VariableVisitor records a local variable "
    + "only when it is initialized with 'new' (see BookstoreAdvancedV01.src.gui.Gui.java:23)")
    @Test
    void bookstoreLocalVariableDependencyTest(){
        given(interpreterFor(PathTemplate.BookstoreAdvanced.SRC.path))
                .assertThatClass("Gui")
                .hasDependencyTo("Item");
    }



//    @Test
//    void associationArcsArePresentForDefaultPackageTest() {
//        ASTInterpreter interpreter = new ASTInterpreter();
//        interpreter.parseProject(PathTemplate.ParserTesting.SRC.path);
//        interpreter.convertToGraph(Collections.emptyList(), Collections.emptyList());
//
//        given(interpreter)
//                .assertThatClass("ImplementingClass")
//                .implementsInterface("TestingInterface");
//
//    }
}

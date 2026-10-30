package gr.uoi.ooad.utils.assertions;

import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;
import gr.uoi.ooad.model.graph.VertexType;
import gr.uoi.ooad.parser.ast.ASTInterpreter;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class UMLModelAssertions {

    private ASTInterpreter interpreter;

    private static UMLModelAssertions instance = null;

    public UMLModelAssertions(ASTInterpreter interpreter) {
        this.interpreter = interpreter;
    }

    public static UMLModelAssertions given(ASTInterpreter interpreter){
        if (instance == null){
            instance = new UMLModelAssertions(interpreter);
        } else {
            // changed parsed project create new assertions object
            if (!instance.interpreter.hasSourcePackagePath(interpreter.getSourcePackagePath())){
                instance = new UMLModelAssertions(interpreter);
            }
        }
        return instance;
    }

    public ClassStructureAssertion assertThatClass(String className){

        List<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();

        ClassifierVertex classifierVertex =
                sinkVertices.stream()
                        .filter(it -> it.getName().equals(className)
                            && it.getVertexType().equals(VertexType.CLASS))
                        .findFirst().orElseGet(() -> Assertions.fail("Class: " + className + " not found in the model."));

        return ClassifierAssertionsFactory.assertionsForClass(this, classifierVertex);


    }

    public ClassStructureAssertion assertThatInterface(String interfaceName){

        List<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();

        ClassifierVertex classifierVertex =
                sinkVertices.stream()
                        .filter(it -> it.getName().equals(interfaceName)
                                && it.getVertexType().equals(VertexType.INTERFACE))
                        .findFirst().orElseGet(() -> Assertions.fail("Interface: " + interfaceName + " not found in the model."));

        return ClassifierAssertionsFactory.assertionsForClass(this, classifierVertex);


    }

    public ClassStructureAssertion assertThatClassifier(String classifierName){

        List<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();

        ClassifierVertex classifierVertex =
                sinkVertices.stream()
                        .filter(it -> it.getName().equals(classifierName))
                        .findFirst().orElseGet(() -> Assertions.fail("Classifier: " + classifierName + " not found in the model."));

        return ClassifierAssertionsFactory.assertionsForClass(this, classifierVertex);
    }

    public PackageStructureAssertion assertThatPackage(String packageName){

        List<PackageVertex> packageVertices = interpreter.getVertices();

        PackageVertex packageVertex =
                packageVertices.stream()
                        .filter(it -> it.getName().equals(packageName)
                        && it.getVertexType().equals(VertexType.PACKAGE))
                        .findFirst().orElseGet(() -> Assertions.fail("Package: " + packageName + " not found in the model."));

        return ClassifierAssertionsFactory.assertionsForPackage(this, packageVertex);

    }

    public UMLModelAssertions everyClassExtendsAtMostOneClass() {
        List<String> violators = interpreter.getSinkVertices().stream()
                .filter(v -> v.getVertexType().equals(VertexType.CLASS))
                .filter(v -> v.getArcs().stream()
                        .filter(a -> a.arcType().equals(ArcType.EXTENSION))
                        .count() > 1)
                .map(ClassifierVertex::getName)
                .toList();

        Assertions.assertTrue(violators.isEmpty(), "Classes that extend more than one class: " + violators);
        return this;
    }
}

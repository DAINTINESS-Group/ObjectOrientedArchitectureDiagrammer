package gr.uoi.ooad.utils.assertions;

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
                        .findFirst().orElseGet(Assertions::fail);

        return ClassifierAssertionsFactory.assertionsForClass(this, classifierVertex);


    }

    public ClassStructureAssertion assertThatInterface(String interfaceName){

        List<ClassifierVertex> sinkVertices = interpreter.getSinkVertices();

        ClassifierVertex classifierVertex =
                sinkVertices.stream()
                        .filter(it -> it.getName().equals(interfaceName)
                                && it.getVertexType().equals(VertexType.INTERFACE))
                        .findFirst().orElseGet(Assertions::fail);

        return ClassifierAssertionsFactory.assertionsForClass(this, classifierVertex);


    }

    public PackageStructureAssertion assertThatPackage(String packageName){

        List<PackageVertex> packageVertices = interpreter.getVertices();

        PackageVertex packageVertex =
                packageVertices.stream()
                        .filter(it -> it.getName().equals(packageName)
                        && it.getVertexType().equals(VertexType.PACKAGE))
                        .findFirst().orElseGet(Assertions::fail);

        return ClassifierAssertionsFactory.assertionsForPackage(this, packageVertex);

    }

}

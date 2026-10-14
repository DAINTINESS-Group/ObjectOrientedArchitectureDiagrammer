package gr.uoi.ooad.utils.assertions;

import gr.uoi.ooad.parser.ast.ASTInterpreter;

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

    public static void assertThatClass(String className){

    }

    public static void assertThatPackage(String packageName){

    }

}

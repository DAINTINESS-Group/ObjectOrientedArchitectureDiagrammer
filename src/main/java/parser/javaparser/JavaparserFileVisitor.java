package parser.javaparser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import model.tree.javaparser.JavaparserLeafNode;
import model.tree.node.LeafNode;
import model.tree.node.NodeType;
import parser.FileVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**This class is responsible for the creation of the AST of a Java source file.
 * Using the ASTNode API it parses the files methods parameters, return types and field declarations
 */
public class JavaparserFileVisitor implements FileVisitor {

    private final List<String> allCreatedObjects;
    private final List<String> createdAssignedObjects;

    public JavaparserFileVisitor(){
        allCreatedObjects = new ArrayList<>();
        createdAssignedObjects = new ArrayList<>();
    }

    public void createAST(File file, LeafNode leafNode) {
        try {
            JavaparserLeafNode javaparserLeafNode = (JavaparserLeafNode) leafNode;
            CompilationUnit compilationUnit = StaticJavaParser.parse(file);

            InheritanceVisitor inheritanceVisitor = new InheritanceVisitor(javaparserLeafNode);
            inheritanceVisitor.visit(compilationUnit, null);

            VoidVisitor<Void> constructorVisitor = new ConstructorVisitor(javaparserLeafNode);
            constructorVisitor.visit(compilationUnit, null);

            VoidVisitor<Void> fieldNameVisitor = new FieldVisitor(javaparserLeafNode);
            fieldNameVisitor.visit(compilationUnit, null);

            VoidVisitor<List<String>> variableVisitor = new VariableVisitor(javaparserLeafNode);
            variableVisitor.visit(compilationUnit, createdAssignedObjects);

            VoidVisitor<Void> methodNameVisitor = new MethodVisitor(javaparserLeafNode);
            methodNameVisitor.visit(compilationUnit, null);

            VoidVisitor<List<String>> objectCreationVisitor = new ObjectCreationVisitor();
            objectCreationVisitor.visit(compilationUnit, allCreatedObjects);
            populateCreatedObjects(javaparserLeafNode);

            VoidVisitor<Void> enumVisitor = new EnumVisitor(javaparserLeafNode, inheritanceVisitor.isClassOrInterface());
            enumVisitor.visit(compilationUnit, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class InheritanceVisitor extends VoidVisitorAdapter<Void> {
        private final JavaparserLeafNode leafNode;
        private boolean isClassOrInterface;

        public InheritanceVisitor(JavaparserLeafNode leafNode) {
            this.leafNode = leafNode;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Void arg) {
            super.visit(classOrInterfaceDeclaration, arg);
            leafNode.setNodeName(classOrInterfaceDeclaration.getNameAsString());
            isClassOrInterface = true;

            if (classOrInterfaceDeclaration.isInterface()) {
                leafNode.setNodeType(NodeType.INTERFACE);
                return;
            }

            leafNode.setNodeType(NodeType.CLASS);
            classOrInterfaceDeclaration.getExtendedTypes().forEach(classOrInterfaceType ->
                    leafNode.setBaseClass(classOrInterfaceType.getNameAsString())
            );

            classOrInterfaceDeclaration.getImplementedTypes().forEach(classOrInterfaceType ->
                    leafNode.addImplementedInterface(classOrInterfaceType.getNameAsString())
            );
        }

        public boolean isClassOrInterface() {
            return isClassOrInterface;
        }
    }

    private static class ConstructorVisitor extends VoidVisitorAdapter<Void> {
        private final JavaparserLeafNode leafNode;

        public ConstructorVisitor(JavaparserLeafNode leafNode) {
            this.leafNode = leafNode;
        }

        @Override
        public void visit(ConstructorDeclaration constructorDeclaration, Void arg) {
            super.visit(constructorDeclaration, arg);
            String visibility = "package private";
            List<String> parameters = new ArrayList<>();
            List<String> parametersName = new ArrayList<>();
            if (constructorDeclaration.getModifiers().size() > 0) {
            	visibility = constructorDeclaration.getModifiers().get(0).toString();
            	visibility = visibility.substring(0, visibility.length() - 1);
            }
            leafNode.addMethodVisibility(constructorDeclaration.getNameAsString(), visibility);
            leafNode.addMethod(constructorDeclaration.getNameAsString(), "Constructor");
            constructorDeclaration.getParameters().forEach(parameter ->{
                    leafNode.addMethodParameterType(parameter.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"));
                    parameters.add(parameter.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"));
                    parametersName.add(parameter.getName().toString());
            });
            leafNode.addForPlantUML(constructorDeclaration.getNameAsString(), parameters, parametersName);
        }
    }

    private static class FieldVisitor extends VoidVisitorAdapter<Void> {
        private final JavaparserLeafNode leafNode;

        public FieldVisitor(JavaparserLeafNode leafNode) {
            this.leafNode = leafNode;
        }

        @Override
        public void visit(FieldDeclaration fieldDeclaration, Void arg) {
            super.visit(fieldDeclaration, arg);

            fieldDeclaration.getVariables().forEach(v ->
                    {
                    	String visibility = "package private";
                        if (fieldDeclaration.getModifiers().size() > 0) {
                        	visibility = fieldDeclaration.getModifiers().get(0).toString();
                        	visibility = visibility.substring(0, visibility.length() - 1);
                        }
                        leafNode.addFieldVisibility(v.getNameAsString(),  visibility);
                    	leafNode.addField(v.getNameAsString(),
                            v.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"));
                    });
        }
    }

    private static class VariableVisitor extends VoidVisitorAdapter<List<String>> {
        private final JavaparserLeafNode leafNode;

        public VariableVisitor(JavaparserLeafNode leafNode) {
            this.leafNode = leafNode;
        }

        @Override
        public void visit(VariableDeclarationExpr variableDeclarationExpr, List<String> createdAssignedObjects) {
            super.visit(variableDeclarationExpr, createdAssignedObjects);
            Pattern pattern = Pattern.compile("new ([A-Za-z]+)\\([^)]*\\)");
            Matcher matcher = pattern.matcher(variableDeclarationExpr.toString());
            if (!matcher.find()) {
                return;
            }
            createdAssignedObjects.add(matcher.group(1));

            variableDeclarationExpr.getVariables().forEach(variableDeclaration -> leafNode.addVariable(variableDeclaration.getNameAsString(),
                    variableDeclaration.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"))
            );
        }
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        private final JavaparserLeafNode leafNode;

        public MethodVisitor(JavaparserLeafNode leafNode) {
            this.leafNode = leafNode;
        }

        @Override
        public void visit(MethodDeclaration methodDeclaration, Void arg) {
            super.visit(methodDeclaration, arg);
            String visibility = "package private";
            List<String> parameters = new ArrayList<>();
            List<String> parametersName = new ArrayList<>();
            if (methodDeclaration.getModifiers().size() > 0) {
            	visibility = methodDeclaration.getModifiers().get(0).toString();
            	visibility = visibility.substring(0, visibility.length() - 1);
            }
            leafNode.addMethodVisibility(methodDeclaration.getNameAsString(), visibility);
            leafNode.addMethod(methodDeclaration.getNameAsString(),
                    methodDeclaration.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"));
            methodDeclaration.getParameters().forEach(parameter -> {
            	leafNode.addMethodParameterType(parameter.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"));
            	parameters.add(parameter.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"));
                parametersName.add(parameter.getName().toString());
            });
            leafNode.addForPlantUML(methodDeclaration.getNameAsString(), parameters, parametersName);
        }
    }

    private static class ObjectCreationVisitor extends VoidVisitorAdapter<List<String>> {

        @Override
        public void visit(ObjectCreationExpr objectCreationExpr, List<String> createdObjects) {
            super.visit(objectCreationExpr, createdObjects);
            createdObjects.add(objectCreationExpr.asObjectCreationExpr().getType().asString().replaceAll("<", "[").replaceAll(">", "]"));
        }
    }

    private static class EnumVisitor extends VoidVisitorAdapter<Void> {
        private final JavaparserLeafNode leafNode;
        private final boolean isClassOrInterface;

        public EnumVisitor(JavaparserLeafNode leafNode, boolean isClassOrInterface) {
            this.leafNode = leafNode;
            this.isClassOrInterface = isClassOrInterface;
        }

        @Override
        public void visit(EnumDeclaration enumDeclaration, Void arg) {
            super.visit(enumDeclaration, arg);
            if (!enumDeclaration.isEnumDeclaration() || isClassOrInterface) {
                return;
            }
            leafNode.setNodeName(enumDeclaration.getNameAsString());
            leafNode.setNodeType(NodeType.ENUM);
        }

    }

    private void populateCreatedObjects(JavaparserLeafNode leafNode) {
        List<String> notAssignedCreatedObjects = getNotAssignedCreatedObjects(leafNode.getFieldsTypes(), allCreatedObjects);
        notAssignedCreatedObjects = getNotAssignedCreatedObjects(leafNode.getVariablesTypes(), notAssignedCreatedObjects);
        notAssignedCreatedObjects = getNotAssignedCreatedObjects(createdAssignedObjects, notAssignedCreatedObjects);
        notAssignedCreatedObjects.forEach(leafNode::addCreatedObject);
    }

    private List<String> getNotAssignedCreatedObjects(List<String> assignedObjects, List<String> createdObjects) {
        List<String> newList = new ArrayList<>();
        Collections.sort(createdObjects);
        Collections.sort(assignedObjects);

        int i = 0;
        int j = 0;
        main:
        while (i < createdObjects.size()) {
            while (j < assignedObjects.size()) {
                if (createdObjects.get(i).equals(assignedObjects.get(j))) {
                    j++;
                }else {
                    newList.add(createdObjects.get(i));
                }
                i++;
                if (i == createdObjects.size()) {
                    break main;
                }
            }
            newList.add(createdObjects.get(i));
            i++;
        }
        return newList;
    }
}

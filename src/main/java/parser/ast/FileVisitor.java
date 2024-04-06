package parser.ast;

import static parser.ast.tree.ModifierType.PACKAGE_PRIVATE;
import static parser.ast.tree.NodeType.ENUM;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.ast.tree.LeafNode;
import parser.ast.tree.LeafNodeBuilder;
import parser.ast.tree.ModifierType;
import parser.ast.tree.NodeType;
import parser.ast.tree.PackageNode;

/**
 * This class is responsible for the creation of the AST of a Java source file using {@link
 * JavaParser}. Using the different visitors, it parses the file's inheritance declarations,
 * constructor, methods parameters, return types, field & local variable declarations as well as the
 * instantiated objects that aren't assigned to a variable.
 */
public class FileVisitor {
    private static final Logger logger = LogManager.getLogger(FileVisitor.class);

    private final InheritanceVisitor inheritanceVisitor = new InheritanceVisitor();
    private final ConstructorVisitor constructorVisitor = new ConstructorVisitor();
    private final FieldVisitor fieldNameVisitor = new FieldVisitor();
    private final MethodVisitor methodNameVisitor = new MethodVisitor();
    private final EnumVisitor enumVisitor = new EnumVisitor();
    private final MemberVisitor memberVisitor = new MemberVisitor();
    private final VariableVisitor variableVisitor = new VariableVisitor();
    private final ObjectCreationVisitor objectCreationVisitor = new ObjectCreationVisitor();
    private final ImportVisitor importVisitor = new ImportVisitor();
    private final LeafNodeBuilder leafNodeBuilder;
    private final Map<String, String> variablesMap;
    private final List<String> objectTypes;
    private final List<LeafNode.Method> methods;
    private final List<LeafNode.Field> fields;
    private final List<LeafNode> innerClasses;
    private final Path path;
    private final List<String> imports;
    private final List<String> createdObjects;
    private final List<String> unassignedObjects;
    private final List<String> implementedInterfaces;
    private final List<String> enums;
    private final List<String> records;
    private NodeType nodeType;
    private boolean isClassOrInterface;
    private String baseClass;
    private String nodeName;

    /**
     * The FileVisitor's default constructor.
     *
     * @param parentNode the LeafNode representing the Java source file that we are visiting.
     */
    public FileVisitor(PackageNode parentNode, Path path) {
        this.path = path;
        leafNodeBuilder = new LeafNodeBuilder(parentNode, path);
        createdObjects = new ArrayList<>();
        unassignedObjects = new ArrayList<>();
        imports = new ArrayList<>();
        implementedInterfaces = new ArrayList<>();
        objectTypes = new ArrayList<>();
        methods = new ArrayList<>();
        fields = new ArrayList<>();
        enums = new ArrayList<>();
        records = new ArrayList<>();
        variablesMap = new HashMap<>();
        innerClasses = new ArrayList<>();
        baseClass = "";
        nodeName = "";
        nodeType = null;
    }

    /** This method is responsible for the creation of the AST. */
    public LeafNode createAST() {
        try {
            StaticJavaParser.getParserConfiguration()
                    .setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
            CompilationUnit compilationUnit = StaticJavaParser.parse(path.toFile());

            isClassOrInterface = false;
            compilationUnit.accept(inheritanceVisitor, null);

            if (!isClassOrInterface) {
                compilationUnit.accept(enumVisitor, null);
            } else {
                compilationUnit.accept(importVisitor, null);
                compilationUnit.accept(memberVisitor, null);
                compilationUnit.accept(constructorVisitor, null);
                compilationUnit.accept(fieldNameVisitor, null);
                compilationUnit.accept(variableVisitor, null);
                compilationUnit.accept(methodNameVisitor, null);
                compilationUnit.accept(objectCreationVisitor, null);
                unassignedObjects.addAll(populateCreatedObjects());
            }
        } catch (FileNotFoundException e) {
            logger.debug("File with path: {} not found", path);
            throw new RuntimeException(e);
        }

        return leafNodeBuilder
                .setNodeType(nodeType)
                .setBaseClass(baseClass)
                .setFields(fields)
                .setCreatedObjects(unassignedObjects)
                .setMethods(methods)
                .setNodeName(nodeName)
                .setVariables(variablesMap)
                .setImports(imports)
                .setRecords(records)
                .setInnerClasses(innerClasses)
                .setImplementedInterface(implementedInterfaces)
                .setInnerEnums(enums)
                .build();
    }

    private class InheritanceVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Void arg) {
            super.visit(classOrInterfaceDeclaration, arg);
            isClassOrInterface = true;

            // Inner Class case.
            if (!path.endsWith(classOrInterfaceDeclaration.getNameAsString() + ".java")) {
                String innerBaseClass = "";
                if (!classOrInterfaceDeclaration.getExtendedTypes().isEmpty()) {
                    innerBaseClass =
                            classOrInterfaceDeclaration.getExtendedTypes().get(0).getNameAsString();
                }

                List<String> innerImplementedInterfaces = new ArrayList<>();
                for (ClassOrInterfaceType c : classOrInterfaceDeclaration.getImplementedTypes()) {
                    innerImplementedInterfaces.add(c.getNameAsString());
                }
                // Create the leaf node for the inner class.
                LeafNodeBuilder innerLeafNodeBuilder = new LeafNodeBuilder(null, Paths.get(""));
                LeafNode innerClass =
                        innerLeafNodeBuilder
                                .setNodeName(classOrInterfaceDeclaration.getNameAsString())
                                .setBaseClass(innerBaseClass)
                                .setImplementedInterface(innerImplementedInterfaces)
                                .build();
                innerClasses.add(innerClass);

                return;
            }

            nodeName = classOrInterfaceDeclaration.getNameAsString();
            nodeType =
                    classOrInterfaceDeclaration.isInterface() ? NodeType.INTERFACE : NodeType.CLASS;

            if (!classOrInterfaceDeclaration.getExtendedTypes().isEmpty()) {
                baseClass = classOrInterfaceDeclaration.getExtendedTypes().get(0).getNameAsString();
            }

            List<String> implementedTypes =
                    classOrInterfaceDeclaration.getImplementedTypes().stream()
                            .map(NodeWithSimpleName::getNameAsString)
                            .collect(Collectors.toCollection(ArrayList::new));
            implementedInterfaces.addAll(implementedTypes);
        }
    }

    private class ConstructorVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(ConstructorDeclaration constructorDeclaration, Void arg) {
            super.visit(constructorDeclaration, arg);

            ModifierType modifierType =
                    constructorDeclaration.getModifiers().isEmpty()
                            ? PACKAGE_PRIVATE
                            : ModifierType.get(
                                    constructorDeclaration.getModifiers().get(0).toString());

            Map<String, String> parameters =
                    constructorDeclaration.getParameters().stream()
                            .collect(
                                    Collectors.toMap(
                                            NodeWithSimpleName::getNameAsString,
                                            parameter -> getType(parameter.getTypeAsString())));

            methods.add(
                    new LeafNode.Method(
                            constructorDeclaration.getNameAsString(),
                            "Constructor",
                            modifierType,
                            parameters));
        }
    }

    private class FieldVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(FieldDeclaration fieldDeclaration, Void arg) {
            super.visit(fieldDeclaration, arg);

            for (VariableDeclarator variable : fieldDeclaration.getVariables()) {
                ModifierType modifierType =
                        fieldDeclaration.getModifiers().isEmpty()
                                ? PACKAGE_PRIVATE
                                : ModifierType.get(
                                        fieldDeclaration.getModifiers().get(0).toString());

                fields.add(
                        new LeafNode.Field(
                                variable.getNameAsString(),
                                getType(variable.getTypeAsString()),
                                modifierType));
            }
        }
    }

    private class VariableVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(VariableDeclarationExpr variableDeclarationExpr, Void arg) {
            super.visit(variableDeclarationExpr, null);

            Pattern pattern =
                    Pattern.compile(
                            "[A-Za-z0-9]+ [A-Za-z0-9]+ = new ([A-Za-z0-9]+)\\([A-Za-z0-9]*[,"
                                    + " A-Za-z0-9*]*\\)");
            Matcher matcher = pattern.matcher(variableDeclarationExpr.toString());
            if (!matcher.find()) return;

            objectTypes.add(matcher.group(1));
            variableDeclarationExpr
                    .getVariables()
                    .forEach(
                            it ->
                                    variablesMap.put(
                                            it.getNameAsString(), getType(it.getTypeAsString())));
        }
    }

    private class MethodVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(MethodDeclaration methodDeclaration, Void arg) {
            super.visit(methodDeclaration, arg);

            ModifierType modifierType =
                    methodDeclaration.getModifiers().isEmpty()
                            ? PACKAGE_PRIVATE
                            : ModifierType.get(methodDeclaration.getModifiers().get(0).toString());

            Map<String, String> parameters =
                    methodDeclaration.getParameters().stream()
                            .collect(
                                    Collectors.toMap(
                                            NodeWithSimpleName::getNameAsString,
                                            parameter -> getType(parameter.getTypeAsString())));
            methods.add(
                    new LeafNode.Method(
                            methodDeclaration.getNameAsString(),
                            getType(methodDeclaration.getTypeAsString()),
                            modifierType,
                            parameters));
        }
    }

    private class ObjectCreationVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(ObjectCreationExpr objectCreationExpr, Void arg) {
            super.visit(objectCreationExpr, arg);

            createdObjects.add(
                    getType(objectCreationExpr.asObjectCreationExpr().getType().asString()));
        }
    }

    private class EnumVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(EnumDeclaration enumDeclaration, Void arg) {
            super.visit(enumDeclaration, arg);

            nodeName = enumDeclaration.getNameAsString();
            nodeType = ENUM;
        }
    }

    private class MemberVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(EnumDeclaration enumDeclaration, Void arg) {
            super.visit(enumDeclaration, arg);

            enums.add(enumDeclaration.getNameAsString());
        }

        @Override
        public void visit(RecordDeclaration n, Void arg) {
            super.visit(n, arg);

            records.add(n.getNameAsString());
        }
    }

    private class ImportVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(ImportDeclaration importDeclaration, Void arg) {
            super.visit(importDeclaration, arg);

            imports.add(
                    importDeclaration
                            .getNameAsString()
                            .replaceFirst("^import ", "")
                            .trim()
                            .replaceAll(";$", ""));
        }
    }

    private List<String> populateCreatedObjects() {
        List<String> notAssignedCreatedObjects =
                filterAssignedCreatedObjects(
                        fields.stream()
                                .map(LeafNode.Field::fieldType)
                                .collect(Collectors.toCollection(ArrayList::new)),
                        createdObjects);

        notAssignedCreatedObjects =
                filterAssignedCreatedObjects(
                        new ArrayList<>(variablesMap.values()), notAssignedCreatedObjects);

        notAssignedCreatedObjects =
                filterAssignedCreatedObjects(objectTypes, notAssignedCreatedObjects);
        return notAssignedCreatedObjects;
    }

    private List<String> filterAssignedCreatedObjects(
            List<String> assignedObjects, List<String> createdObjects) {
        List<String> newList = new ArrayList<>();
        Collections.sort(assignedObjects);
        Collections.sort(createdObjects);
        int i = 0;
        int j = 0;

        main:
        while (i < createdObjects.size()) {
            while (j < assignedObjects.size()) {
                if (createdObjects.get(i).equals(assignedObjects.get(j))) {
                    j++;
                } else {
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

    private static String getType(String type) {
        return type.replaceAll("<", "[").replaceAll(">", "]");
    }
}

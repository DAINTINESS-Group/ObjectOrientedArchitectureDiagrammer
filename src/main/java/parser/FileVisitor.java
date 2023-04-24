package parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import model.tree.LeafNode;
import model.tree.NodeType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**This class is responsible for the creation of the AST of a Java source file.
 * Using the ASTNode API it parses the files methods parameters, return types and field declarations
 */
public class FileVisitor {

	private final List<String> allCreatedObjects;
	private final List<String> returnedObjects;
	private final List<String> createdAssignedObjects;

	/** The constructor calls the createAST method that is responsible for the creation
	 * of the AST
     * @param file the Java source file
     * @param leafNode the leaf node representing the Java source file
	 */
	public FileVisitor(File file, LeafNode leafNode){
		allCreatedObjects = new ArrayList<>();
		returnedObjects = new ArrayList<>();
		createdAssignedObjects = new ArrayList<>();
		try {
			createAST(file, leafNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    private void createAST(File file, LeafNode leafNode) throws IOException {
		CompilationUnit compilationUnit = StaticJavaParser.parse(file);

		InheritanceVisitor inheritanceVisitor = new InheritanceVisitor(leafNode);
		inheritanceVisitor.visit(compilationUnit, null);

		VoidVisitor<Void> constructorVisitor = new ConstructorVisitor(leafNode);
		constructorVisitor.visit(compilationUnit, null);

		VoidVisitor<Void> fieldNameVisitor = new FieldVisitor(leafNode);
		fieldNameVisitor.visit(compilationUnit, null);

		VoidVisitor<List<String>> variableVisitor = new VariableVisitor(leafNode);
		variableVisitor.visit(compilationUnit, createdAssignedObjects);

		VoidVisitor<Void> methodNameVisitor = new MethodVisitor(leafNode);
		methodNameVisitor.visit(compilationUnit, null);

		VoidVisitor<List<String>> returnObjectsVisitor = new ReturnObjectsVisitor();
		returnObjectsVisitor.visit(compilationUnit, returnedObjects);

		VoidVisitor<List<String>> objectCreationVisitor = new ObjectCreationVisitor();
		objectCreationVisitor.visit(compilationUnit, allCreatedObjects);
		populateCreatedObjects(leafNode);

		VoidVisitor<Void> enumVisitor = new EnumVisitor(leafNode, inheritanceVisitor.isClassOrInterface());
		enumVisitor.visit(compilationUnit, null);
    }

	private static class InheritanceVisitor extends VoidVisitorAdapter<Void> {
		private final LeafNode leafNode;
		private boolean isClassOrInterface;

		public InheritanceVisitor(LeafNode leafNode) {
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
		private final LeafNode leafNode;

		public ConstructorVisitor(LeafNode leafNode) {
			this.leafNode = leafNode;
		}

		@Override
		public void visit(ConstructorDeclaration constructorDeclaration, Void arg) {
			super.visit(constructorDeclaration, arg);
			leafNode.addMethod(constructorDeclaration.getNameAsString(), "Constructor");
			constructorDeclaration.getParameters().forEach(parameter ->
					leafNode.addMethodParametersType(parameter.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]")));
		}
	}

	private static class FieldVisitor extends VoidVisitorAdapter<Void> {
		private final LeafNode leafNode;

		public FieldVisitor(LeafNode leafNode) {
			this.leafNode = leafNode;
		}

		@Override
		public void visit(FieldDeclaration fieldDeclaration, Void arg) {
			super.visit(fieldDeclaration, arg);
			fieldDeclaration.getVariables().forEach(v ->
					leafNode.addField(v.getNameAsString(),
							v.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"))
			);
		}
	}

	private static class VariableVisitor extends VoidVisitorAdapter<List<String>> {
		private final LeafNode leafNode;

		public VariableVisitor(LeafNode leafNode) {
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
		private final LeafNode leafNode;

		public MethodVisitor(LeafNode leafNode) {
			this.leafNode = leafNode;
		}

		@Override
		public void visit(MethodDeclaration methodDeclaration, Void arg) {
			super.visit(methodDeclaration, arg);
			leafNode.addMethod(methodDeclaration.getNameAsString(),
					methodDeclaration.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]"));
			methodDeclaration.getParameters().forEach(parameter ->
					leafNode.addMethodParametersType(parameter.getTypeAsString().replaceAll("<", "[").replaceAll(">", "]")));
		}
	}

	private static class ReturnObjectsVisitor extends VoidVisitorAdapter<List<String>> {

		@Override
		public void visit(ReturnStmt returnStmt, List<String> returnedObjects) {
			super.visit(returnStmt, returnedObjects);
			Pattern pattern = Pattern.compile("return new ([A-Za-z]+)\\([^)]*\\);");
			Matcher matcher = pattern.matcher(returnStmt.asReturnStmt().toString());
			if (!matcher.matches()) {
				return;
			}
			returnedObjects.add(matcher.group(1).replaceAll("<", "[").replaceAll(">", "]"));
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
		private final LeafNode leafNode;
		private final boolean isClassOrInterface;

		public EnumVisitor(LeafNode leafNode, boolean isClassOrInterface) {
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

	private void populateCreatedObjects(LeafNode leafNode) {
		List<String> notAssignedCreatedObjects = getNotAssignedCreatedObjects(leafNode.getFieldsTypes(), allCreatedObjects);
		notAssignedCreatedObjects = getNotAssignedCreatedObjects(leafNode.getVariablesTypes(), notAssignedCreatedObjects);
		notAssignedCreatedObjects = getNotAssignedCreatedObjects(returnedObjects, notAssignedCreatedObjects);
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

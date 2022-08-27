package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.text.edits.MalformedTreeException;

import model.tree.LeafNode;
import model.tree.PackageNode;

/**This class is responsible for the creation of the AST Tree of a Java source file.
 * Using the ASTNode API it parses the files methods and field declarations
 */
public class FileVisitor {

	private CompilationUnit unit;
	private LeafNode leafNode;
	private String sourceFile[];
	
	/** This method calls the createAST method that is responsible for the creation
	 * of the AST
	 */
	public FileVisitor(File file, LeafNode leafNode, Map<Path, PackageNode> packageNodes){
		try {
			createAST(file, leafNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    private void createAST(File file, LeafNode leafNode) throws IOException, MalformedTreeException {
    	ASTParser parser = ASTParser.newParser(AST.JLS17);
    	this.sourceFile = ReadFileToCharArray(file.getAbsolutePath()).split("\\n");
	    parser.setSource(ReadFileToCharArray(file.getAbsolutePath()).toCharArray());
	    this.unit = (CompilationUnit)parser.createAST(null);
	    this.leafNode = leafNode;
	    processJavaFile();
    }
    
    private  void processJavaFile() throws MalformedTreeException {
	    // to iterate through methods
    	List<AbstractTypeDeclaration> types = new ArrayList<>();
    	for (Object o: unit.types()) {
    		types.add((AbstractTypeDeclaration)(o));
    	}
		if (types.isEmpty()) {
			leafNode.setInheritanceLine(new String[]{"enum"});
		}
	    for (AbstractTypeDeclaration type : types) {
	        if (type.getNodeType() == ASTNode.TYPE_DECLARATION) {
	        	SimpleName typeName = type.getName();
	        	leafNode.setInheritanceLine(convertInheritanceLine(type));
	            List<BodyDeclaration> bodies = new ArrayList<>();
	        	for (Object o: type.bodyDeclarations()) {
	        		bodies.add((BodyDeclaration)(o));
	        	}
	            for (BodyDeclaration body : bodies) {
	            	if (isField(body)) {
						String fieldName = "";
	            		FieldDeclaration field = (FieldDeclaration)body;
	            		List<VariableDeclarationFragment> fragments = new ArrayList<>();
	    	        	for (Object o: field.fragments()) {
	    	        		fragments.add((VariableDeclarationFragment)(o));
	    	        	}
	            		for(VariableDeclarationFragment fragment: fragments) {
							fieldName = fragment.getName().toString();
		            		Map<Object, Object> map = new HashMap<>();
		            		for (Object ob: fragment.properties().keySet()) {
		            			map.put(ob, map.get(ob));
		            		}
		            	}
	            		leafNode.addField(fieldName , field.getType().toString().replaceAll("<", "[").replaceAll(">", "]"));
	            	}
	                if (isMethod(body)) {
	                    MethodDeclaration method = (MethodDeclaration)body;
	                    String methodName = method.getName().getFullyQualifiedName();
	                    Type returnType = method.getReturnType2();
	                    
	                    String returnTypeName;
	                    if (returnType==null) returnTypeName = "Constructor";
	                    else returnTypeName = returnType.toString();
	                    
	                    List<String> parameters = new ArrayList<>();
	                    for (Object parameter : method.parameters()) {
	                        VariableDeclaration variableDeclaration = (VariableDeclaration) parameter;
							String variableType = variableDeclaration.
									getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).toString() + "[]".repeat(Math.max(0, variableDeclaration.getExtraDimensions()));
	                        parameters.add(variableType);
	                    }
	                    
	                    leafNode.addMethodParametersTypes(parameters);
	                    leafNode.addMethod(methodName, returnTypeName.replaceAll("<", "[").replaceAll(">", "]"));
	                }
	            }
	        }
	    }
	}//end processJavaFile

	private String[] convertInheritanceLine(AbstractTypeDeclaration type) {
		String inheritanceLine[] = Arrays.copyOfRange(getInheritanceLine(type), 1, getInheritanceLine(type).length);
		for (int i = 0; i < inheritanceLine.length; i++) {
			inheritanceLine[i] = inheritanceLine[i].trim();
			inheritanceLine[i] = inheritanceLine[i].replace(",", "");
			inheritanceLine[i] = inheritanceLine[i].replace("{", "");
		}
		List<String> list = new ArrayList<>(Arrays.asList(inheritanceLine));
		list.removeAll(Arrays.asList("", null));
		return list.toArray(new String[0]);
	}

	private String[] getInheritanceLine(AbstractTypeDeclaration type) {
		return sourceFile[unit.getLineNumber(type.getName().getStartPosition())-1].split(" ");
	}

	private boolean isMethod(BodyDeclaration body) {
		return body.getNodeType() == ASTNode.METHOD_DECLARATION;
	}

	private boolean isField(BodyDeclaration body) {
		return body.getNodeType() == ASTNode.FIELD_DECLARATION;
	}
	
	private String ReadFileToCharArray(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}//end ReadFileToCharArray
	
}

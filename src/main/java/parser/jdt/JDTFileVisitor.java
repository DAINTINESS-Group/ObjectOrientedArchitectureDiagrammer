package parser.jdt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.tree.jdt.JDTLeafNode;
import model.tree.node.LeafNode;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.text.edits.MalformedTreeException;
import parser.FileVisitor;

import static org.eclipse.jdt.core.dom.ASTNode.METHOD_DECLARATION;

/**This class is responsible for the creation of the AST of a Java source file.
 * Using the ASTNode API it parses the files methods parameters, return types and field declarations
 */
public class JDTFileVisitor implements FileVisitor {

	private CompilationUnit unit;
	private String sourceFile[];


    public void createAST(File file, LeafNode leafNode) {
		try {
			ASTParser parser = ASTParser.newParser(AST.JLS17);
			this.sourceFile = ReadFileToCharArray(file.getAbsolutePath()).split("\\n");
			parser.setSource(ReadFileToCharArray(file.getAbsolutePath()).toCharArray());
			this.unit = (CompilationUnit)parser.createAST(null);
			JDTLeafNode jdtLeafNode = (JDTLeafNode) leafNode;
			processJavaFile(jdtLeafNode);
		} catch (IOException | MalformedTreeException e) {
			e.printStackTrace();
		}
	}
    
    private  void processJavaFile(JDTLeafNode jdtLeafNode) throws MalformedTreeException {
	    // to iterate through methods
    	List<AbstractTypeDeclaration> types = new ArrayList<>();
    	for (Object o: unit.types()) {
    		types.add((AbstractTypeDeclaration)(o));
    	}
		if (types.isEmpty()) {
			jdtLeafNode.setInheritanceLine(new String[]{"enum"});
		}
	    for (AbstractTypeDeclaration type : types) {
	        if (type.getNodeType() == ASTNode.TYPE_DECLARATION) {
				jdtLeafNode.setInheritanceLine(convertInheritanceLine(type));
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
						jdtLeafNode.addField(fieldName, field.getType().toString().replaceAll("<", "[").replaceAll(">", "]"));
	            	}
	                if (isMethod(body)) {
	                    MethodDeclaration method = (MethodDeclaration)body;
	                    String methodName = method.getName().getFullyQualifiedName();
	                    Type returnType = method.getReturnType2();
	                    
	                    String returnTypeName;
	                    if (returnType==null) returnTypeName = "Constructor";
	                    else returnTypeName = returnType.toString();
	                    
	                    for (Object parameter : method.parameters()) {
	                        VariableDeclaration variableDeclaration = (VariableDeclaration) parameter;
							String variableType = variableDeclaration.
									getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY).toString() + "[]".repeat(Math.max(0, variableDeclaration.getExtraDimensions()));
							jdtLeafNode.addMethodParameterType(variableType.replaceAll("<", "[").replaceAll(">", "]"));
	                    }
						jdtLeafNode.addMethod(methodName, returnTypeName.replaceAll("<", "[").replaceAll(">", "]"));
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
		return body.getNodeType() == METHOD_DECLARATION;
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

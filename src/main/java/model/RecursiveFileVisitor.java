package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

public class RecursiveFileVisitor {

	private CompilationUnit unit;
	private LeafNode leafNode;
	
    public void visitAllFiles(File[] files) throws MalformedTreeException, IOException, BadLocationException {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("\n\n\n"
                		+ "Directory: " + file.getAbsolutePath());
                visitAllFiles(file.listFiles()); // Calls same method again.
            } else {
            	String filePath = file.getAbsolutePath();
                System.out.println("\n\nFile: " + filePath + "***");
                //if (filePath.toLowerCase().endsWith("java"))
                	//processJavaFile(file);
            }
        }
    }//end ShowFiles
    
    public void createAST(File file, LeafNode leafNode, List<PackageNode> packageNodes) throws IOException, MalformedTreeException, BadLocationException {
    	System.out.println("File: " + file.getPath());
    	ASTParser parser = ASTParser.newParser(AST.JLS8);
	    parser.setSource(ReadFileToCharArray(file.getAbsolutePath()));
	    this.unit = (CompilationUnit)parser.createAST(null);
	    this.leafNode = leafNode;
	    validatePackageDeclaration(packageNodes);
	    processJavaFile();
    }
    
    private void validatePackageDeclaration(List<PackageNode> packageNodes) throws IOException, MalformedTreeException, BadLocationException {
	    if (!isPackageValid()) {
	    	for(PackageNode p: packageNodes) {
	    		if (p.getName().equals(unit.getPackage().getName().toString())) {
	    			leafNode.setParrentNode(p);
	    		}
	    	}
	    }
    }

	private boolean isPackageValid() {
		return unit.getPackage().getName().toString().equals(leafNode.getParentNode().getName());
	}
    
    private  void processJavaFile() throws IOException, MalformedTreeException, BadLocationException {
    	//TODO check for inheritance
	    // to iterate through methods
	    List<AbstractTypeDeclaration> types = unit.types();
	    for (AbstractTypeDeclaration type : types) {
	        if (type.getNodeType() == ASTNode.TYPE_DECLARATION) {
	        	SimpleName typeName = type.getName();
	        	System.out.println("Type name: " + typeName); 
    			System.out.println("   Type modifiers: " + type.modifiers() );

	            List<BodyDeclaration> bodies = type.bodyDeclarations();
	            for (BodyDeclaration body : bodies) {
	            	
	            	if (isField(body)) {
	            		FieldDeclaration field = (FieldDeclaration)body;
	            		//field.getJavadoc();	            		
	            		List<VariableDeclarationFragment> fragments= field.fragments();
	            		for(VariableDeclarationFragment fragment: fragments) {
		            		System.out.println(" field name: " + fragment.getName().toString());
		            		Map<String, Object> map = fragment.properties();
		            		if (map != null) {
		            			for(String key: map.keySet())
		            				System.out.println(key + " : " + map.get(key).toString());
		            		}
		            	}
	            		leafNode.addFieldType(field.getType().toString());
            			System.out.println("   modifiers: " + field.modifiers() );
            			System.out.println("   type: " + field.getType() );
	            	}
	                if (isMethod(body)) {
	                    MethodDeclaration method = (MethodDeclaration)body;
	                    String methodName = method.getName().getFullyQualifiedName();
	                    Type returnType = method.getReturnType2();
	                    
	                    String returnTypeName;
	                    if (returnType==null) returnTypeName = "Constructor";
	                    else returnTypeName = returnType.toString();
	                    
	                    List<String> parameters = new ArrayList<String>();
	                    for (Object parameter : method.parameters()) {
	                        VariableDeclaration variableDeclaration = (VariableDeclaration) parameter;
	                        String vrblType = variableDeclaration.getStructuralProperty(SingleVariableDeclaration.TYPE_PROPERTY)
	                                .toString();
	                        for (int i = 0; i < variableDeclaration.getExtraDimensions(); i++) {
	                        	vrblType += "[]";
	                        }
	                        parameters.add(vrblType);
	                    }
	                    
	                    

	                    leafNode.addMethodParameterType(parameters);
	                    leafNode.addMethodReturnType(returnTypeName);
	                    System.out.println(" method: " + methodName + " --> " + returnTypeName );
	                    System.out.println("   modifiers: " + method.modifiers() );
	                    System.out.println("   parameters: " + parameters );
	                }
	            }
	        }
	    }
	}//end processJavaFile

	private boolean isMethod(BodyDeclaration body) {
		return body.getNodeType() == ASTNode.METHOD_DECLARATION;
	}

	private boolean isField(BodyDeclaration body) {
		return body.getNodeType() == ASTNode.FIELD_DECLARATION;
	}
	
	private char[] ReadFileToCharArray(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString().toCharArray();	
	}//end ReadFileToCharArray
	
	
	
}

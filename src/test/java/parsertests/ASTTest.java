package parsertests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;
import org.junit.jupiter.api.Test;

import parser.Parser;
import model.PackageNode;
import model.LeafNode;

class ASTTest {
	private Parser parser;
	private List<PackageNode> packages;
	private List<LeafNode> leafs;
	private List<String> methodReturnTypes;
	private List<String> fieldTypes;
	private List<List<String>> methodParameterTypes;
	private List<String> first_parameter;
	private List<String> second_parameter;
	@Test
	void test() throws IOException, MalformedTreeException, BadLocationException, ParseException{
		parser = new Parser("src\\test\\resources\\LatexEditor\\src\\controller\\commands");
		packages = parser.getPackageNodes();
		methodReturnTypes = new ArrayList<>(Arrays.asList("Constructor", "void"));
		fieldTypes = new ArrayList<>(Arrays.asList("VersionsManager"));
		first_parameter = new ArrayList<>(Arrays.asList("VersionsManager"));
		second_parameter = new ArrayList<>(Arrays.asList(""));
		methodParameterTypes = new ArrayList<List<String>>();
		methodParameterTypes.add(first_parameter);
		methodParameterTypes.add(second_parameter);
		for (PackageNode p: packages) {
			if ( p.getName().equals("commands") ) {
				p.getLeafNodes();
				for (LeafNode l: p.getLeafNodes()) {
					if ( l.getName().equals("AddLatexCommand.java") ) {
						List<String> methodReturnTypesTest = new ArrayList<>();
						List<String> fieldTypesTest = new ArrayList<>();
						List<List<String>> methodParameterTypesTest = new ArrayList<List<String>>();
						methodParameterTypesTest = l.getMethodParameterTypes();
						fieldTypesTest = l.getFieldTypes();
						methodReturnTypesTest = l.getMethodReturnTypes();
						Collections.sort(methodReturnTypesTest);
						Collections.sort(methodReturnTypes);
						assertTrue(methodReturnTypesTest.size() == methodReturnTypes.size() 
								&& methodReturnTypes.containsAll(methodReturnTypesTest) 
								&& methodReturnTypesTest.containsAll(methodReturnTypes));
						Collections.sort(fieldTypesTest);
						Collections.sort(fieldTypes);
						assertTrue(fieldTypesTest.size() == fieldTypes.size() 
								&& fieldTypes.containsAll(fieldTypesTest) 
								&& fieldTypesTest.containsAll(fieldTypes));
						System.out.println(methodParameterTypesTest);
						System.out.println(methodParameterTypes);
						for (int i = 0; i < methodParameterTypes.size(); i++) {
							Collections.sort(methodParameterTypesTest.get(i));
							Collections.sort(methodParameterTypes.get(i));
							assertTrue(methodParameterTypesTest.size() == methodParameterTypes.size() 
									&& methodParameterTypes.get(i).containsAll(methodParameterTypesTest.get(i)) 
									&& methodParameterTypes.get(i).containsAll(methodParameterTypesTest.get(i)));
						}
					}
				}
			}
		}
		
	}

}

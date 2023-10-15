package parser.factory;

import parser.javaparser.JavaparserProjectParser;
import parser.jdt.JDTProjectParser;

public class ProjectParserFactory {

	public static Parser createProjectParser(ParserType parserType) {
		if (parserType.equals(ParserType.JDT)) {
			return new JDTProjectParser();
		}
		return new JavaparserProjectParser();
	}
}

package parser.factory;

import parser.javaparser.JavaparserProjectParser;
import parser.jdt.JDTProjectParser;

public class ProjectParserFactory {

    private final ParserType parserType;

    public ProjectParserFactory(ParserType parserType) {
        this.parserType = parserType;
    }

    public Parser createProjectParser() {
        if (parserType.equals(ParserType.JDT)) {
            return new JDTProjectParser();
        }

        return new JavaparserProjectParser();
    }
}

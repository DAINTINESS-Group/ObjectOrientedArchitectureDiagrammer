package parser.factory;

import parser.ProjectParser;

public class ProjectParserFactory {

    public static Parser createProjectParser(ParserType parserType) {
        if (parserType.equals(ParserType.JDT)) {
            throw new IllegalArgumentException(ParserType.JDT + " is not supported");
        }

        return new ProjectParser();
    }
}

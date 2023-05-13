package parser;

import parser.javaparser.JavaparserProjectParser;
import parser.jdt.JDTProjectParser;

public class ParserFactory {

    private final ParserType parserType;

    public ParserFactory(ParserType parserType) {
        this.parserType = parserType;
    }

    public ProjectParserHelper createParser() {
        if (parserType.equals(ParserType.JDT)) {
            return new JDTProjectParser();
        }else {
            return new JavaparserProjectParser();
        }
    }

}

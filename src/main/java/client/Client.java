package client;

import java.io.IOException;
import java.text.ParseException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

import parser.Parser;

public class Client {

	public static void main(String[] args) throws IOException, MalformedTreeException, BadLocationException, ParseException{
		Parser parser = new Parser(args[0]);
	}

}

package controller;

import parser.Parser;

public class Controller {
	
	public Controller(String sourcePackagePath) {
		createTree(sourcePackagePath);
	}
	
	private void createTree(String sourcePackagePath) {
		new Parser(sourcePackagePath);
	}
}

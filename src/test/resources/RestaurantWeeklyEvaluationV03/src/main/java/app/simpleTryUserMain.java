package app;

import java.io.IOException;

import controller.IMainEngine;
import controller.MainEngineFactory;

public final class simpleTryUserMain {

	public static void main(String[] args) {
		
		try {
			System.err.println("#### " + new java.io.File(".").getCanonicalPath() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MainEngineFactory mainEngineFactory = new MainEngineFactory();
		IMainEngine mainEngine  = mainEngineFactory.createMainEngine();
		
		int _loadFlag;
		_loadFlag = mainEngine.loadAllData();
		mainEngine.computeAllStats();
		System.out.println("Loading returned: " + _loadFlag + "\n");
	
		mainEngine.createReports("ReportGeneratorForFiles");
		mainEngine.createReports("ReportGeneratorForHTML");
		

	}
}

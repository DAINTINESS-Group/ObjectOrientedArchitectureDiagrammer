package tests;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import engine.MainEngine;


public class MainEngineTest {
	private MainEngine mainEngine;
	private int timelineSize;
	private int numPhases; 
	
	@Before
	public void setUp() throws Exception {
		mainEngine = new MainEngine();
		numPhases = 0;
		timelineSize = 0;
	}

	@Test
	public final void testSetTimeLine() {
		timelineSize = mainEngine.setTimeLine("input_test.txt");		
		assertEquals(6, timelineSize); 
	}

	@Test
	public final void testProducePhases() {
		mainEngine.setTimeLine("input_test.txt");
		numPhases = mainEngine.producePhases();
		
		assertEquals(4, numPhases); 
		//fail("Not yet implemented"); 
	}

	@Test
	public final void testHtmlVisualize() {
//		char referenceRaster[][] = {{'v', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{'a', '|', ' ', ' ', ' ', ' ', ' ', ' ', '@', ' ', ' ', ' ', ' ', ' ', ' '},
//		{'l', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{'u', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{'e', '|', ' ', ' ', ' ', '@', ' ', '@', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{' ', '|', ' ', ' ', ' ', ' ', '@', ' ', '@', ' ', ' ', ' ', ' ', ' ', ' '},
//		{' ', '|', ' ', ' ', '@', ' ', ' ', ' ', ' ', '@', '@', ' ', ' ', ' ', ' '},
//		{' ', '|', '@', '@', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '@', '@', ' ', ' '},
//		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '@', ' '},
//		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
//		{'~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~', '~'},
//		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'T', 'i', 'm', 'e'}};

		char referenceRaster[][] = {{'v', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'a', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'l', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'u', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'e', '|', ' ', ' ', ' ', '@', ' ', '@'},
		{' ', '|', ' ', ' ', ' ', ' ', '@', ' '},
		{' ', '|', ' ', ' ', '@', ' ', ' ', ' '},
		{' ', '|', '@', '@', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'~', '~', '~', '~', '~', '~', '~', '~'},
		{' ', '|', ' ', ' ', 'T', 'i', 'm', 'e'}};

		timelineSize = mainEngine.setTimeLine("input_test.txt");
		mainEngine.setVisualizer("HtmlVisualizer");
		char actualResult [][] = mainEngine.visualize();

//		for (int i=0; i< actualResult.length; i++) {
//			for(int j = 0; j < actualResult[0].length; j++ )
//				System.out.print(actualResult[i][j]);
//			System.out.println();
//		}
		Boolean testSuccess = true;
		for (int i=0; i< referenceRaster.length; i++)
			for(int j = 0; j < referenceRaster[0].length; j++ )
				if (actualResult[i][j] != referenceRaster[i][j])
					testSuccess = false;
		assertTrue(testSuccess);
	}

	
	@Test
	public final void testConsolelVisualize() {
		char referenceRaster[][] = {{'v', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'a', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'l', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'u', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'e', '|', ' ', ' ', ' ', '@', ' ', '@'},
		{' ', '|', ' ', ' ', ' ', ' ', '@', ' '},
		{' ', '|', ' ', ' ', '@', ' ', ' ', ' '},
		{' ', '|', '@', '@', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{' ', '|', ' ', ' ', ' ', ' ', ' ', ' '},
		{'~', '~', '~', '~', '~', '~', '~', '~'},
		{' ', '|', ' ', ' ', 'T', 'i', 'm', 'e'}};

		timelineSize = mainEngine.setTimeLine("input_test.txt");
		mainEngine.setVisualizer("ConsoleVisualizer");
		char actualResult [][] = mainEngine.visualize();

		Boolean testSuccess = true;
		for (int i=0; i< referenceRaster.length; i++)
			for(int j = 0; j < referenceRaster[0].length; j++ )
				if (actualResult[i][j] != referenceRaster[i][j])
					testSuccess = false;
		assertTrue(testSuccess);
	}

	
	@Test
	public final void testReportPhases() {
		mainEngine.setTimeLine("input_test.txt");
		numPhases = mainEngine.producePhases();
		List<String> als = mainEngine.reportPhases();
		
		assertEquals(numPhases, als.size());
		//fail("Not yet implemented"); 
	}	
}//end class

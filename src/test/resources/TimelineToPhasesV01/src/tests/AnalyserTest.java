package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import parsing.IParser;
import parsing.ParserFactory;
import commons.TimeLine;
import analysis.AnalyserFactory;
import analysis.IAnalyser;
import analysis.NaiveAnalyser;

public class AnalyserTest {
	
	private AnalyserFactory analyserFactory;
	
	@Before
	public void setUp() throws Exception {
		analyserFactory = new AnalyserFactory();
	}

	@Test
	public void testAnalyzerCreation() {
		IAnalyser analyser = analyserFactory.createAnalyser("NaiveAnalyser");
		assertNotNull(analyser);
		assertTrue(analyser instanceof NaiveAnalyser);
	}

	@Test
	public void testPhaseProduction() {
		ParserFactory parserFactory = new ParserFactory();
		IParser parser = parserFactory.createParser("SimpleTextParser");
		// assertNotNull(parser);		
		//we tested this at ParserTest; if the test fails it should be due to the analyzer object that we test
		
		TimeLine tl = parser.parse("input_test.txt");
		IAnalyser analyser = analyserFactory.createAnalyser("NaiveAnalyser");
		assertEquals(4,analyser.producePhasesFromTimeLine(tl).size());
	}
}

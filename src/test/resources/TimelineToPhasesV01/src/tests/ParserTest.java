package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import commons.TimeLine;
import commons.ValuePair;
import parsing.IParser;
import parsing.ParserFactory;
import parsing.SimpleTextParser;
import parsing.TestParser;



public class ParserTest {
	
	private ParserFactory parserFactory;
	
	@Before
	public void setUp() throws Exception {
		parserFactory = new ParserFactory();
	}

	@Test
	public void testParserSimpleParserCreation() {
		IParser parser = parserFactory.createParser("SimpleTextParser");
		assertNotNull(parser);
		assertTrue(parser instanceof SimpleTextParser);
	}
	@Test
	public void testParserDummyParserCreation() {
		IParser parser = parserFactory.createParser("TestParser");
		assertNotNull(parser);
		assertTrue(parser instanceof TestParser);
	}

	@Test
	public void testParser() {
		IParser parser = parserFactory.createParser("SimpleTextParser");
		TimeLine tl = parser.parse("input_test.txt");
		assertNotNull(tl);
		
		assertEquals(6,tl.getValues().size());
		
		for(ValuePair pair: tl.getValues()){
			assertNotEquals(-1, pair.getPos());
			assertNotEquals(-1, pair.getX());
			assertNotEquals(-1, pair.getY());
		}
	}
}

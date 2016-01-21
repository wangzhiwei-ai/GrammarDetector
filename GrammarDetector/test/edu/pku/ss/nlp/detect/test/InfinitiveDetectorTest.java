package edu.pku.ss.nlp.detect.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.pku.ss.nlp.detect.InfinitiveDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.Infinitive;
import edu.pku.ss.nlp.units.LanguageUnit;

public class InfinitiveDetectorTest {
	@Test
	public void detectGrammarTest() {
		String sent1 = "";// empty
		String sent2 = null;// null
		String sent3 = "Would you like to go to the movies?";// no gerund.
																// one
																// gerunds.
		InfinitiveDetector detector = new InfinitiveDetector();

		// 1. test empty sentence.
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent1)));

		// 2. sentence is null.
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent2)));

		// 3. sentence contains no gerund.
		grammarMap.put(new TokenPosition(4, 6), Infinitive.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent3)));

	}
}

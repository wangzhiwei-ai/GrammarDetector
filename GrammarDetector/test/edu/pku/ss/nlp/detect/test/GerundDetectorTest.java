package edu.pku.ss.nlp.detect.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.pku.ss.nlp.detect.GerundDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.Gerund;
import edu.pku.ss.nlp.units.LanguageUnit;

public class GerundDetectorTest {
	@Test
	public void detectGrammarTest() {
		String sent1 = "";// empty
		String sent2 = null;// null
		String sent3 = "I love you.";// no gerund.
		String sent4 = "I enjoy swimming.";// one gerund.
		String sent5 = "I enjoy swimming and playing basketball.";// more than
																	// one
																	// gerunds.
		GerundDetector detector = new GerundDetector();

		// 1. test empty sentence.
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent1)));

		// 2. sentence is null.
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent2)));

		// 3. sentence contains no gerund.
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent3)));

		// 4. sentence contains one gerund.
		grammarMap.put(new TokenPosition(3), Gerund.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent4)));

		// 5. sentence contains more than one gerund.
		grammarMap.clear();
		grammarMap.put(new TokenPosition(3), Gerund.createDefault());
		grammarMap.put(new TokenPosition(5), Gerund.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent5)));
	}
}

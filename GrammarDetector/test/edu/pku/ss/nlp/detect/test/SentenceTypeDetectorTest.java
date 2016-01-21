package edu.pku.ss.nlp.detect.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.pku.ss.nlp.detect.SentenceTypeDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.SentenceType;
import edu.pku.ss.nlp.units.LanguageUnit;

public class SentenceTypeDetectorTest {
	@Test
	public void detectGrammarTest() {
		String sent1 = "";// empty
		String sent2 = null;// null
		String sent3 = "I love you.";
		String sent4 = "I love you !";
		String sent5 = "do you love me ?";
		SentenceTypeDetector detector = new SentenceTypeDetector();

		// 1. test empty sentence.
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent1)));

		// 2. sentence is null.
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent2)));

		// 3. sentence contains no gerund.
		grammarMap.put(new TokenPosition(-1), new SentenceType("陈述句"));
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent3)));

		// 4. sentence contains one gerund.
		grammarMap.clear();
		grammarMap.put(new TokenPosition(-1), new SentenceType("感叹句"));
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent4)));

		// 5. sentence contains more than one gerund.
		grammarMap.clear();
		grammarMap.put(new TokenPosition(-1), new SentenceType("疑问句"));
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent5)));
	}
}

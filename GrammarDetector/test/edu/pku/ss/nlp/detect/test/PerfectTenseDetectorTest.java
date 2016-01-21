package edu.pku.ss.nlp.detect.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.pku.ss.nlp.detect.PerfectTenseDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.PerfectTense;
import edu.pku.ss.nlp.units.LanguageUnit;

public class PerfectTenseDetectorTest {
	@Test
	public void detectGrammarTest() {
		String sent1 = "";// empty
		String sent2 = null;// null
		String sent3 = "I have known Paul since we were children.";
		String sent4 = "He hasn't been here since last night.";
		String sent5 = "Have you been here since last night?";
		String sent6 = "I haven’t yet decided whether I will attend the party.";
		String sent7 = "My father has given up alcohol.";
		String sent8 = "Henry has just finished his homework.";
		String sent9 = "The man had run away when the police arrived.";

		PerfectTenseDetector detector = new PerfectTenseDetector();

		// 1. test empty sentence.
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent1)));

		// 2. sentence is null.
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent2)));

		// 3. have known
		grammarMap.put(new TokenPosition(2, 4), PerfectTense.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent3)));

		// 4. hasn't been
		grammarMap.clear();
		grammarMap.put(new TokenPosition(2, 5), PerfectTense.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent4)));

		// 5. Have you been
		grammarMap.clear();
		grammarMap.put(new TokenPosition(1, 4), PerfectTense.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent5)));

		// 6. haven’t
		grammarMap.clear();
		grammarMap.put(new TokenPosition(2, 6), PerfectTense.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent6)));

		// 7. has given
		grammarMap.clear();
		grammarMap.put(new TokenPosition(3, 5), PerfectTense.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent7)));

		// 8. has just finished
		grammarMap.clear();
		grammarMap.put(new TokenPosition(2, 5), PerfectTense.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent8)));

		// 9. had run
		grammarMap.clear();
		grammarMap.put(new TokenPosition(3, 5), PerfectTense.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent9)));
	}
}

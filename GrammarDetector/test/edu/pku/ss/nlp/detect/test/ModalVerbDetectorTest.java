package edu.pku.ss.nlp.detect.test;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.pku.ss.nlp.detect.ModalVerbDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.ModalVerb;
import edu.pku.ss.nlp.units.LanguageUnit;

public class ModalVerbDetectorTest {
	@Test
	public void detectGrammarTest() {
		String sent1 = "";// empty
		String sent2 = null;// null
		String sent3 = "You can enter if you have a ticket.";// can
		String sent4 = "ICaren can’t be home now.";// can
		String sent5 = "May I use your bathroom?";// may
		String sent6 = "I must attend the meeting.";// must
		String sent7 = "You should exercise every day.";// should
		String sent8 = "I will do my best in my new job.";
		String sent9 = "Shall I buy some milk?";
		ModalVerbDetector detector = new ModalVerbDetector();

		// 1. test empty sentence.
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent1)));

		// 2. sentence is null.
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent2)));

		// 3. can
		grammarMap.put(new TokenPosition(2), ModalVerb.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent3)));

		// 4. can
		grammarMap.clear();
		grammarMap.put(new TokenPosition(2), ModalVerb.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent4)));

		// 5. may
		grammarMap.clear();
		grammarMap.put(new TokenPosition(1), ModalVerb.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent5)));

		// 6. must
		grammarMap.clear();
		grammarMap.put(new TokenPosition(2), ModalVerb.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent6)));
		// 7. should
		grammarMap.clear();
		grammarMap.put(new TokenPosition(2), ModalVerb.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent7)));

		// 8. will
		grammarMap.clear();
		grammarMap.put(new TokenPosition(2), ModalVerb.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent8)));

		// 9. shall
		grammarMap.clear();
		grammarMap.put(new TokenPosition(1), ModalVerb.createDefault());
		assertEquals(grammarMap,
				detector.detectGrammar(new LanguageUnit(sent9)));
	}
}

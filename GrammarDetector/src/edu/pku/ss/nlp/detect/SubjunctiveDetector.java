package edu.pku.ss.nlp.detect;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.ClauseType;
import edu.pku.ss.nlp.grammar.Comparative;
import edu.pku.ss.nlp.toolkit.ExtractionFeature;
import edu.pku.ss.nlp.toolkit.LibSvmModel;
import edu.pku.ss.nlp.units.LanguageUnit;

public class SubjunctiveDetector implements Detectable {
	private Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
	private final String grammarName = "虚拟语气";

	public void AddGrammarMap(int startIndex, int endIndex, String grammarName) {
		this.grammarMap.put(new TokenPosition(startIndex, endIndex),
				new Comparative(grammarName));
	}

	@SuppressWarnings("unused")
	public boolean RecognizeSubjunctive(LanguageUnit unit) {
		String sentence = Join(unit.getTokenList());
		ExtractionFeature ef = new ExtractionFeature();
		String testIntance = ef.MakeInstance(sentence);

		boolean flag = true;
		LibSvmModel libsvm = new LibSvmModel(testIntance);
		double result = libsvm.testInstance();

		if (result == -1.0 || result == 0.0) {
			return false;
		} else {
			return true;
		}
	}

	private String Join(List<String> tokenList) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokenList.size(); i++) {
			sb.append(tokenList.get(i) + " ");
		}
		return sb.toString();
	}

	@Override
	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		if (unit.getTokenList() == null || unit == null
				|| unit.getTokenList().isEmpty())
			return null;

		grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		if (RecognizeSubjunctive(unit) == true)
			this.grammarMap.put(new TokenPosition(-1), new ClauseType(
					this.grammarName));

		return Collections.unmodifiableMap(grammarMap);
	}
}

package edu.pku.ss.nlp.detect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.pku.ss.nlp.format.JsonFormatter;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.Gerund;
import edu.pku.ss.nlp.units.LanguageUnit;

public class GerundDetector implements Detectable {

	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		if (unit.isEmpty())
			return grammarMap;
		List<String> posList = unit.getPOSList();
		for (int i = 0; i < posList.size(); ++i) {
			if (posList.get(i).equals("VBG")) {
				grammarMap.put(new TokenPosition(i + 1, i + 2),
						Gerund.createDefault());
			}
		}

		return grammarMap;
	}

	public static void main(String[] args) {
		LanguageUnit lu = new LanguageUnit("Auto-stor uses a re-direct method via symbolic links that is adequate for viewing files , but slow when retrieving them .");
		GerundDetector detector = new GerundDetector();
		Map<TokenPosition, BaseGrammar> grammarMap = detector.detectGrammar(lu);
		Map<TokenPosition, Set<BaseGrammar>> map = new HashMap<TokenPosition, Set<BaseGrammar>>();
		for (TokenPosition tp : grammarMap.keySet()) {
			Set<BaseGrammar> set = new HashSet<BaseGrammar>();
			set.add(grammarMap.get(tp));
			map.put(tp, set);
		}
		// System.out.println(grammarMap.size());
		System.out.println(JsonFormatter.formatToJson(map, lu));
		// System.out.println(JsonFormatter.formatToJson(grammarMap, lu));
	}

}

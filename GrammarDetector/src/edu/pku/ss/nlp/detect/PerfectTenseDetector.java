package edu.pku.ss.nlp.detect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.pku.ss.nlp.format.JsonFormatter;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.PerfectTense;
import edu.pku.ss.nlp.units.LanguageUnit;

public class PerfectTenseDetector implements Detectable {

	@Override
	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		if (unit.size() < 2)
			return grammarMap;

		List<String> lemmaList = unit.getLemmaList();
		List<String> posList = unit.getPOSList();
		if (!lemmaList.contains("have"))
			return grammarMap;

		int size = unit.size();
		String lemma, pos;
		for (int i = size - 1; i >= 0;) {
			pos = posList.get(i);
			if (pos.equals("VBN") || pos.equals("VBD")) {
				int j = i - 1;

				for (; j >= 0; --j) {
					lemma = lemmaList.get(j);

					if (lemma.equals("have")) {
						grammarMap.put(new TokenPosition(j + 1, i + 2),
								PerfectTense.createDefault());
						i = j - 1;
						break;
					} else if ("VBN".equals(posList.get(j))
							|| "VBD".equals(posList.get(j))) {
						i = j;
						break;
					}
				}
				if (j == -1)
					i--;

			} else {
				i--;
			}
		}

		return grammarMap;
	}

	public static void main(String[] args) {
		LanguageUnit lu = new LanguageUnit(
				"The man had run away when the police arrived.");
		PerfectTenseDetector detector = new PerfectTenseDetector();
		Map<TokenPosition, BaseGrammar> grammarMap = detector.detectGrammar(lu);
		Map<TokenPosition, Set<BaseGrammar>> map = new HashMap<TokenPosition, Set<BaseGrammar>>();
		for (TokenPosition tp : grammarMap.keySet()) {
			Set<BaseGrammar> set = new HashSet<BaseGrammar>();
			set.add(grammarMap.get(tp));
			map.put(tp, set);
		}
		System.out.println(JsonFormatter.formatToJson(map, lu));
	}
}

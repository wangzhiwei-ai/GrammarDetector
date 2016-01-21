package edu.pku.ss.nlp.detect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.pku.ss.nlp.format.JsonFormatter;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.ModalVerb;
import edu.pku.ss.nlp.units.LanguageUnit;

/**
 * 情态动词检测.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:36:35
 */
public class ModalVerbDetector implements Detectable {

	private Set<String> modalVerbSet;

	public ModalVerbDetector() {
		this.modalVerbSet = new HashSet<String>();
		this.modalVerbSet.add("can");
		this.modalVerbSet.add("shall");
		this.modalVerbSet.add("may");
		this.modalVerbSet.add("must");
		this.modalVerbSet.add("should");
		this.modalVerbSet.add("will");
		this.modalVerbSet.add("would");
	}

	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		if (unit.isEmpty())
			return grammarMap;

		List<String> posList = unit.getPOSList();
		int size = posList.size();
		String pos;
		for (int i = 0; i < size; ++i) {
			pos = posList.get(i);
			if (pos.equals("MD")) {
				grammarMap.put(new TokenPosition(i + 1), new ModalVerb("情态动词"));
			}
		}

		List<String> lemmaList = unit.getLemmaList();
		size = lemmaList.size();
		String lemma;
		for (int i = 0; i < size; ++i) {
			lemma = lemmaList.get(i).toLowerCase();
			if (this.modalVerbSet.contains(lemma)) {
				grammarMap.put(new TokenPosition(i + 1), new ModalVerb("情态动词"));
			}
		}
		return grammarMap;
	}

	public static void main(String[] args) {
		String sent = "Shall I buy some milk?";
		LanguageUnit lu = new LanguageUnit(sent);
		ModalVerbDetector detector = new ModalVerbDetector();
		Map<TokenPosition, BaseGrammar> gm = detector.detectGrammar(lu);

		Map<TokenPosition, Set<BaseGrammar>> map = new HashMap<TokenPosition, Set<BaseGrammar>>();
		for (TokenPosition tp : gm.keySet()) {
			Set<BaseGrammar> set = new HashSet<BaseGrammar>();
			set.add(gm.get(tp));
			map.put(tp, set);
		}

		System.out.println(JsonFormatter.formatToJson(map, lu));
	}

}

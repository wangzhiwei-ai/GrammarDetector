package edu.pku.ss.nlp.detect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.Infinitive;
import edu.pku.ss.nlp.units.LanguageUnit;

/**
 * 不定式检测器.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:33:52
 */
public class InfinitiveDetector implements Detectable {

	/**
	 * Time complexity: O(n)
	 */
	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		if (unit.size() < 2)
			return grammarMap;

		String curPOS, nextPOS;
		List<String> posList = unit.getPOSList();
		int i = 0;
		while (i < posList.size() - 1) {
			curPOS = posList.get(i);
			nextPOS = posList.get(i + 1);
			if (curPOS.equals("TO") && nextPOS.equals("VB")) {
				grammarMap.put(new TokenPosition(i + 1, i + 3),
						Infinitive.createDefault());
				i += 2;
			} else {
				i++;
			}
		}

		return grammarMap;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		LanguageUnit lu = new LanguageUnit("I want to go to Beijing.");
		InfinitiveDetector detector = new InfinitiveDetector();
		Map<TokenPosition, BaseGrammar> grammarMap = detector.detectGrammar(lu);
		// System.out.println(JsonFormatter.formatToJson(grammarMap, lu));
	}

}

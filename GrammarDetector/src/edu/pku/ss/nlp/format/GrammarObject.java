package edu.pku.ss.nlp.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import edu.pku.ss.nlp.detect.LexiconDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;

public class GrammarObject {
	private Map<List<String>, List<String>> tokensGrammarsMap;

	public static GrammarObject createInstance(
			Map<TokenPosition, Set<BaseGrammar>> gm, LanguageUnit lu) {
		return new GrammarObject(gm, lu);
	}

	public GrammarObject(Map<TokenPosition, Set<BaseGrammar>> gm,
			LanguageUnit lu) {
		this.tokensGrammarsMap = new HashMap<List<String>, List<String>>();

		List<String> tokens = lu.getTokenList();
		for (TokenPosition tp : gm.keySet()) {
			List<String> correspondTokens = convertTokenPositionToTokens(tp,
					tokens);
			Set<BaseGrammar> grammarSet = gm.get(tp);
			List<String> grammarDesList = convertGrammarSetToDesList(grammarSet);
			this.tokensGrammarsMap.put(correspondTokens, grammarDesList);
		}
	}

	private List<String> convertTokenPositionToTokens(TokenPosition tp,
			List<String> tokens) {
		List<String> convertedTokens = new ArrayList<String>();
		List<Integer> indexList = tp.getIndexList();

		if (indexList.size() == 1 && indexList.get(0) == -1) {
			convertedTokens.add("-1_ALL");
			return convertedTokens;
		}

		for (int index : indexList) {
			// System.out.println(index);

			convertedTokens.add(index + "_" + tokens.get(index - 1));
		}

		return convertedTokens;
	}

	private List<String> convertGrammarSetToDesList(Set<BaseGrammar> grammarSet) {
		List<String> grammarDesList = new ArrayList<String>();
		for (BaseGrammar grammar : grammarSet) {
			grammarDesList.add(grammar.toString());
		}

		return grammarDesList;
	}

	public static void main(String[] args) {
		LexiconDetector ld = new LexiconDetector();
		LanguageUnit unit = new LanguageUnit("I love you !");
		Map<TokenPosition, Set<BaseGrammar>> grammarMap = ld
				.detectGrammar(unit);
		GrammarObject go = GrammarObject.createInstance(grammarMap, unit);
		Gson gson = new Gson();
		System.out.println(gson.toJson(go));
	}
}

package edu.pku.ss.nlp.format;

import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;

public class JsonFormatter {
	public static String formatToJson(Map<TokenPosition, Set<BaseGrammar>> gm,
			LanguageUnit lu) {
		GrammarObject go = GrammarObject.createInstance(gm, lu);
		Gson gson = new Gson();
		return gson.toJson(go);
	}
}

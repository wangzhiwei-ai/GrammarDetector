package edu.pku.ss.nlp.detect;

import java.util.Map;
import java.util.Set;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;

public interface MultipleDetectable {
	public Map<TokenPosition, Set<BaseGrammar>> detectGrammar(LanguageUnit unit);
}

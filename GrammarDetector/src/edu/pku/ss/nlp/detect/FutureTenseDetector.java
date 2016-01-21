package edu.pku.ss.nlp.detect;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;

import java.util.Map;

/**
 * Created by zhiwei on 16/1/19.
 */
public class FutureTenseDetector implements Detectable {
    @Override
    public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
        return null;
    }
}

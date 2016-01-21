package edu.pku.ss.nlp.detect;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.PastParticiple;
import edu.pku.ss.nlp.units.LanguageUnit;
import edu.pku.ss.nlp.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhiwei on 16/1/21.
 */
public class PastParticipleDetector implements Detectable {
    @Override
    public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
        Map<TokenPosition, BaseGrammar> map = new HashMap<>();
        if (null == unit || unit.isEmpty()) return map;

        this.doDetectGrammar(unit, map);

        return map;
    }

    private void doDetectGrammar(LanguageUnit unit, Map<TokenPosition, BaseGrammar> map) {
        int size = unit.size();
        List<String> posList = unit.getPOSList();
        for (int i = 0; i < size; ++i) {
            String pos = posList.get(i);
            if (Constants.POSTag.VBN.equals(pos)) {
                map.put(new TokenPosition(i), PastParticiple.createDefault());
            }
        }
    }


    public static void main(String[] args) {

    }
}

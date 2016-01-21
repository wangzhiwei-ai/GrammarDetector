package edu.pku.ss.nlp.detect;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.CountGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;
import edu.pku.ss.nlp.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检测当前句子中的名词是可数名词还是不可数名词，只处理词性标记是NN或者是NNS两种情况.
 * Created by zhiwei on 16/1/21.
 */
public class CountNounDetector implements Detectable {
    @Override
    public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
        Map<TokenPosition, BaseGrammar> map = new HashMap<>();
        if (null == unit || unit.isEmpty()) return map;

        //step1. 如果名词的词性是复数标记NNS，那么在当前句子中一定是可数名词.
        this.detectNNS(unit, map);


        return map;
    }

    private void detectNNS(LanguageUnit unit, Map<TokenPosition, BaseGrammar> map) {
        List<String> posList = unit.getPOSList();
        int size = posList.size();
        for (int i = 0; i < size; ++i) {
            String pos = posList.get(i);
            if (Constants.POSTag.NNS.equals(pos)) {
                map.put(new TokenPosition(i), CountGrammar.createCountableGrammar());
            }
        }
    }

    /**
     * 冠词a,an后面接的第一个名词，如果是NN那么该名词一定是可数名词.
     *
     * @param unit
     * @param map
     */
    public void detectDetNoun(LanguageUnit unit, Map<TokenPosition, BaseGrammar> map) {
        int size = unit.size();
        if (size < 2) return;

        List<String> lowerTokenList = new ArrayList<>();
        for (String token : unit.getTokenList()) {
            lowerTokenList.add(token.toLowerCase());
        }

        List<String> posList = unit.getPOSList();
        //递归检测可数名词.
        int start = 0, end = size;
        String pos = null;
        String token = null;
        while (start < end) {
            token = lowerTokenList.get(start);
            if (Constants.Determiner.A.equals(token) || Constants.Determiner.AN.equals(token)) {
                for (int i = start + 1; i < end; ++i) {
                    pos = posList.get(i);
                    if (Constants.POSTag.NN.equals(pos)) {
                        map.put(new TokenPosition(i), CountGrammar.createCountableGrammar());
                        start = i + 1;
                        break;
                    }

                }
            } else {
                ++start;
            }
        }
    }

    /**
     * 如果介词(词性为IN)后面第一个NN，那么该NN是不可数名词.
     *
     * @param unit
     * @param map
     */
    public void detectUncountableNoun(LanguageUnit unit, Map<TokenPosition, BaseGrammar> map) {
        int size = unit.size();
        if (size < 2) return;

        List<String> posList = unit.getPOSList();
        //递归检测可数名词.
        int start = 0, end = size;
        String pos = null;
        while (start < end) {
            pos = posList.get(start);
            if (Constants.POSTag.IN.equals(pos)) {
                for (int i = start + 1; i < end; ++i) {
                    pos = posList.get(i);
                    if (Constants.POSTag.NN.equals(pos)) {
                        map.put(new TokenPosition(i), CountGrammar.createUncountableGrammar());
                        start = i + 1;
                        break;
                    }

                }
            } else {
                ++start;
            }
        }
    }


    public static void main(String[] args) {
        String sentence = "Why does it feel like the most beautiful girl in the world is in this room?";
        LanguageUnit unit = new LanguageUnit(sentence);
        Map<TokenPosition, BaseGrammar> map = new HashMap<>();
        CountNounDetector cnd = new CountNounDetector();
        cnd.detectUncountableNoun(unit, map);

        for (TokenPosition tp : map.keySet()) {
            System.out.println(tp + "\t" + map.get(tp));
        }
    }
}

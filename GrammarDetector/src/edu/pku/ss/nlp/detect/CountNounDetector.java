package edu.pku.ss.nlp.detect;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.CountGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;
import edu.pku.ss.nlp.utils.Constants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 检测当前句子中的名词是可数名词还是不可数名词，只处理词性标记是NN或者是NNS两种情况.
 * Created by zhiwei on 16/1/21.
 */
public class CountNounDetector implements Detectable {

    private Set<String> dtCountableSet;

    public CountNounDetector() {
        this.init();
    }

    private void init() {
        try {
            this.dtCountableSet = new HashSet<>(FileUtils.readLines(new File(Constants.CountNounDetector.TOKEN_COUNTABLE_PATH)));
            System.out.println(this.dtCountableSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
        Map<TokenPosition, BaseGrammar> map = new HashMap<>();
        if (null == unit || unit.isEmpty()) return map;

        //step1. 如果名词的词性是复数标记NNS，那么在当前句子中一定是可数名词.
        this.detectNNS(unit, map);
        this.detectDetNoun(unit, map);

        this.addDefaultUncountableNoun(unit, map);

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
            start++;
            if (this.dtCountableSet.contains(token)) {
                for (int i = start; i < end; ++i) {
                    pos = posList.get(i);
                    if (Constants.POSTag.NN.equals(pos)) {
                        map.put(new TokenPosition(i), CountGrammar.createCountableGrammar());
                        start = i;
                        break;
                    }

                }
            }
        }
    }

    /**
     * 不满足上述三个条件的词性标记为NN的名词,默认为不可数名词.
     *
     * @param unit
     * @param map
     */
    public void addDefaultUncountableNoun(LanguageUnit unit, Map<TokenPosition, BaseGrammar> map) {
        List<String> posList = unit.getPOSList();
        int size = unit.size();
        String pos = null;
        for (int i = 0; i < size; ++i) {
            pos = posList.get(i);
            if (Constants.POSTag.NN.equals(pos)) {
                TokenPosition tp = new TokenPosition(i);
                if (!map.containsKey(tp)) {
                    map.put(tp, CountGrammar.createUncountableGrammar());
                }
            }
        }
    }


    public static void main(String[] args) {
        String sentence = "How can we increase our happiness?";
        LanguageUnit unit = new LanguageUnit(sentence);
        CountNounDetector cnd = new CountNounDetector();
        Map<TokenPosition, BaseGrammar> map = cnd.detectGrammar(unit);

        for (TokenPosition tp : map.keySet()) {
            System.out.println(tp + "\t" + map.get(tp));
        }
    }
}

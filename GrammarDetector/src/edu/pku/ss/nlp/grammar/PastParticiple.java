package edu.pku.ss.nlp.grammar;

/**
 * Created by zhiwei on 16/1/21.
 */
public class PastParticiple extends BaseGrammar {
    public PastParticiple(String name) {
        super(name);
    }

    public static PastParticiple createDefault() {
        return new PastParticiple("过去分词");
    }
}

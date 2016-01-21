package edu.pku.ss.nlp.grammar;

/**
 * Created by zhiwei on 16/1/19.
 */
public class Participle extends BaseGrammar {
    public Participle(String name) {
        super(name);
    }

    public static Participle createPresentParticiple() {
        return new Participle("现在分词");
    }

    public static Participle createPastParticiple() {
        return new Participle("过去分词");
    }
}

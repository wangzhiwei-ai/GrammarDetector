package edu.pku.ss.nlp.grammar;

/**
 * Created by zhiwei on 16/1/19.
 */
public class PastTense extends BaseGrammar {
    public PastTense(String name) {
        super(name);
    }

    public static PastTense createDefault() {
        return new PastTense("过去时");
    }
}

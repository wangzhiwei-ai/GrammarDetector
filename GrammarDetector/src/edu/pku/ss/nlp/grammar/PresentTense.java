package edu.pku.ss.nlp.grammar;

/**
 * Created by zhiwei on 16/1/19.
 */
public class PresentTense extends BaseGrammar {
    public PresentTense(String name) {
        super(name);
    }

    public static PresentTense createDefault() {
        return new PresentTense("现在时");
    }
}

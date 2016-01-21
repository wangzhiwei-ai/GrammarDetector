package edu.pku.ss.nlp.grammar;

/**
 * Created by zhiwei on 16/1/19.
 */
public class ProgressiveTense extends BaseGrammar {
    public ProgressiveTense(String name) {
        super(name);
    }

    public static ProgressiveTense createDefault() {
        return new ProgressiveTense("进行时");
    }
}

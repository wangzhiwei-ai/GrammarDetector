package edu.pku.ss.nlp.grammar;

/**
 * Created by zhiwei on 16/1/21.
 */
public class CountGrammar extends BaseGrammar {
    private static final String COUNTABLE_GRAMMAR = "可数名词";
    private static final String UNCOUNTABLE_GRAMMAR = "不可数名词";


    public CountGrammar(String name) {
        super(name);
    }

    public static CountGrammar createCountableGrammar() {
        return new CountGrammar(COUNTABLE_GRAMMAR);
    }

    public static CountGrammar createUncountableGrammar() {
        return new CountGrammar(UNCOUNTABLE_GRAMMAR);
    }
}

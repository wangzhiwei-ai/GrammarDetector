package edu.pku.ss.nlp.grammar;

public class Gerund extends BaseGrammar {

	public Gerund(String name) {
		super(name);
	}

	public static Gerund createDefault() {
		return new Gerund("动名词");
	}
}

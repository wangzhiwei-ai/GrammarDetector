package edu.pku.ss.nlp.grammar;

public class PerfectTense extends BaseGrammar {

	public PerfectTense(String name) {
		super(name);
	}

	public static PerfectTense createDefault() {
		return new PerfectTense("完成时");
	}
}

package edu.pku.ss.nlp.grammar;

public class FutureTense extends BaseGrammar {

	public FutureTense(String name) {
		super(name);
	}
	
	
	public static FutureTense createDefault(){
		return new FutureTense("将来时");
	}
}

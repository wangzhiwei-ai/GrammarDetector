package edu.pku.ss.nlp.grammar;

public class POSGrammar extends BaseGrammar {

	public POSGrammar(String name) {
		super(name);
	}

	@Override
	public String getGrammarType() {
		String clsName = POSGrammar.class.getName();

		return clsName.substring(clsName.lastIndexOf(".") + 1);
	}

	
	
}

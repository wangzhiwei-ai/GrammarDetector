package edu.pku.ss.nlp.detect;

import java.util.Map;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.DependencyParseAnnotator;

public class TenseDetector implements Detectable {

	@Override
	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		DependencyParseAnnotator dpa = new DependencyParseAnnotator();
		Annotation ann = new Annotation();
		dpa.annotate(ann);
	}

}

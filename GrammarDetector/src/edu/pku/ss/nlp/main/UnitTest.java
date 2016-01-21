package edu.pku.ss.nlp.main;

import java.util.Map;
import java.util.Set;

import edu.pku.ss.nlp.detect.ClauseTypeDetector;
import edu.pku.ss.nlp.detect.Detectable;
import edu.pku.ss.nlp.detect.SentenceTypeDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.format.JsonFormatter;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.pipeline.DetectPipeline;
import edu.pku.ss.nlp.units.LanguageUnit;

public class UnitTest {

	public static void main(String[] args) {
		String sent = "Mr. Ling is just the boy whom I want to see.";
		LanguageUnit lu = new LanguageUnit(sent);

		DetectPipeline pipeline = new DetectPipeline();

		Detectable detetor = new SentenceTypeDetector();
		// Detectable detector1 = new POSDetector();
		// Detectable detector2 = new SentenceTypeDetector();
		// Detectable detector4 = new ModalVerbDetector();
		//Detectable detector5 = new ComparativeDetector();
		 Detectable detector6 = new ClauseTypeDetector();
		// Detectable detector7 = new SubjunctiveDetector();
		// Detectable detector8 = new GerundDetector();
		// Detectable detector9 = new InfinitiveDetector();
		// Detectable detector10 = new PerfectTenseDetector();

		// pipeline.addDetector(detector5);
		pipeline.addDetector(detector6);
		// pipeline.addDetector(detector7);

		Map<TokenPosition, Set<BaseGrammar>> grammarMap = pipeline.detect(lu);
		String jsonStr = JsonFormatter.formatToJson(grammarMap, lu);
		System.out.println(jsonStr);
	}

}

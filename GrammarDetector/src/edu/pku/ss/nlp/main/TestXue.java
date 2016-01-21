package edu.pku.ss.nlp.main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import edu.pku.ss.nlp.detect.ClauseTypeDetector;
import edu.pku.ss.nlp.detect.ComparativeDetector2;
import edu.pku.ss.nlp.detect.Detectable;
import edu.pku.ss.nlp.detect.LexiconDetector;
import edu.pku.ss.nlp.detect.MultipleDetectable;
import edu.pku.ss.nlp.detect.SubjunctiveDetector;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.format.JsonFormatter;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.pipeline.DetectPipeline;
import edu.pku.ss.nlp.units.LanguageUnit;

public class TestXue {

	public static void main(String[] args) throws IOException {
		// step1:初始化一个语法检测流水线.
		DetectPipeline pipeline = new DetectPipeline();

		// step2:初始化一些列语法检测器.
		Detectable detector5 = new ComparativeDetector2();
		Detectable detector6 = new ClauseTypeDetector();
		Detectable detector7 = new SubjunctiveDetector();

		//
		MultipleDetectable detector3 = new LexiconDetector();
		pipeline.addDetector(detector3);

		// step4:将语法检测器添加至流水线.
		pipeline.addDetector(detector5);
		pipeline.addDetector(detector6);
		pipeline.addDetector(detector7);

		// step5:执行语法检测,获取语法点map.
		List<String> sentList = FileUtils.readLines(new File(
				"res/test/sents.txt"));
		File outFile = new File("res/test/sents_xue2.result");
		int i = 0;
		for (String sent : sentList) {
			// System.out.println(sent);
			i++;
			LanguageUnit unit = new LanguageUnit(sent);
			Map<TokenPosition, Set<BaseGrammar>> grammarMap = pipeline
					.detect(unit);
			String jsonStr = JsonFormatter.formatToJson(grammarMap, unit);
			FileUtils.write(outFile, jsonStr + "\n", true);
			if (i % 50 == 0)
				System.out.println(i + " complete!");

		}

		// step6:以json格式打印检测结果.
		// System.out.println(JsonFormatter.formatToJson(grammarMap, unit));
	}

}

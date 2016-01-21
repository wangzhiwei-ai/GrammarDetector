package edu.pku.ss.nlp.performance.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import edu.pku.ss.nlp.detect.Detectable;
import edu.pku.ss.nlp.detect.LexiconDetector;
import edu.pku.ss.nlp.detect.MultipleDetectable;
import edu.pku.ss.nlp.units.LanguageUnit;

public class PerformanceTest {

	@SuppressWarnings("unused")
	public static void testSpeed(Detectable detector) throws IOException {
		List<String> sentList = FileUtils.readLines(new File(
				"res/test/sents.txt"));
		int size = sentList.size();
		long start = System.currentTimeMillis();
		int i = 0;
		for (String sent : sentList) {
			i++;
			detector.detectGrammar(new LanguageUnit(sent));
			if (i == 10000)
				break;
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start) * 1.0 / 1000);
		System.out.println(i * 1.0 / ((end - start) * 1.0 / 1000) + "s");
	}

	@SuppressWarnings("unused")
	public static void testSpeed(MultipleDetectable detector)
			throws IOException {
		List<String> sentList = FileUtils.readLines(new File(
				"res/test/sents.txt"));
		int size = sentList.size();
		int i = 0;
		long start = System.currentTimeMillis();
		for (String sent : sentList) {
			++i;
			detector.detectGrammar(new LanguageUnit(sent));
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start) * 1.0 / 1000);
		System.out.println(i * 1.0 / ((end - start) * 1.0 / 1000) + "s");
	}

	public static void main(String[] args) throws IOException {
		// testSpeed(new GerundDetector());
		// testSpeed(new POSDetector());
		// testSpeed(new ModalVerbDetector());
		// testSpeed(new PerfectTenseDetector());
		testSpeed(new LexiconDetector());
	}

}

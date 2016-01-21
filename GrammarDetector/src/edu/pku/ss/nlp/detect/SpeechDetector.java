package edu.pku.ss.nlp.detect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.DirectSpeech;
import edu.pku.ss.nlp.units.LanguageUnit;

public class SpeechDetector implements Detectable {
	private Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
	private final String grammarName = "间接引语";

	@SuppressWarnings("serial")
	private final static HashSet<String> speechVocabulary = new HashSet<String>() {
		{
			add("said");
			add("says");
			add("say");
			add("told");
			add("tell");
			add("tells");
			add("asked");
			add("asks");
			add("ask");
			add("explain");
			add("explains");
			add("explained");
		}
	};

	private ArrayList<String> specialRule = new ArrayList<String>();
	final private String specialRulePath = "res/speech/speech_sentence.txt";

	public ArrayList<String> LoadList(String readPath) {
		ArrayList<String> list = new ArrayList<String>();
		try {
			List<String> lines = FileUtils.readLines(new File(readPath));

			int size = lines.size();
			String line;
			for (int i = 0; i < size; ++i) {
				line = lines.get(i);
				list.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}

	public String RelabelSentence(LanguageUnit unit) {
		List<String> tokenList = unit.getTokenList();
		String result = "";

		for (int i = 0; i < tokenList.size(); i++) {
			String token = tokenList.get(i).toLowerCase();

			if (speechVocabulary.contains(token)) {
				token = "SPEECH_WORD";
			}

			result = result + token + " ";
		}

		// System.out.println(result);
		return result;
	}

	public void AddGrammarMap(String match, String sentence) {
		if (match.trim().equals(sentence.trim())) {
			this.grammarMap.put(new TokenPosition(-1), new DirectSpeech(grammarName));
			return;
		}

		int end = sentence.indexOf(match);
		if (end < 0) {
			return;
		}

		int startIndex = sentence.substring(0, end).split(" ").length;
		int endIndex = startIndex + match.split(" ").length;
		this.grammarMap.put(new TokenPosition(startIndex + 1, endIndex + 1), new DirectSpeech(this.grammarName));
	}

	@SuppressWarnings("unused")
	public String MatchingRule(String test, ArrayList<String> s) {
		Matcher matcher;
		String result = "";

		for (int i = 0; i < s.size(); i++) {
			Pattern pattern = Pattern.compile(s.get(i));
			matcher = pattern.matcher(test);
			if (matcher.find()) {
				String temp[] = test.split(s.get(i));
				result = matcher.group();
				AddGrammarMap(result, test);
			}
		}
		return result;
	}

	public void RecognizeSpecialSentence(LanguageUnit unit) {
		if (this.specialRule.isEmpty())
			this.specialRule = LoadList(this.specialRulePath);

		String sentence = RelabelSentence(unit);
		MatchingRule(sentence, this.specialRule);
	}

	@Override
	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		if (unit.getSentence().equals("") | unit == null)
			return null;
		grammarMap = new HashMap<TokenPosition, BaseGrammar>();

		RecognizeSpecialSentence(unit);
		return this.grammarMap;
	}

}

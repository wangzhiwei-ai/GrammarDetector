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
import edu.pku.ss.nlp.grammar.ClauseType;
import edu.pku.ss.nlp.grammar.Comparative;
import edu.pku.ss.nlp.units.LanguageUnit;

public class ComparativeDetector2 implements Detectable {
	private Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
	private final String grammarName = "比较句";

	@SuppressWarnings("serial")
	private final static HashSet<String> CompareVocabulary = new HashSet<String>() {
		{
			add("junior");
			add("senior");
			add("inferior");
			add("superior");
			add("much");
			add("more");
			add("less");
			add("most");
			add("least");
		}
	};

	private ArrayList<String> specialRule = new ArrayList<String>();
	final private String specialRulePath = "res/comparative/comparative_sentence.txt";

	public void AddGrammarMap(int startIndex, int endIndex, String grammarName) {
		this.grammarMap.put(new TokenPosition(startIndex, endIndex),
				new Comparative(grammarName));
	}

	public boolean RecognizeComparative(String word, String pos) {
		if (pos.equals("JJR") | pos.equals("RBR")
				| CompareVocabulary.contains(word))
			return true;
		else
			return false;
	}

	public boolean RecognizeAdvanced(String word, String pos) {
		if (pos.equals("JJS") | pos.equals("RBS")
				| CompareVocabulary.contains(word))
			return true;
		else
			return false;
	}

	public HashSet<String> LoadTable(String readPath) {
		HashSet<String> table = new HashSet<String>();
		try {
			List<String> lines = FileUtils.readLines(new File(readPath));

			int size = lines.size();
			String line;
			for (int i = 0; i < size; ++i) {
				line = lines.get(i);
				table.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return table;
	}

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
		List<String> posList = unit.getPOSList();
		String result = "";

		for (int i = 0; i < tokenList.size(); i++) {
			String token = tokenList.get(i).toLowerCase();
			String pos = posList.get(i);

			if (RecognizeComparative(token, pos)) {
				token = "COMPARATIVE";
			}

			if (RecognizeAdvanced(token, pos)) {
				token = "ADVANCED";
			}
			result = result + token + " ";
		}
		return result;
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

	public void AddGrammarMap(String match, String sentence) {
		if (match.trim().equals(sentence.trim())) {
			this.grammarMap.put(new TokenPosition(-1), new ClauseType(
					grammarName));
			return;
		}

		int end = sentence.indexOf(match);
		if (end < 0) {
			return;
		}

		int startIndex = sentence.substring(0, end).split(" ").length;
		int endIndex = startIndex + match.split(" ").length;
		this.grammarMap.put(new TokenPosition(startIndex, endIndex),
				new ClauseType(this.grammarName));
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

package edu.pku.ss.nlp.toolkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import edu.pku.ss.nlp.units.LanguageUnit;

public class ExtractionFeature {
	private HashSet<String> stopwords;
	private HashMap<String, Integer> table;

	private final String splitSymbol = "\t";
	private final String stopPath = "res/NLPToolkit/stopwords.txt";
	private final String highTfPath = "res/subjunctive/subjunctive_high_tf.txt";

	@SuppressWarnings("serial")
	private final HashSet<String> ModularityVocabulary = new HashSet<String>() {
		{
			add("can");
			add("may");
			add("need");
			add("could");
			add("would");
			add("have");
			add("had");
			add("should");
			add("might");
			add("shall");
		}
	};

	private static class ValueComparator implements
			Comparator<Map.Entry<String, Integer>> {
		public int compare(Map.Entry<String, Integer> m,
				Map.Entry<String, Integer> n) {
			return n.getValue() - m.getValue();
		}
	}

	public HashMap<String, Integer> countWordTF(String readPath) {
		HashMap<String, Integer> table = new HashMap<String, Integer>();
		if (this.stopwords == null)
			stopwords = getStopWord(this.stopPath);

		try {
			List<String> lines = FileUtils.readLines(new File(readPath));

			for (int i = 0; i < lines.size(); i++) {
				String[] words = lines.get(i).split(" ");

				for (int j = 0; j < words.length; j++) {
					String word = words[j].toLowerCase();
					if (this.stopwords.contains(word))
						continue;

					if (table.containsKey(word)) {
						int c = table.get(word);
						c++;
						table.put(word, c);
					} else {
						table.put(word, 1);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return table;
	}

	public HashSet<String> getStopWord(String stopWordPath) {
		HashSet<String> table = new HashSet<String>();

		try {
			List<String> lines = FileUtils.readLines(new File(stopWordPath));

			for (int i = 0; i < lines.size(); i++) {
				String word = lines.get(i);
				table.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return table;
	}

	public void getHighTFWords(String readPath, String writePath, int top) {
		HashMap<String, Integer> table = countWordTF(readPath);

		List<Map.Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>();
		List<String> result = new ArrayList<String>();
		ValueComparator vc = new ValueComparator();

		list.addAll(table.entrySet());
		Collections.sort(list, vc);

		int i = 0;
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it
				.hasNext() && i < top; i++) {
			result.add(it.next().getKey());
		}

		try {
			FileUtils.writeLines(new File(writePath), result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, Integer> getHighTFTable(String readPath) {
		HashMap<String, Integer> table = new HashMap<String, Integer>();

		try {
			List<String> lines = FileUtils.readLines(new File(readPath));

			for (int i = 0; i < lines.size(); i++) {
				table.put(lines.get(i).trim(), 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return table;
	}

	public String getHighTFFeature(LanguageUnit unit) {
		StringBuilder feature = new StringBuilder();
		List<String> tokenList = unit.getTokenList();

		if (this.table == null) {
			this.table = getHighTFTable(this.highTfPath);
		}

		for (String key : table.keySet()) {
			if (tokenList.contains(key)) {
				feature = feature.append("1" + this.splitSymbol);
			} else {
				feature = feature.append("0" + this.splitSymbol);
			}
		}

		return feature.toString();
	}

	public String getPOS(LanguageUnit unit) {
		StringBuilder feature = new StringBuilder();
		List<String> posList = unit.getPOSList();

		int CCNum = 0;
		int VNum = 0;
		for (int i = 0; i < posList.size(); i++) {
			String key = posList.get(i);
			if (key.equals("CC")) {
				CCNum++;
			} else if (key.equals("V")) {
				VNum++;
			}
		}

		feature.append(CCNum + this.splitSymbol);
		feature.append(VNum + this.splitSymbol);
		return feature.toString();
	}

	public String getModularity(LanguageUnit unit) {
		StringBuilder feature = new StringBuilder();
		List<String> tokenList = unit.getTokenList();

		int modularityNum = 0;

		for (String key : this.ModularityVocabulary) {
			if (tokenList.contains(key)) {
				feature = feature.append("1" + this.splitSymbol);
			} else {
				feature = feature.append("0" + this.splitSymbol);
			}
		}
		/*
		 * for(int i=0;i<tokenList.size();i++){ String key = tokenList.get(i);
		 * if(this.ModularityVocabulary.contains(key)){ modularityNum++; } }
		 */

		feature.append(modularityNum + this.splitSymbol);
		return feature.toString();
	}

	public String getLength(LanguageUnit unit) {
		StringBuilder feature = new StringBuilder();
		List<String> tokenList = unit.getTokenList();

		feature.append(tokenList.size() + this.splitSymbol);
		return feature.toString();
	}

	public void MakeSet(String readPath, String writePath, int label) {
		try {
			List<String> result = new ArrayList<String>();
			List<String> lines = FileUtils.readLines(new File(readPath));

			for (int i = 0; i < lines.size(); i++) {
				String feature = getAllFeature(lines.get(i));
				result.add(label + splitSymbol + feature);
			}

			FileUtils.writeLines(new File(writePath), result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String MakeInstance(String sentence) {
		String label = "0";
		String feature = getAllFeature(sentence);

		return label + splitSymbol + feature;
	}

	public String getAllFeature(String line) {
		StringBuilder result = new StringBuilder("");
		LanguageUnit unit = new LanguageUnit(line);

		result.append(getLength(unit));
		result.append(getModularity(unit));
		result.append(getPOS(unit));
		result.append(getHighTFFeature(unit));

		return result.toString();
	}
}

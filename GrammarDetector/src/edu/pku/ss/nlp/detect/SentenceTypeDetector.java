package edu.pku.ss.nlp.detect;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.SentenceType;
import edu.pku.ss.nlp.units.LanguageUnit;

/**
 * 句子类型检测器. 句子类型根据用途可以分为四类：陈述句,疑问句,感叹句,祈使句.
 * 
 * @author nulooper
 * @date 2015年4月11日
 * @time 下午10:36:35
 */
public class SentenceTypeDetector implements Detectable {

	private Set<String> firstLemmaSet;

	public SentenceTypeDetector() {
		this.firstLemmaSet = new HashSet<String>();
		firstLemmaSet.add("do");
		firstLemmaSet.add("let");
		firstLemmaSet.add("be");
		firstLemmaSet.add("please");
		firstLemmaSet.add("just");
		firstLemmaSet.add("no");
		firstLemmaSet.add("never");
		firstLemmaSet.add("come");
		firstLemmaSet.add("mind");
		firstLemmaSet.add("never");
	}

	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		List<String> tokenList = unit.getTokenList();
		List<String> lemmaList = unit.getLemmaList();

		if (tokenList.isEmpty()) {
			return Collections.unmodifiableMap(grammarMap);
		}

		int size = unit.size();
		String firstLemma = lemmaList.get(0);
		String lastLemma = lemmaList.get(size - 1);

		if (this.isExclamatory(firstLemma, lastLemma)) {
			grammarMap.put(new TokenPosition(-1), new SentenceType("感叹句"));
		} else if (this.isInterrogative(lastLemma)) {
			grammarMap.put(new TokenPosition(-1), new SentenceType("疑问句"));
		} else if (this.isImperative(lemmaList, unit.getPOSList(), firstLemma, lastLemma)) {
			grammarMap.put(new TokenPosition(-1), new SentenceType("祈使句"));
		} else {
			grammarMap.put(new TokenPosition(-1), new SentenceType("陈述句"));
		}

		return Collections.unmodifiableMap(grammarMap);
	}

	/**
	 * 检查是否是感叹句.
	 * 
	 * @param firstLemma
	 * @param lastLemma
	 * @return
	 */
	public boolean isExclamatory(String firstLemma, String lastLemma) {
		if ("what".equals(firstLemma) || "how".equals(firstLemma)) {
			return "!".equals(lastLemma);
		}

		return false;
	}

	/**
	 * 检查是否是疑问句.
	 * 
	 * @param lastLemma
	 * @return
	 */
	public boolean isInterrogative(String lastLemma) {
		return "?".equals(lastLemma);
	}

	/**
	 * 检查是否是祈使句.
	 * 
	 * @param lemmaList
	 * @return
	 */
	public boolean isImperative(List<String> lemmaList, List<String> posList, String firstLemma, String lastLemma) {
		int size = lemmaList.size();
		if (size <= 1)
			return false;
		String firstPos = posList.get(0);
		// 一个词或者连个词，标点为感叹号或者句点的都是祈使句.
		if (size <= 5 && ("!".equals(lastLemma) || ".".equals(lastLemma)))
			return true;

		// 如果第一个词原型是do，结尾标点为感叹号或者句点，则为祈使句.
		if (firstLemmaSet.contains(firstLemma) && ("!".equals(lastLemma) || ".".equals(lastLemma)))
			return true;

		// 如果第一个词是人称代词you, 且结尾标点为感叹号，则为祈使句.
		if ("you".equals(firstLemma) && ("!".equals(lastLemma)))
			return true;

		// 如果第一个词是动词原型, 切结尾标点为感叹号或者句点，则为祈使句.
		if ("VB".equals(firstPos) && ("!".equals(lastLemma) || ".".equals(lastLemma)))
			return true;

		return false;
	}

	public static void main(String[] args) {

		String sent = "Hurry up and you'll catch the train.";
		LanguageUnit lu = new LanguageUnit(sent);
		SentenceTypeDetector std = new SentenceTypeDetector();
		List<String> lemmaList = lu.getLemmaList();
		String firstLemma = lemmaList.get(0);
		String lastLemma = lemmaList.get(lemmaList.size() - 1);
		System.out.println(std.isImperative(lu.getLemmaList(), lu.getPOSList(), firstLemma, lastLemma));

	}
}

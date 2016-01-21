package edu.pku.ss.nlp.units;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import edu.pku.ss.nlp.toolkit.NLPToolkit;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * 语言单元类. 当前语言单元只代表一个真实的英语句子，如果使用多句文本进行实例化，那么初始化的结果只处理文本中的第一个句子.
 * 
 * @author nulooper
 * @date 2015年4月11日
 * @time 下午10:39:59
 */
public class LanguageUnit {
	private List<String> tokenList;
	private List<String> posList;
	private List<String> lemmaList;

	private String sentence;
	private int size;

	private Tree phraseTree = null;

	public LanguageUnit(String sentence) {
		this.init(sentence);
	}

	private void init(String sentence) {
		if (null == sentence)
			sentence = "";
		this.sentence = sentence;

		this.tokenList = new ArrayList<String>();
		this.posList = new ArrayList<String>();
		this.lemmaList = new ArrayList<String>();

		List<CoreMap> annotateMap = NLPToolkit.getInstance().annotate(sentence);
		if (null == annotateMap || annotateMap.isEmpty()) {
			return;
		}
		CoreMap sentAnnotation = annotateMap.get(0);
		for (CoreLabel token : sentAnnotation.get(TokensAnnotation.class)) {
			// this is the text of the token
			String word = token.get(TextAnnotation.class);
			this.tokenList.add(word);
			// this is the POS tag of the token
			String pos = token.get(PartOfSpeechAnnotation.class);
			this.posList.add(pos);
			// this is the NER label of the token
			String lemma = token.get(LemmaAnnotation.class);
			this.lemmaList.add(lemma);
		}

		this.size = this.tokenList.size();
	}

	public List<String> getTokenList() {
		return Collections.unmodifiableList(this.tokenList);
	}

	public List<String> getPOSList() {
		return Collections.unmodifiableList(this.posList);
	}

	public List<String> getLemmaList() {
		return Collections.unmodifiableList(this.lemmaList);
	}

	public Tree getPhraseTree() {
		if (this.phraseTree == null) {
			Properties props = new Properties();
			props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			Annotation annotation = new Annotation(this.sentence);
			pipeline.annotate(annotation);

			List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
			if (null == sentences || sentences.isEmpty()) {
				return null;
			}

			CoreMap sentAnnotation = sentences.get(0);
			this.phraseTree = sentAnnotation.get(TreeAnnotation.class);
		}

		return this.phraseTree;
	}

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	@Override
	public String toString() {
		return "LanguageUnit [tokenList=" + tokenList + ", posList=" + posList + ", lemmaList=" + lemmaList + "]";
	}

	public String getSentence() {
		return this.sentence;
	}

	public List<String> getLowerTokenList() {
		List<String> lowerTokenList = new ArrayList<String>();
		for (String token : this.tokenList) {
			lowerTokenList.add(token.toLowerCase());
		}

		return lowerTokenList;
	}

	public static void main(String[] args) {
		String sent = "Please watch out!";
		LanguageUnit lu = new LanguageUnit(sent);
		for(String pos : lu.getPOSList()){
			System.out.println(pos);
		}
	}
}

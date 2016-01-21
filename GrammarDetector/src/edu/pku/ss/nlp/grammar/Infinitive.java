package edu.pku.ss.nlp.grammar;

/**
 * 不定式语法.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:36:35
 */
public class Infinitive extends BaseGrammar {

	public Infinitive(String name) {
		super(name);
	}

	public static Infinitive createDefault() {
		return new Infinitive("不定式");
	}
}

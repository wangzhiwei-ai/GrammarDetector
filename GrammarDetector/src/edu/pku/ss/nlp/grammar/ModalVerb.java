package edu.pku.ss.nlp.grammar;

/**
 * 情态动词语法.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:36:35
 */
public class ModalVerb extends BaseGrammar {

	public ModalVerb(String name) {
		super(name);
	}

	public static ModalVerb createDefault() {
		return new ModalVerb("情态动词");
	}
}

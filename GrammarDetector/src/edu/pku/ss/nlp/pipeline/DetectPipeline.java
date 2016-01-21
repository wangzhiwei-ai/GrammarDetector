package edu.pku.ss.nlp.pipeline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.pku.ss.nlp.detect.Detectable;
import edu.pku.ss.nlp.detect.MultipleDetectable;
import edu.pku.ss.nlp.detect.TokenPosition;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;

/**
 * 语法检测流水线.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:44:54
 */
public class DetectPipeline {
	private List<Detectable> detectors;
	private List<MultipleDetectable> multipleDetectors;

	public DetectPipeline() {
		this.detectors = new ArrayList<Detectable>();
		this.multipleDetectors = new ArrayList<MultipleDetectable>();
	}

	public void addDetector(Detectable detector) {
		this.detectors.add(detector);
	}

	public void addDetector(MultipleDetectable multipleDetector) {
		this.multipleDetectors.add(multipleDetector);
	}

	public Map<TokenPosition, Set<BaseGrammar>> detect(LanguageUnit unit) {
		Map<TokenPosition, Set<BaseGrammar>> grammarMap = new HashMap<TokenPosition, Set<BaseGrammar>>();

		for (Detectable detector : this.detectors) {
			this.mergeSingleMapTo(detector.detectGrammar(unit), grammarMap);
		}

		for (MultipleDetectable md : this.multipleDetectors) {
			this.mergeMapTo(md.detectGrammar(unit), grammarMap);
		}

		return Collections.unmodifiableMap(grammarMap);
	}

	private void mergeSingleMapTo(Map<TokenPosition, BaseGrammar> srcMap,
			Map<TokenPosition, Set<BaseGrammar>> tarMap) {
		if (null == srcMap || tarMap == null)
			return;
		for (TokenPosition key : srcMap.keySet()) {
			if (tarMap.containsKey(key)) {
				tarMap.get(key).add(srcMap.get(key));
			} else {
				Set<BaseGrammar> gs = new HashSet<BaseGrammar>();
				gs.add(srcMap.get(key));
				tarMap.put(key, gs);
			}
		}
	}

	private void mergeMapTo(Map<TokenPosition, Set<BaseGrammar>> srcMap,
			Map<TokenPosition, Set<BaseGrammar>> tarMap) {
		if (null == srcMap || null == tarMap) {
			Logger.getGlobal().logp(Level.WARNING,
					DetectPipeline.class.getName(), "mergeMapTo",
					"输入srcMap或tarMap为null！");
			return;
		}
		if (srcMap.isEmpty()) {
			return;
		}

		for (TokenPosition key : srcMap.keySet()) {
			if (tarMap.containsKey(key)) {
				tarMap.get(key).addAll(srcMap.get(key));
			} else {
				tarMap.put(key, new HashSet<BaseGrammar>());
				tarMap.get(key).addAll(srcMap.get(key));
			}
		}

	}

	public static void main(String[] args) {

	}

}

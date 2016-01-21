package edu.pku.ss.nlp.detect;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import edu.pku.ss.nlp.format.JsonFormatter;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.POSGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;

/**
 * 情态动词检测器. 目前只检测：名词, 冠词, 代词, 形容词, 副词, 介词, 副词.
 * 
 * @author nulooper
 * @date 2015年4月11日
 * @time 下午10:36:35
 */
public class POSDetector implements Detectable {
	private Map<String, String> tagNameMap;

	private static final String NAME_TAGS_PATH = "res/pos/name_tags.tsv";

	public POSDetector() {
		this.tagNameMap = new HashMap<String, String>();
		try {
			List<String> lines = FileUtils.readLines(new File(NAME_TAGS_PATH));
			String[] items, tags;

			int size = lines.size();
			String line;
			for (int i = 0; i < size; ++i) {
				line = lines.get(i);
				items = line.split("\t");
				if (items.length != 2) {
					Logger.getGlobal().logp(Level.WARNING,
							this.getClass().getName(), "POSDetector",
							NAME_TAGS_PATH + " 文件第" + (i + 1) + "行格式不正确!");
					continue;
				}

				tags = items[1].split("\\|");
				for (String tag : tags) {
					this.tagNameMap.put(tag, items[0]);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
		List<String> posList = unit.getPOSList();

		int size = unit.size();
		String pos;
		for (int i = 0; i < size; ++i) {
			pos = posList.get(i);
			if (this.tagNameMap.containsKey(pos)) {
				grammarMap.put(new TokenPosition(i + 1), new POSGrammar(
						tagNameMap.get(pos)));
			}
		}

		return Collections.unmodifiableMap(grammarMap);
	}

	public static void main(String[] args) {
		LanguageUnit unit = new LanguageUnit(
				"hey , hey , hey , slow down now .");
		POSDetector p = new POSDetector();
		Map<TokenPosition, BaseGrammar> gm = p.detectGrammar(unit);
		Map<TokenPosition, Set<BaseGrammar>> map = new HashMap<TokenPosition, Set<BaseGrammar>>();
		for (TokenPosition tp : gm.keySet()) {
			Set<BaseGrammar> set = new HashSet<BaseGrammar>();
			set.add(gm.get(tp));
			map.put(tp, set);
		}
		System.out.println(JsonFormatter.formatToJson(map, unit));
	}
}

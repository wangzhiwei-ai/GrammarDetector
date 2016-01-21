package edu.pku.ss.nlp.detect;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import edu.pku.ss.nlp.format.JsonFormatter;
import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.Lexicon;
import edu.pku.ss.nlp.units.LanguageUnit;

/**
 * 词汇检测. 例如四级、六级、八级等专业词汇.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:34:19
 */
public class LexiconDetector implements MultipleDetectable {

	private Map<String, Set<BaseGrammar>> lexiconLevelsMap;
	private final static String LEXICON_PATH = "res/lexicon/lexicon.dict";

	public LexiconDetector() {
		this.lexiconLevelsMap = new HashMap<String, Set<BaseGrammar>>();
		try {
			List<String> lines = FileUtils.readLines(new File(LEXICON_PATH));
			String[] items;
			String token = null;
			for (String line : lines) {
				items = line.split("\\|\\|\\|");
				token = items[0];
				String[] levels = items[1].split("___");

				Set<BaseGrammar> lexicons = new HashSet<BaseGrammar>();
				for (String level : levels) {
					lexicons.add(new Lexicon(level));
				}

				this.lexiconLevelsMap.put(token, lexicons);

				// System.out.println(token + "|||"
				// + Utils.joinOn(Arrays.asList(levels), ","));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Map<TokenPosition, Set<BaseGrammar>> detectGrammar(LanguageUnit unit) {
		Map<TokenPosition, Set<BaseGrammar>> grammarMap = new HashMap<TokenPosition, Set<BaseGrammar>>();

		List<String> tokens = unit.getTokenList();
		int size = tokens.size();
		String token;
		for (int i = 0; i < size; ++i) {
			token = tokens.get(i).toLowerCase();
			if (this.lexiconLevelsMap.containsKey(token)) {
				grammarMap.put(new TokenPosition(i + 1),
						this.lexiconLevelsMap.get(token));
			}
		}

		return Collections.unmodifiableMap(grammarMap);
	}

	public static void main(String[] args) {
		LexiconDetector ld = new LexiconDetector();
		LanguageUnit unit = new LanguageUnit("well , the one thing we might have to slow down is a commitment we made");
		Map<TokenPosition, Set<BaseGrammar>> grammarMap = ld
				.detectGrammar(unit);
		System.out.println(JsonFormatter.formatToJson(grammarMap, unit));
	}

}

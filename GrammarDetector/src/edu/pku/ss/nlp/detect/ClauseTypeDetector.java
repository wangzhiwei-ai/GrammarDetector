package edu.pku.ss.nlp.detect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.ClauseType;
import edu.pku.ss.nlp.units.LanguageUnit;
import edu.pku.ss.nlp.utils.Constants;
import edu.pku.ss.nlp.utils.Utils;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;

public class ClauseTypeDetector implements Detectable {

	private enum TypeEnum {
		TREE, ORIGINAL
	}

	private Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();

	private static final String CLAUSE_OPTIONS_PATH = "res/clause/clause_options.tsv";
	private ArrayList<String> options = new ArrayList<String>();

	public ClauseTypeDetector() {
	}

	public void LoadOptions() {
		try {
			List<String> lines = FileUtils.readLines(new File(
					CLAUSE_OPTIONS_PATH));

			int size = lines.size();
			String line;
			for (int i = 0; i < size; ++i) {
				line = lines.get(i);
				this.options.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] LoadRule(String path) {
		try {
			List<String> lines = FileUtils.readLines(new File(path));
			String[] rule = new String[lines.size()];
			String line;
			int size = lines.size();
			for (int i = 0; i < size; ++i) {
				line = lines.get(i);
				rule[i] = line.trim();
			}

			return rule;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String NoTregex(String test, String s[]) {
		Matcher matcher;
		String result = "";

		for (int i = 0; i < s.length; i++) {
			Pattern pattern = Pattern.compile(s[i]);
			matcher = pattern.matcher(test);
			if (matcher.find()) {
				String temp[] = test.split(s[i]);
				result = result + matcher.group() + temp[1] + "\n";
			}
		}
		return result;
	}

	public String Tregex(Tree t, String s[]) {
		TregexMatcher m;
		String result = "";

		for (int i = 0; i < s.length; i++) {
			TregexPattern p = TregexPattern.compile(s[i]);
			m = p.matcher(t);

			while (m.find()) {
				String le = m.getMatch().toString();
				result = result + le + "\n";
			}
		}

		return result;
	}

	public Tree ReplaceSymbol(Tree test, String o) {
		HashMap<String, String> commandTable = new HashMap<String, String>();
		TregexPattern patterns;
		TsurgeonPattern surgery;

		final String[] operator = { "relabel target BE", "relabel target AS",
				"relabel target INTHAT", "relabel target SBARCause",
				"relabel target THAT" };

		final String[] command = {
				"VB|VBD|VBG|VBN|VBP|VBZ=target <been|am|is|was|are|were|'s|'re|be|'m|Are|Am|Is|Was|Were",
				"IN=target<(if|though)$-(RB<as)",
				"SBAR=target<(WHPP<#(IN<in$+(WHNP<<that)))",
				"SBAR=target<(IN<because)", "NP=target <#(DT<#That)" };

		final String operatorResult = "relabel target SBARResult";
		final String operatorLink = "relabel target LINK";

		for (int i = 0; i < operator.length; i++) {
			commandTable.put(operator[i], command[i]);
		}

		if (commandTable.containsKey(o)) {
			patterns = TregexPattern.compile(commandTable.get(o));
			surgery = Tsurgeon.parseOperation(o);
			return Tree.valueOf(Tsurgeon
					.processPattern(patterns, surgery, test).toString());
		} else if (o.equals(operatorResult)) {
			final String s[] = new String[] {
					"SBAR=target<(IN<that)<S,,(RB,so)",
					"SBAR=target<(IN<that)<S,,(JJ,so)",
					"SBAR=target<(IN<that)<S,,(/^NN/,,(JJ,,such))" };

			for (int i = 0; i < s.length; i++) {
				patterns = TregexPattern.compile(s[i]);
				surgery = Tsurgeon.parseOperation(o);
				test = Tsurgeon.processPattern(patterns, surgery, test);
			}
			return test;
		} else if (o.equals(operatorLink)) {
			String result = "";

			TregexPattern p = TregexPattern.compile("/VB.*/=target >VP$+SBAR");
			TregexMatcher m = p.matcher(test);

			while (m.find()) {
				String le = m.getMatch().toString();
				le = le.replaceAll("\\(VB[A-Z]", "");
				le = le.replaceAll("\\)", "");
				le = le.replaceAll(" ", "");

				result = GetLamma(le);
				if (result
						.matches("keep|rest|remain|stay|lie|stand|seem|appear|look|feel|smell|sound|taste|become|grow|turn|fall|get|go|come|run|prove")) {
					surgery = Tsurgeon.parseOperation(o);
					test = Tree.valueOf(Tsurgeon.processPattern(p, surgery,
							test).toString());
				}
			}
			return test;
		} else {
			return null;
		}
	}

	private String getMatchOfThanAsThe(String original, String[] s, Tree tree) {
		String result = "";
		String typeOneWord = "";
		String typeTwoWord = "";
		String typeThreeWord = "";

		for (String str : s) {
			String type = str.split("_")[1];

			if (type.equals("1")) {
				String modifier = str.split("_")[0];
				String patternOfModifier = modifier + ".(than.(NP!.VP))";
				TregexPattern patternOfMdf = TregexPattern
						.compile(patternOfModifier);
				TregexMatcher matcherOfMdf = patternOfMdf.matcher(tree);

				while (matcherOfMdf.find()) {
					typeOneWord = getString(matcherOfMdf.getMatch().getLeaves());
					result += typeOneWord.trim() + " than"
							+ original.split(typeOneWord + " than")[1] + "\n";
				}
			} else if (type.equals("2")) {
				String modifierOne = str.split("_")[0].split("#")[0];
				String modifierTwo = str.split("_")[0].split("#")[1];
				String patternOfModifier = modifierTwo + ",(the,,("
						+ modifierOne + ",The))";
				TregexPattern patternOfMdf = TregexPattern
						.compile(patternOfModifier);
				TregexMatcher matcherOfMdf = patternOfMdf.matcher(tree);

				while (matcherOfMdf.find()) {
					typeTwoWord = getString(matcherOfMdf.getMatch().getLeaves());
					result += "the" + typeTwoWord
							+ original.split("the" + typeTwoWord)[1] + "\n";
				}
			} else {
				String modifierOne = str.split("_")[0].split("#")[0];
				String modifierTwo = str.split("_")[0].split("#")[1];
				String patternOfModifier = modifierTwo + "," + modifierOne
						+ "..(as.NP)";
				TregexPattern patternOfMdf = TregexPattern
						.compile(patternOfModifier);
				TregexMatcher matcherOfMdf = patternOfMdf.matcher(tree);

				while (matcherOfMdf.find()) {
					typeThreeWord = getString(matcherOfMdf.getMatch()
							.getLeaves());
					result += modifierOne + typeThreeWord
							+ original.split(modifierOne + typeThreeWord)[1]
							+ "\n";
				}
			}

		}
		return result;
	}

	@SuppressWarnings("unused")
	public String GetLamma(String word) {
		LanguageUnit u = new LanguageUnit(word);
		List<String> l = u.getLemmaList();

		for (int i = 0; i < l.size(); i++) {
			return l.get(0);
		}

		return null;
	}

	private String getString(List<Tree> list) {
		String str = "";

		for (Tree tree : list) {
			str += " " + tree.value().toString();
		}

		return str;
	}

	public void AddGrammarMap(String result, LanguageUnit unit,
			String grammarName, TypeEnum type) {
		String[] clauses = result.split("\n");
		for (String clause : clauses) {

			String c;
			if (type == TypeEnum.TREE) {
				c = getString(Tree.valueOf(clause).getLeaves()).trim();
			} else {
				c = clause;
			}

			List<String> tokens = unit.getTokenList();
			String tokenStr = Utils.joinOn(tokens, " ");

			if (c.trim().equals(tokenStr.trim())) {
				this.grammarMap.put(new TokenPosition(-1), new ClauseType(
						grammarName));
				return;
			}

			int end = tokenStr.indexOf(c);
			if (end < 0) {
				continue;
			}
			int startIndex = tokenStr.substring(0, end).split(" ").length;

			int endIndex = startIndex + c.split(" ").length;
			this.grammarMap.put(new TokenPosition(startIndex+1, endIndex+1),
					new ClauseType(grammarName));
		}
	}

	public boolean SubjectClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "主语从句";

		String s[] = Constants.ClauseTypeConstants.S_SUBJECT_CLAUSE;

		String result = Tregex(unit.getPhraseTree(), s);

		if (!result.equals("")) {
			AddGrammarMap(result, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		}

		return false;
	}

	public boolean PredicativeClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "表语从句";
		final String OPERATION_1 = "relabel target BE";
		final String OPERATION_2 = "relabel target AS";

		String s[] = Constants.ClauseTypeConstants.S_PREDICATIVE_CLAUSE;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_2);

		String result = Tregex(newPhraseTree, s);

		if (!result.equals("")) {
			AddGrammarMap(result, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		}
		return false;
	}

	public boolean ObjectClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "宾语从句";

		final String OPERATION_1 = "relabel target BE";
		final String OPERATION_2 = "relabel target LINK";
		final String OPERATION_3 = "relabel target AS";
		final String OPERATION_4 = "relabel target SBARCause";
		final String OPERATION_5 = "relabel target SBARResult";

		String s[] = Constants.ClauseTypeConstants.S_OBJECT_CLAUSE;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_2);
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_3);
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_4);
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_5);

		String result = Tregex(newPhraseTree, s);
		if (!result.equals("")) {
			AddGrammarMap(result, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		}
		return false;
	}

	public boolean PrepositionalClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "介宾从句";

		String s1[] = Constants.ClauseTypeConstants.S_PREPOSITIONAL_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_PREPOSITIONAL_CLAUSE_2;

		String result1 = Tregex(unit.getPhraseTree(), s1);
		String result2 = NoTregex(unit.getSentence(), s2);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}

		return false;
	}

	public boolean AppositiveClause(LanguageUnit unit) {
		return false;
	}

	public boolean AdjectiveComplementClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "形容词性补语从句";
		final String OPERATION_1 = "relabel target BE";

		String s[] = Constants.ClauseTypeConstants.S_ADJECTIVECOMPLEMENT_CLAUSE;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);

		String result = Tregex(newPhraseTree, s);

		if (!result.equals("")) {
			AddGrammarMap(result, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		}

		return false;
	}

	public boolean AttributiveClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "定语从句";
		final String OPERATION_1 = "relabel target BE";
		final String OPERATION_2 = "relabel target INTHAT";
		final String OPERATION_3 = "relabel target THAT";

		String s1[] = Constants.ClauseTypeConstants.S_ATTRIBUTIVE_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_ATTRIBUTIVE_CLAUSE_2;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_2);
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_3);

		String result1 = Tregex(newPhraseTree, s1);
		String result2 = NoTregex(unit.getSentence(), s2);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	private String NoTregex(String original, String[] s, Tree tree,
			String wpattern, String npattern, String vpattern, boolean flag) {

		Matcher matcher;
		String result = "";

		String Words = "";
		String lastWord = "";
		String firstWord = "";

		String patternOfWords = "";
		TregexPattern patternWords = null;
		TregexMatcher matcherWords = null;

		TregexPattern patternNP = null;
		String patternOfNP = "";
		TregexMatcher matcherNP = null;
		String NPWords = "";

		TregexPattern patternVP = null;
		String patternOfVP = "";
		TregexMatcher matcherVP = null;
		String VPWords = "";

		TregexPattern patternJJOrRB = null;
		String patternOfJJOrRB = "";
		TregexMatcher matcherJJOrRB = null;
		String JJOrRBWord = "";

		String vpatternString = "";
		// 获取or not
		String vString = "";
		if (!vpattern.equals("")) {
			for (int j = vpattern.split("\\.").length - 1; j >= 0; j--) {
				vpatternString = ".(" + vpattern.split("\\.")[j]
						+ vpatternString + ")";
				vString = " " + vpattern.split("\\.")[j] + vString;
			}
			vpattern = "(VP" + vpatternString + ")";
		}

		for (int i = 0; i < s.length; i++) {

			// 编译Pattern
			Pattern pattern = Pattern.compile(s[i]);
			matcher = pattern.matcher(original);

			// 遍历匹配到的正则式
			if (matcher.find()) {

				// 获取how
				lastWord = s[i].split("\\*")[1].split("\\\\b")[1];
				// 获取No matter
				if (!s[i].split("\\.")[0].equals(""))
					firstWord = s[i].split("\\.")[0].split("\\\\b")[1];
				else
					firstWord = "";

				// 获取,(matter,(No))
				String partOfPatternOfWord = "";
				if (!firstWord.equals("")) {
					for (int j = 0; j < firstWord.split(" ").length; j++) {
						partOfPatternOfWord = ",(" + firstWord.split(" ")[j]
								+ partOfPatternOfWord + ")";
					}
					if (flag) {
						partOfPatternOfWord = "," + partOfPatternOfWord;
					}
				}

				// 获取前面的how,(matter,(No))
				patternOfWords = getCompletePat("one", lastWord
						+ partOfPatternOfWord, wpattern, npattern, vpattern);
				System.out.println(patternOfWords);
				patternWords = TregexPattern.compile(patternOfWords);
				matcherWords = patternWords.matcher(tree);

				while (matcherWords.find()) {

					// 即为No matter how
					if (!firstWord.equals(""))
						Words = firstWord + " " + lastWord;
					else
						Words = lastWord;
					// System.out.println(Words);
					// 得到NP的Pattern
					patternOfNP = getCompletePat("two", lastWord
							+ partOfPatternOfWord, wpattern, npattern, vpattern);
					// System.out.println(patternOfNP);
					patternNP = TregexPattern.compile(patternOfNP);
					matcherNP = patternNP.matcher(tree);

					while (matcherNP.find()) {
						// 标签NP下面的单词
						NPWords = getString(matcherNP.getMatch().getLeaves());
						patternOfVP = getCompletePat("three", lastWord
								+ partOfPatternOfWord, wpattern, npattern,
								vpattern);
						// System.out.println(patternOfVP);
						patternVP = TregexPattern.compile(patternOfVP);
						matcherVP = patternVP.matcher(tree);

						while (matcherVP.find()) {
							// 标签VP下面的单词
							VPWords = getString(matcherVP.getMatch()
									.getLeaves());

							// 如果wpattern不为""，则匹配JJ或者RB
							if (!wpattern.equals("")) {

								// 一系列Tregex对象
								patternOfJJOrRB = getCompletePat("four",
										lastWord + partOfPatternOfWord,
										wpattern, npattern, vpattern);
								patternJJOrRB = TregexPattern
										.compile(patternOfJJOrRB);
								matcherJJOrRB = patternJJOrRB.matcher(tree);

								while (matcherJJOrRB.find()) {

									// 词性为JJ或者RB的单词
									JJOrRBWord = getString(matcherJJOrRB
											.getMatch().getLeaves());
									result += Words + JJOrRBWord + NPWords
											+ VPWords + vString + "\n";

									break;
								}
							} else
								result += Words + wpattern + NPWords + VPWords
										+ vString + "\n";
							break;
						}
						break;
					}
					break;
				}
			}
		}

		if (!result.equals(""))
			System.out.print(result);

		return result;
	}

	private String getCompletePat(String num, String wordsPat, String wpattern,
			String npattern, String vpattern) {
		String pattern = "";

		if (num.equals("one")) {
			if (!wpattern.equals("") && !npattern.equals("")) {
				pattern = wordsPat + ".(" + wpattern + ".(NP." + npattern
						+ ".VP))";
			} else if (!wpattern.equals("") && npattern.equals("")) {
				pattern = wordsPat + ".(" + wpattern + ".(NP.VP))";
			} else if (wpattern.equals("") && !npattern.equals("")) {
				pattern = wordsPat + ".(NP." + npattern + ".VP)";
			} else {
				pattern = wordsPat + ".(NP.VP)";
			}
		} else if (num.equals("two")) {
			if (!wpattern.equals("") && !npattern.equals("")) {
				pattern = "(NP." + npattern + ".VP),(" + wpattern + ",("
						+ wordsPat + "))";
			} else if (!wpattern.equals("") && npattern.equals("")) {
				pattern = "(NP.VP),(" + wpattern + ",(" + wordsPat + "))";
			} else if (wpattern.equals("") && !npattern.equals("")) {
				pattern = "(NP." + npattern + ".VP),(" + wordsPat + ")";
			} else {
				pattern = "(NP.VP),(" + wordsPat + ")";
			}
		} else if (num.equals("three")) {
			if (!wpattern.equals("") && !npattern.equals("")) {
				pattern = "VP,(NP,(" + wpattern + ",(" + wordsPat + ")))";
			} else if (!wpattern.equals("") && npattern.equals("")) {
				pattern = "VP,(NP,(" + wpattern + ",(" + wordsPat + ")))";
			} else if (wpattern.equals("") && !npattern.equals("")) {
				pattern = "VP,(NP,(" + wordsPat + "))";
			} else {
				pattern = "VP,(NP,(" + wordsPat + "))";
			}
		} else {
			if (!npattern.equals("")) {
				pattern = wpattern + ",(" + wordsPat + ").(NP." + npattern
						+ ".VP)";
			} else {
				pattern = wpattern + ",(" + wordsPat + ").(NP.VP)";
			}

		}

		if (!vpattern.equals("")) {
			pattern = pattern.replace("VP", vpattern);
		}

		if (npattern.startsWith("!")) {
			pattern = pattern.replace(".!", "!.");
		}

		return pattern;
	}

	private String NoTregexIfBeforeS(String original, String[] s, Tree tree) {

		Matcher matcher;
		String result = "";

		// 分别为Even if, if, Even
		String Words = "";
		String lastWord = "";
		String firstWord = "";

		// 匹配Words涉及的一些对象
		String patternOfWords = "";
		TregexPattern patternWords = null;
		TregexMatcher matcherWords = null;

		// 匹配S涉及到的一些对象
		TregexPattern patternS = null;
		String patternOfS = "";
		TregexMatcher matcherS = null;
		String SWords = "";

		for (int i = 0; i < s.length; i++) {

			Pattern pattern = Pattern.compile(s[i]);
			matcher = pattern.matcher(original);

			if (matcher.find()) {

				lastWord = s[i].split("\\*")[1].split("\\\\b")[1];
				if (!s[i].split("\\.")[0].equals(""))
					firstWord = s[i].split("\\.")[0].split("\\\\b")[1];
				else
					firstWord = "";
				// ,(Even)
				String partOfPatternOfWord = "";
				if (!firstWord.equals("")) {
					for (int j = 0; j < firstWord.split(" ").length; j++) {
						partOfPatternOfWord = ",(" + firstWord.split(" ")[j]
								+ partOfPatternOfWord + ")";
					}
				}
				patternOfWords = lastWord + partOfPatternOfWord + ".(S)";
				patternWords = TregexPattern.compile(patternOfWords);
				matcherWords = patternWords.matcher(tree);

				while (matcherWords.find()) {
					// Words为Even if
					if (!firstWord.equals(""))
						Words = firstWord + " " + lastWord;
					else
						Words = lastWord;

					patternOfS = "S" + ",(" + lastWord + partOfPatternOfWord
							+ ")";
					patternS = TregexPattern.compile(patternOfS);
					matcherS = patternS.matcher(tree);

					while (matcherS.find()) {
						// 得到标签S下面的单词
						SWords = getString(matcherS.getMatch().getLeaves());
						result += Words + SWords + "\n";
						break;
					}
					break;
				}
			}
		}

		if (!result.equals(""))
			System.out.print(result);
		return result;
	}

	public boolean PlaceClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "地点状语从句";
		final String OPERATION_1 = "relabel target BE";

		String s1[] = Constants.ClauseTypeConstants.S_PLACECLAUSE_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_PLACECLAUSE_CLAUSE_2;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);

		String result1 = Tregex(newPhraseTree, s1);
		String result2 = NoTregex(unit.getSentence(), s2, newPhraseTree, "",
				"", "", false);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public boolean CauseClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "原因状语从句";
		final String OPERATION_1 = "relabel target BE";

		String s1[] = Constants.ClauseTypeConstants.S_CAUSE_RULE_1;
		String s2[] = Constants.ClauseTypeConstants.S_CAUSE_RULE_2;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);

		String result1 = Tregex(newPhraseTree, s1);
		String result2 = NoTregex(unit.getSentence(), s2, newPhraseTree, "",
				"", "", false);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public boolean PurposeClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "目的状语从句";

		String s1[] = Constants.ClauseTypeConstants.S_PURPOSE_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_PURPOSE_CLAUSE_2;
		String s3[] = Constants.ClauseTypeConstants.S_PURPOSE_CLAUSE_3;

		String result1 = Tregex(unit.getPhraseTree(), s1);
		String result2 = NoTregex(unit.getSentence(), s2, unit.getPhraseTree(),
				"", "", "", false);
		String result3 = NoTregex(unit.getSentence(), s3, unit.getPhraseTree(),
				"", "MD", "", false);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result3.equals("")) {
			AddGrammarMap(result3, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	public boolean ResultClause(LanguageUnit unit) {
		final String RESULT_CLAUSE_RULE_1 = "res/clause_rule/ResultClause_1.tsv";
		final String RESULT_CLAUSE_RULE_2 = "res/clause_rule/ResultClause_2.tsv";
		final String RESULT_CLAUSE_RULE_3 = "res/clause_rule/ResultClause_3.tsv";
		final String CLUSE_TYPE = "结果状语从句";

		/*
		 * String s1[] = LoadRule(RESULT_CLAUSE_RULE_1); String s2[] =
		 * LoadRule(RESULT_CLAUSE_RULE_2); String s3[] =
		 * LoadRule(RESULT_CLAUSE_RULE_3);
		 */

		String s1[] = Constants.ClauseTypeConstants.S_RESULT_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_RESULT_CLAUSE_2;
		String s3[] = Constants.ClauseTypeConstants.S_RESULT_CLAUSE_3;

		String result1 = Tregex(unit.getPhraseTree(), s1);
		String result2 = NoTregex(unit.getSentence(), s2, unit.getPhraseTree(),
				"", "", "", false);
		String result3 = NoTregex(unit.getSentence(), s3, unit.getPhraseTree(),
				"", "!MD", "", false);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result3.equals("")) {
			AddGrammarMap(result3, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public boolean ConditionClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "条件状语从句";

		String s1[] = Constants.ClauseTypeConstants.S_CONDITION_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_CONDITION_CLAUSE_2;
		String s3[] = Constants.ClauseTypeConstants.S_CONDITION_CLAUSE_3;

		String result1 = Tregex(unit.getPhraseTree(), s1);
		String result2 = NoTregex(unit.getSentence(), s2, unit.getPhraseTree(),
				"", "", "", false);
		String result3 = NoTregex(unit.getSentence(), s3);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result3.equals("")) {
			AddGrammarMap(result3, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public boolean ConcessClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "让步状语从句";

		String s1[] = Constants.ClauseTypeConstants.S_CONCESS_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_CONCESS_CLAUSE_2;
		String s3[] = Constants.ClauseTypeConstants.S_CONCESS_CLAUSE_3;
		String s4[] = Constants.ClauseTypeConstants.S_CONCESS_CLAUSE_4;

		String result1 = Tregex(unit.getPhraseTree(), s1);
		String result2 = NoTregexIfBeforeS(unit.getSentence(), s2,
				unit.getPhraseTree());
		String result3 = NoTregex(unit.getSentence(), s3, unit.getPhraseTree(),
				"", "", "or.not", false);
		String result4 = NoTregex(unit.getSentence(), s4, unit.getPhraseTree(),
				"JJ", "", "", false);
		String result5 = NoTregex(unit.getSentence(), s4, unit.getPhraseTree(),
				"RB", "", "", false);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result3.equals("")) {
			AddGrammarMap(result3, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result4.equals("")) {
			AddGrammarMap(result4, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result5.equals("")) {
			AddGrammarMap(result5, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public boolean ComparisonClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "比较状语从句";

		String s1[] = Constants.ClauseTypeConstants.S_COMPARE_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_COMPARE_CLAUSE_2;

		String result1 = getMatchOfThanAsThe(unit.getSentence(), s1,
				unit.getPhraseTree());
		String result2 = NoTregex(unit.getSentence(), s2, unit.getPhraseTree(),
				"", "", "", false);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public boolean MannerClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "方式状语从句";
		final String OPERATION_1 = "relabel target BE";

		String s1[] = Constants.ClauseTypeConstants.S_MANNER_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_MANNER_CLAUSE_2;
		String s3[] = Constants.ClauseTypeConstants.S_MANNER_CLAUSE_3;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);

		String result1 = Tregex(newPhraseTree, s1);
		String result2 = NoTregexIfBeforeS(unit.getSentence(), s2,
				newPhraseTree);
		String result3 = NoTregex(unit.getSentence(), s3, newPhraseTree, "",
				"", "", false);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result3.equals("")) {
			AddGrammarMap(result3, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public boolean TimeClause(LanguageUnit unit) {
		final String CLUSE_TYPE = "时间状语从句";
		final String OPERATION_1 = "relabel target BE";

		String s1[] = Constants.ClauseTypeConstants.S_TIME_CLAUSE_1;
		String s2[] = Constants.ClauseTypeConstants.S_TIME_CLAUSE_2;
		String s3[] = Constants.ClauseTypeConstants.S_TIME_CLAUSE_3;
		String s4[] = Constants.ClauseTypeConstants.S_TIME_CLAUSE_4;

		Tree newPhraseTree = unit.getPhraseTree();
		newPhraseTree = ReplaceSymbol(newPhraseTree, OPERATION_1);

		String result1 = Tregex(newPhraseTree, s1);
		String result2 = NoTregexIfBeforeS(unit.getSentence(), s2,
				newPhraseTree);
		String result3 = NoTregex(unit.getSentence(), s3, newPhraseTree, "",
				"", "", false);
		String result4 = NoTregex(unit.getSentence(), s4, newPhraseTree, "",
				"", "", true);

		if (!result1.equals("")) {
			AddGrammarMap(result1, unit, CLUSE_TYPE, TypeEnum.TREE);
			return true;
		} else if (!result2.equals("")) {
			AddGrammarMap(result2, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result3.equals("")) {
			AddGrammarMap(result3, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		} else if (!result4.equals("")) {
			AddGrammarMap(result4, unit, CLUSE_TYPE, TypeEnum.ORIGINAL);
			return true;
		}
		return false;
	}

	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		if (unit.getSentence().equals("") | unit == null
				| unit.getPhraseTree() == null)
			return null;

		grammarMap = new HashMap<TokenPosition, BaseGrammar>();

		this.SubjectClause(unit);
		this.PredicativeClause(unit);
		this.ObjectClause(unit);
		this.PrepositionalClause(unit);
		this.AdjectiveComplementClause(unit);
		this.AttributiveClause(unit);
		this.PlaceClause(unit);
		this.CauseClause(unit);
		this.PurposeClause(unit);
		this.ResultClause(unit);
		this.ConditionClause(unit);
		this.ConcessClause(unit);
		this.ComparisonClause(unit);
		this.MannerClause(unit);
		this.TimeClause(unit);

		return this.grammarMap;
	}

	public Map<TokenPosition, BaseGrammar> detectGrammarDependOnOptions(
			LanguageUnit unit) {
		for (int i = 0; i < options.size(); i++) {
			switch (options.get(i)) {
			case "SubjectClause":
				this.SubjectClause(unit);
				break;
			case "PredicativeClause":
				this.PredicativeClause(unit);
				break;
			case "ObjectClause":
				this.ObjectClause(unit);
				break;
			case "PrepositionalClause":
				this.PrepositionalClause(unit);
				break;
			case "AdjectiveComplementClause":
				this.AdjectiveComplementClause(unit);
				break;
			case "AttributiveClause":
				this.AttributiveClause(unit);
				break;
			case "PlaceClause":
				this.PlaceClause(unit);
				break;
			case "CauseClause":
				this.CauseClause(unit);
				break;
			case "PurposeClause":
				this.PurposeClause(unit);
				break;
			case "ResultClause":
				this.ResultClause(unit);
				break;
			case "ConditionClause":
				this.ConditionClause(unit);
				break;
			case "ConcessClause":
				this.ConcessClause(unit);
				break;
			case "ComparisonClause":
				this.ComparisonClause(unit);
				break;
			case "MannerClause":
				this.MannerClause(unit);
				break;
			case "TimeClause":
				this.TimeClause(unit);
				break;
			}
		}
		return this.grammarMap;
	}
}

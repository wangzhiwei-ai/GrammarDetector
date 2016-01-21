package edu.pku.ss.nlp.detect;

import java.util.Map;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.units.LanguageUnit;

/**
 * 语法检测接口.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:24:47
 */
public interface Detectable {

	/**
	 * 语法检测器公共接口. 检测语法返回位置对应的语法.key为语言单元的位置列表, value为对应的语法对象.
	 * 
	 * 注: 当key中的位置列表只有一个元素且位置为-1时，表示value语法对象描述的是整个语言单元; 当key的位置列表为空时，表示没有检测到语法;
	 * 
	 * @param unit
	 *            语言单元. 已经被分词, 词性标注等预处理.
	 * @return 标注结果.
	 */
	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit);

}

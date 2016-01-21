package edu.pku.ss.nlp.detect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.pku.ss.nlp.grammar.BaseGrammar;
import edu.pku.ss.nlp.grammar.Voice;
import edu.pku.ss.nlp.units.LanguageUnit;

public class VoiceDetector implements Detectable{
	private Map<TokenPosition, BaseGrammar> grammarMap = new HashMap<TokenPosition, BaseGrammar>();
	private final String positiveGrammarName = "主动语态";
	private final String passiveGrammarName = "被动语态";

	public void AddGrammarMap(String grammarName) {
		this.grammarMap.put(new TokenPosition(-1),
				new Voice(grammarName));
	}
	
	@SuppressWarnings("serial")
	private final static HashSet<String> BeVocabulary = new HashSet<String>() {
		{
			add("be");
			add("is");
			add("are");
			add("am");
			add("was");
			add("were");
			add("being");
			add("been");
		}
	};
	
	public void RecognizeVoice(LanguageUnit unit) {
		List<String> posList = unit.getPOSList();
		List<String> tokenList = unit.getTokenList();
		
		boolean flag = false;
		for(int i=0;i<posList.size();i++){
			String now_word = tokenList.get(i);
			
			if((i!=posList.size()-1) && BeVocabulary.contains(now_word)){
				String next_pos = posList.get(i+1);
				if(next_pos.equals("VBN")){
					AddGrammarMap(passiveGrammarName);
					flag = true;
				}
			}
		}
		
		if(flag == false){
			AddGrammarMap(positiveGrammarName);
		}
	}	
	
	@Override
	public Map<TokenPosition, BaseGrammar> detectGrammar(LanguageUnit unit) {
		if (unit.getSentence().equals("") | unit == null)
			return null;
		grammarMap = new HashMap<TokenPosition, BaseGrammar>();

		RecognizeVoice(unit);
		return this.grammarMap;
	}

}

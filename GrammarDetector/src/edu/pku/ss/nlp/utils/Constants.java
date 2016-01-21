package edu.pku.ss.nlp.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Constants {
    public static final String BASE_DIR = "res/";

    public static class ClauseTypeConstants {
        public static final String SUB_DIR = BASE_DIR + "clause_rule/";

        public static final String SUBJECT_CLAUSE_RULE = SUB_DIR + "SubjectClause.tsv";

        public static final String PREDICATIVE_CLAUSE_RULE = SUB_DIR + "PredicativeClause.tsv";

        public static final String OBJECT_CLAUSE_RULE = SUB_DIR + "ObjectClause.tsv";

        public static final String PREPOSITIONAL_CLAUSE_RULE_1 = SUB_DIR + "PrepositionalClause_1.tsv";
        public static final String PREPOSITIONAL_CLAUSE_RULE_2 = SUB_DIR + "PrepositionalClause_2.tsv";

        public static final String ADJECTIVECOMPLEMENT_CLAUSE_RULE = SUB_DIR + "AdjectiveComplementClause.tsv";

        public static final String ATTRIBUTIVE_CLAUSE_RULE_1 = SUB_DIR + "AttributiveClause_1.tsv";
        public static final String ATTRIBUTIVE_CLAUSE_RULE_2 = SUB_DIR + "AttributiveClause_2.tsv";

        public static final String PLACECLAUSE_CLAUSE_RULE_1 = SUB_DIR + "PlaceClause_1.tsv";
        public static final String PLACECLAUSE_CLAUSE_RULE_2 = SUB_DIR + "PlaceClause_2.tsv";


        public static final String CAUSE_RULE_1 = SUB_DIR + "CauseClause_1.tsv";
        public static final String CAUSE_RULE_2 = SUB_DIR + "CauseClause_2.tsv";

        public static final String PURPOSE_CLAUSE_RULE_1 = SUB_DIR + "PurposeClause_1.tsv";
        public static final String PURPOSE_CLAUSE_RULE_2 = SUB_DIR + "PurposeClause_2.tsv";
        public static final String PURPOSE_CLAUSE_RULE_3 = SUB_DIR + "PurposeClause_3.tsv";

        public static final String RESULT_CLAUSE_RULE_1 = SUB_DIR + "ResultClause_1.tsv";
        public static final String RESULT_CLAUSE_RULE_2 = SUB_DIR + "ResultClause_2.tsv";
        public static final String RESULT_CLAUSE_RULE_3 = SUB_DIR + "ResultClause_3.tsv";

        public static final String CONDITION_CLAUSE_RULE_1 = SUB_DIR + "ConditionClause_1.tsv";
        public static final String CONDITION_CLAUSE_RULE_2 = SUB_DIR + "ConditionClause_2.tsv";
        public static final String CONDITION_CLAUSE_RULE_3 = SUB_DIR + "ConditionClause_3.tsv";

        public static final String CONCESS_CLAUSE_RULE_1 = SUB_DIR + "ConcessClause_1.tsv";
        public static final String CONCESS_CLAUSE_RULE_2 = SUB_DIR + "ConcessClause_2.tsv";
        public static final String CONCESS_CLAUSE_RULE_3 = SUB_DIR + "ConcessClause_3.tsv";
        public static final String CONCESS_CLAUSE_RULE_4 = SUB_DIR + "ConcessClause_4.tsv";

        public static final String COMPARE_CLAUSE_RULE_1 = SUB_DIR + "ComparisonClause_1.tsv";
        public static final String COMPARE_CLAUSE_RULE_2 = SUB_DIR + "ComparisonClause_2.tsv";

        public static final String MANNER_CLAUSE_RULE_1 = SUB_DIR + "MannerClause_1.tsv";
        public static final String MANNER_CLAUSE_RULE_2 = SUB_DIR + "MannerClause_2.tsv";
        public static final String MANNER_CLAUSE_RULE_3 = SUB_DIR + "MannerClause_3.tsv";

        public static final String TIME_CLAUSE_RULE_1 = SUB_DIR + "TimeClause_1.tsv";
        public static final String TIME_CLAUSE_RULE_2 = SUB_DIR + "TimeClause_2.tsv";
        public static final String TIME_CLAUSE_RULE_3 = SUB_DIR + "TimeClause_3.tsv";
        public static final String TIME_CLAUSE_RULE_4 = SUB_DIR + "TimeClause_4.tsv";

        public static String[] S_SUBJECT_CLAUSE = null;
        public static String[] S_PREDICATIVE_CLAUSE = null;
        public static String[] S_OBJECT_CLAUSE = null;

        public static String[] S_PREPOSITIONAL_CLAUSE_1 = null;
        public static String[] S_PREPOSITIONAL_CLAUSE_2 = null;

        public static String[] S_ADJECTIVECOMPLEMENT_CLAUSE = null;

        public static String[] S_ATTRIBUTIVE_CLAUSE_1 = null;
        public static String[] S_ATTRIBUTIVE_CLAUSE_2 = null;

        public static String[] S_PLACECLAUSE_CLAUSE_1 = null;
        public static String[] S_PLACECLAUSE_CLAUSE_2 = null;

        public static String[] S_CAUSE_RULE_1 = null;
        public static String[] S_CAUSE_RULE_2 = null;

        public static String[] S_PURPOSE_CLAUSE_1 = null;
        public static String[] S_PURPOSE_CLAUSE_2 = null;
        public static String[] S_PURPOSE_CLAUSE_3 = null;

        public static String[] S_RESULT_CLAUSE_1 = null;
        public static String[] S_RESULT_CLAUSE_2 = null;
        public static String[] S_RESULT_CLAUSE_3 = null;

        public static String[] S_CONDITION_CLAUSE_1 = null;
        public static String[] S_CONDITION_CLAUSE_2 = null;
        public static String[] S_CONDITION_CLAUSE_3 = null;

        public static String[] S_CONCESS_CLAUSE_1 = null;
        public static String[] S_CONCESS_CLAUSE_2 = null;
        public static String[] S_CONCESS_CLAUSE_3 = null;
        public static String[] S_CONCESS_CLAUSE_4 = null;

        public static String[] S_COMPARE_CLAUSE_1 = null;
        public static String[] S_COMPARE_CLAUSE_2 = null;

        public static String[] S_MANNER_CLAUSE_1 = null;
        public static String[] S_MANNER_CLAUSE_2 = null;
        public static String[] S_MANNER_CLAUSE_3 = null;

        public static String[] S_TIME_CLAUSE_1 = null;
        public static String[] S_TIME_CLAUSE_2 = null;
        public static String[] S_TIME_CLAUSE_3 = null;
        public static String[] S_TIME_CLAUSE_4 = null;


        //public ClauseTypeConstants(){
        static {
            S_SUBJECT_CLAUSE = LoadRule(SUBJECT_CLAUSE_RULE);

            S_PREDICATIVE_CLAUSE = LoadRule(PREDICATIVE_CLAUSE_RULE);

            S_OBJECT_CLAUSE = LoadRule(OBJECT_CLAUSE_RULE);

            S_PREPOSITIONAL_CLAUSE_1 = LoadRule(PREPOSITIONAL_CLAUSE_RULE_1);
            S_PREPOSITIONAL_CLAUSE_2 = LoadRule(PREPOSITIONAL_CLAUSE_RULE_2);

            S_ADJECTIVECOMPLEMENT_CLAUSE = LoadRule(ADJECTIVECOMPLEMENT_CLAUSE_RULE);

            S_ATTRIBUTIVE_CLAUSE_1 = LoadRule(ATTRIBUTIVE_CLAUSE_RULE_1);
            S_ATTRIBUTIVE_CLAUSE_2 = LoadRule(ATTRIBUTIVE_CLAUSE_RULE_2);

            S_PLACECLAUSE_CLAUSE_1 = LoadRule(PLACECLAUSE_CLAUSE_RULE_1);
            S_PLACECLAUSE_CLAUSE_2 = LoadRule(PLACECLAUSE_CLAUSE_RULE_2);

            S_CAUSE_RULE_1 = LoadRule(CAUSE_RULE_1);
            S_CAUSE_RULE_2 = LoadRule(CAUSE_RULE_2);

            S_PURPOSE_CLAUSE_1 = LoadRule(PURPOSE_CLAUSE_RULE_1);
            S_PURPOSE_CLAUSE_2 = LoadRule(PURPOSE_CLAUSE_RULE_2);
            S_PURPOSE_CLAUSE_3 = LoadRule(PURPOSE_CLAUSE_RULE_3);

            S_RESULT_CLAUSE_1 = LoadRule(RESULT_CLAUSE_RULE_1);
            S_RESULT_CLAUSE_2 = LoadRule(RESULT_CLAUSE_RULE_2);
            S_RESULT_CLAUSE_3 = LoadRule(RESULT_CLAUSE_RULE_3);

            S_CONDITION_CLAUSE_1 = LoadRule(CONDITION_CLAUSE_RULE_1);
            S_CONDITION_CLAUSE_2 = LoadRule(CONDITION_CLAUSE_RULE_2);
            S_CONDITION_CLAUSE_3 = LoadRule(CONDITION_CLAUSE_RULE_3);

            S_CONCESS_CLAUSE_1 = LoadRule(CONCESS_CLAUSE_RULE_1);
            S_CONCESS_CLAUSE_2 = LoadRule(CONCESS_CLAUSE_RULE_2);
            S_CONCESS_CLAUSE_3 = LoadRule(CONCESS_CLAUSE_RULE_3);
            S_CONCESS_CLAUSE_4 = LoadRule(CONCESS_CLAUSE_RULE_4);

            S_COMPARE_CLAUSE_1 = LoadRule(COMPARE_CLAUSE_RULE_1);
            S_COMPARE_CLAUSE_2 = LoadRule(COMPARE_CLAUSE_RULE_2);

            S_MANNER_CLAUSE_1 = LoadRule(MANNER_CLAUSE_RULE_1);
            S_MANNER_CLAUSE_2 = LoadRule(MANNER_CLAUSE_RULE_2);
            S_MANNER_CLAUSE_3 = LoadRule(MANNER_CLAUSE_RULE_3);

            S_TIME_CLAUSE_1 = LoadRule(TIME_CLAUSE_RULE_1);
            S_TIME_CLAUSE_2 = LoadRule(TIME_CLAUSE_RULE_2);
            S_TIME_CLAUSE_3 = LoadRule(TIME_CLAUSE_RULE_3);
            S_TIME_CLAUSE_4 = LoadRule(TIME_CLAUSE_RULE_4);
        }

        public static String[] LoadRule(String path) {
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
    }


    public static class LanguageTool {
        public static final String[] CategoryName = {"Possible Typo", "Grammar", "Collocations", "Miscellaneous", "Punctuation Errors", "Commonly Confused Words", "Nonstandard Phrases", "Redundant Phrases", "Bad style", "Semantic"};
    }

    public static final class POSTag {
        public static final String CC = "CC";//Coordinating conjunction
        public static final String CD = "CD";//Cardinal number
        public static final String DT = "DT";//Determiner
        public static final String EX = "EX";//Existential there
        public static final String FW = "FW";//Foreign word
        public static final String IN = "IN";//Preposition or subordinating conjunction
        public static final String JJ = "JJ";//Adjective
        public static final String JJR = "JJR";//Adjective, comparative
        public static final String JJS = "JJS";//Adjective, superlative
        public static final String LS = "LS";//List item marker
        public static final String MD = "MD";//Modal
        public static final String NN = "NN";//Noun, singular or mass
        public static final String NNS = "NNS";//Noun, plural
        public static final String NNP = "NNP";//Proper noun, singular
        public static final String NNPS = "NNPS";//Proper noun, plural
        public static final String PDT = "PDT";//Predeterminer
        public static final String POS = "POS";//Possessive ending
        public static final String PRP = "PRP";//Personal pronoun
        public static final String PRP$ = "PRP$";//Possessive pronoun
        public static final String RB = "RB";//Adverb
        public static final String RBR = "RBR";//Adverb, comparative
        public static final String RBS = "RBS";//Adverb, superlative
        public static final String RP = "RP";//Particle
        public static final String SYM = "SYM";//Symbol
        public static final String TO = "TO";//to
        public static final String UH = "UH";//Interjection
        public static final String VB = "VB";//Verb, base form
        public static final String VBD = "VBD";//Verb, past tense
        public static final String VBG = "VBG";//Verb, gerund or present participle
        public static final String VBN = "VBN";//Verb, past participle
        public static final String VBP = "VBP";//Verb, non-3rd person singular present
        public static final String VBZ = "VBZ";//Verb, 3rd person singular present
        public static final String WDT = "WDT";//Wh-determiner
        public static final String WP = "WP";//Wh-pronoun
        public static final String WP$ = "WP$";//Possessive wh-pronoun
        public static final String WRB = "WRB";//Wh-adverb
    }

    public static final class Determiner {
        public static final String A = "a";
        public static final String AN = "an";
        public static final String THE = "the";
    }

    public static final class CountNounDetector {
        public static final String TOKEN_COUNTABLE_PATH = BASE_DIR + "countable_uncountable/dict/token_countable.txt";
    }

}

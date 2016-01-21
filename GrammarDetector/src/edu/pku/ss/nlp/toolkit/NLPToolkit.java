package edu.pku.ss.nlp.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NLPToolkit {

	private static NLPToolkit instance = null;

	private static final String PROPERTIES_PATH = "res/nlptoolkit/nlptoolkit.properties";

	private StanfordCoreNLP pipeline;// = new StanfordCoreNLP();

	private NLPToolkit() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File(PROPERTIES_PATH)));
			System.out.println(props.keys());
			this.pipeline = new StanfordCoreNLP(props);
		} catch (IOException e) {
			Logger.getGlobal().logp(Level.SEVERE, this.getClass().getName(),
					"NLPToolkit", "读取配置文件失败!使用默认配置!");

			this.pipeline = new StanfordCoreNLP(props);
		}
	}

	public static NLPToolkit getInstance() {
		if (null == instance) {
			instance = new NLPToolkit();
		}
		return instance;
	}

	public List<CoreMap> annotate(String inputText) {
		Annotation annotation = new Annotation(inputText);
		pipeline.annotate(annotation);

		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);

		return Collections.unmodifiableList(sentences);
	}

	public static void main(String[] args) throws IOException {
		NLPToolkit.getInstance();
		long start = System.currentTimeMillis();
		String text = "I love you !";
		for(int i = 0; i < 100; ++i){
			NLPToolkit.getInstance().annotate(text);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start) * 1.0 / 1000);
	}
}

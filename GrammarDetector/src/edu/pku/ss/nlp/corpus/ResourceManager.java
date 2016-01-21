package edu.pku.ss.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * 语言资源管理器.
 * 
 * @author nulooper
 * @date 2015年3月24日
 * @time 下午10:39:59
 */
public class ResourceManager {

	private static ResourceManager instance = new ResourceManager();

	private ResourceManager() {

	}

	public static ResourceManager getInstance() {
		if (null == instance) {
			instance = new ResourceManager();
		}

		return instance;
	}

	public static void main(String[] args) throws IOException {
		String sent = "Need I pay now?";
		String lowSent = sent.toLowerCase();
		Pattern pat = Pattern.compile("^need.*\\?$");
		
		System.out.println(sent.length());
		
		Matcher matcher = pat.matcher(lowSent);
		System.out.println(matcher.matches());
		System.out.println(matcher.start(0));
		System.out.println(matcher.end(0));
		List<String> lines = FileUtils.readLines(new File("res/test"));
		for(String line : lines){
			System.out.println(line);
		}
	}
}

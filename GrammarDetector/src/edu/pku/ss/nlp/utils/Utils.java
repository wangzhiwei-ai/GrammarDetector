package edu.pku.ss.nlp.utils;

import java.util.List;

public class Utils {

	public  static <T> String joinOn(List<T> items, String on) {
		if (null == items || items.isEmpty()) {
			return "";
		}

		if (null == on) {
			on = " ";
		}

		int size = items.size();
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (; i < size - 1; ++i) {
			sb.append(items.get(i)).append(on);
		}
		sb.append(items.get(i));

		return sb.toString();
	}
	
	

	public static void main(String[] args) {

	}
}

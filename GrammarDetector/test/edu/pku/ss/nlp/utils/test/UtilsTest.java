package edu.pku.ss.nlp.utils.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.pku.ss.nlp.utils.Utils;

public class UtilsTest {
	@Test
	public void joinOnTest() {
		// list equals null.
		List<String> items = null;
		assertEquals("", Utils.joinOn(items, " "));

		// list is empty.
		items = new ArrayList<String>();
		assertEquals("", Utils.joinOn(items, " "));

		// on equals null
		items = new ArrayList<String>();
		items.add("a");
		items.add("b");
		assertEquals("a b", Utils.joinOn(items, null));

		// on equals "_"
		assertEquals("a_b", Utils.joinOn(items, "_"));
	}
}

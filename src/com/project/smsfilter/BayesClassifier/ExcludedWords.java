package com.project.smsfilter.BayesClassifier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ExcludedWords implements Serializable {

	private static final long serialVersionUID = -1702011584640488772L;

	// List of english words i'm not interested in
	// You might use frequently used words for this list
	static String[] enu_most_common = { "the", "to", "and", "a", "an", "in",
			"is", "it", "you", "that", "was", "for", "on", "are", "with", "as",
			"be", "been", "at", "one", "have", "this", "what", "which", };

	Map<String, Integer> mDict;

	public ExcludedWords() {
		mDict = new HashMap<String, Integer>();
	}

	/**
	 * Initializes for english
	 */
	public void InitDefault() {
		init(enu_most_common);
	}

	/**
	 * 
	 * @param excluded
	 */
	public void init(String[] excluded) {
		mDict.clear();
		for (int i = 0; i < excluded.length; i++) {
			mDict.put(excluded[i], i);
		}
	}

	/**
	 * checks to see if a word is to be excluded
	 * 
	 * @param word
	 * @return
	 */
	public boolean isExcluded(String word) {
		return mDict.containsKey(word);
	}

}

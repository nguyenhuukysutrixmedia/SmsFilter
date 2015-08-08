package com.project.smsfilter.BayesClassifier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Classifier implements IClassifier, Serializable {
	
	private static final long serialVersionUID = -6880879885822158535L;
	protected Map<String, Category> mCategories;
	protected ExcludedWords mExcludedWords;

	public Classifier() {
		mCategories = new HashMap<String, Category>();
		mExcludedWords = new ExcludedWords();
		mExcludedWords.InitDefault();
	}

	protected int countTotalWordsInCategories() {
		int total = 0;
		for (ICategory cat : mCategories.values()) {
			total += ((Category) cat).getTotalWords();
		}
		return total;
	}

	protected int countTotalDistinctWordsInCategories() {
		int total = 0;
		for (ICategory cat : mCategories.values()) {
			total += ((Category) cat).getTotalDistinctWords();
		}
		return total;
	}

	protected Category getOrCreateCategory(String cat) {
		Category c = mCategories.get(cat);
		if (c == null) {
			c = new Category(cat, mExcludedWords);
			mCategories.put(cat, c);
		}
		return c;
	}

	protected Category createCategory(String cat) {
		return new Category(cat, mExcludedWords);
	}

	@Override
	public void teachPhrases(String cat, String phrases) {
		getOrCreateCategory(cat).teachPhrases(phrases);
	}

	@Override
	public void teachCategory(String cat, String[] filePaths) {
		getOrCreateCategory(cat).teachCategory(filePaths);
	}

	@Override
	public Map<String, Double> classify(String rawPhrase) {

		Map<String, Double> score = new HashMap<String, Double>();

		for (Map.Entry<String, Category> entry : mCategories.entrySet()) {
			score.put(
					entry.getValue().getName(),
					Math.log((double) (entry.getValue().getTotalWords())
							/ (double) (this.countTotalWordsInCategories())));
		}

		Category words_in_file = createCategory("");
		words_in_file.teachPhrases(rawPhrase);

		int TotalWordsInCategories = CWordManager.getTotalWords();

		for (Map.Entry<String, PhraseCount> entry : words_in_file.getPhrases().entrySet()) {

			PhraseCount pc_in_file = entry.getValue();
			for (Map.Entry<String, Category> entry1 : mCategories.entrySet()) {
				Category cat = entry1.getValue();
				int phraseCount = cat.getPhraseCount(pc_in_file.getRawPhrase());
				double p = Math
						.log(Math.pow((double) (phraseCount + 1)
								/ (double) (cat.getTotalWords() + TotalWordsInCategories), (double) entry.getValue()
								.getCount()));
				score.put(cat.getName(), score.get(cat.getName()) + p);
			}
		}

		for (Map.Entry<String, Category> entry : mCategories.entrySet()) {
			Category cat = entry.getValue();
			score.put(cat.getName(),
					score.get(cat.getName()) + Math.log((double) cat.getTotalWords() / (double) TotalWordsInCategories));
		}

		return score;
	}
}

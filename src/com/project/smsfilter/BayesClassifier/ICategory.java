package com.project.smsfilter.BayesClassifier;

public interface ICategory {

	public void reset();

	public int getPhraseCount(String phrase);

	public void teachCategory(String[] filePaths);

	public void teachPhrase(String rawPhrase);

	public void teachPhrases(String phrases);
}

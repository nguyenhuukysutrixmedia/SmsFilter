package com.project.smsfilter.BayesClassifier;

import java.util.Map;

public interface IClassifier {
	
	public void teachPhrases(String cat, String phrases);

	public void teachCategory(String cat, String[] filePaths);

	public Map<String, Double> classify(String filePath);
}

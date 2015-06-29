package com.project.smsfilter.BayesClassifier;

import java.io.Serializable;

public class PhraseCount implements Serializable{

	private static final long serialVersionUID = 989576139828846833L;
	private String mRawPhrase;
	private int mCount;

	public String getRawPhrase() {
		return mRawPhrase;
	}

	public int getCount() {
		return mCount;
	}

	public void setCount(int count) {
		mCount = count;
	}

	public PhraseCount(String rawPhrase) {
		mRawPhrase = rawPhrase;
		mCount = 0;
	}
}

package com.project.smsfilter.BayesClassifier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CWordManager implements Serializable {

	private static final long serialVersionUID = -1867990403378448859L;
	private static Lock mWordsLocker = new ReentrantLock();;
	private static Map<String, String> mDicWords = new HashMap<String, String>();

	public static int getTotalWords() {
		int iTotalWords = 0;
		mWordsLocker.lock();
		try {
			iTotalWords = mDicWords.size();
		} finally {
			mWordsLocker.unlock();
		}
		return iTotalWords;
	}

	public static void AddWord(String strWord) {
		mWordsLocker.lock();
		try {
			mDicWords.put(strWord, strWord);
		} finally {
			mWordsLocker.unlock();
		}
	}
}

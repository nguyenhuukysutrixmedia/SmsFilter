package com.project.smsfilter.BayesClassifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Category implements ICategory, Iterable<Map.Entry<String, PhraseCount>>, Serializable {

	private static final long serialVersionUID = -8044543086483500748L;
	private static Pattern msPhraseRegEx = Pattern.compile("\\W");
	protected Map<String, PhraseCount> mPhrases;
	private int mTotalWords;
	private String mName;
	private ExcludedWords mExcluded;

	// PROPERTIES
	public String getName() {
		return mName;
	}

	public int getTotalWords() {
		return mTotalWords;
	}

	public int getTotalDistinctWords() {
		return mPhrases.size();
	}

	Map<String, PhraseCount> getPhrases() {
		return mPhrases;
	}

	// end PROPERTIES
	public Category(String cat, ExcludedWords excluded) {
		mPhrases = new HashMap<String, PhraseCount>();
		mExcluded = excluded;
		mName = cat;
	}

	@Override
	public void reset() {
		mTotalWords = 0;
		mPhrases.clear();
	}

	@Override
	public int getPhraseCount(String phrase) {
		PhraseCount pc = mPhrases.get(phrase);
		if (pc != null)
			return pc.getCount();
		else
			return 0;
	}

	@Override
	public void teachCategory(String[] filePaths) {

		Pattern pattern = Pattern.compile("(\\w+)\\W*");
		String line;
		for (String filePath : filePaths) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(filePath));

				while (null != (line = reader.readLine())) {
					Matcher matcher = pattern.matcher(line);
					while (matcher.find()) {
						String word = matcher.group(1);
						teachPhrase(word);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void teachPhrase(String rawPhrase) {

		if ((null != mExcluded) && (mExcluded.isExcluded(rawPhrase)))
			return;

		String Phrase = DePhrase(rawPhrase);
		PhraseCount pc = mPhrases.get(Phrase);

		if (null == pc) {
			pc = new PhraseCount(rawPhrase);
			mPhrases.put(Phrase, pc);
		}

		pc.setCount(pc.getCount() + 1);
		mTotalWords++;

		CWordManager.AddWord(Phrase);
	}

	@Override
	public void teachPhrases(String phrases) {

		Pattern pattern = Pattern.compile("([\\w|\\.]+)\\W*");
		try {
			String[] lines = phrases.split(System.getProperty("line.separator"));
			for (String line : lines) {
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					String word = matcher.group(1);
					teachPhrase(word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean CheckIsPhrase(String s) {
		return msPhraseRegEx.matcher(s).matches();
	}

	public static String DePhrase(String s) {
		StringBuilder sb = new StringBuilder();
		String[] ws = s.split("_");
		for (int i = 0; i < ws.length; i++) {
			ws[i] = msPhraseRegEx.matcher(ws[i]).replaceAll("");
			if (ws[i].length() > 0) {
				sb.append(ws[i]);
				sb.append('_');
			}
		}

		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '_') {
			sb = sb.delete(sb.length() - 1, sb.length());
		}

		return sb.toString();
	}

	@Override
	public Iterator<Entry<String, PhraseCount>> iterator() {
		return mPhrases.entrySet().iterator();
	}

}

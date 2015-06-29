package com.project.smsfilter.unittest;

import java.util.ArrayList;
import java.util.Map;

import android.test.AndroidTestCase;

import com.project.smsfilter.BayesClassifier.Classifier;
import com.project.smsfilter.database.SmsTableHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SMSModel;
import com.project.smsfilter.model.SmsTestModel;

public class BayesClassifierTest extends AndroidTestCase {

	final String NORMAL = "Normal";
	final String SPAM = "Spam";

	private SmsTableHelper mSmsTableHelper;
	private SmsTestTableHelper mSmstTestTableHelper;
	private Classifier classifier;

	public void setUp() {
		mSmsTableHelper = new SmsTableHelper(mContext);
		mSmstTestTableHelper = new SmsTestTableHelper(mContext);
		classifier = new Classifier();
	}

	public void testTrain() {

		// ArrayList<SMSModel> lstSmses = mSmsTableHelper.getAll();

		ArrayList<SMSModel> lstSpamSmses = mSmsTableHelper.getListSpam();
		ArrayList<SMSModel> lstNormalSmses = mSmsTableHelper.getListNotSpam();

		for (SMSModel sms : lstSpamSmses) {
			String strPhrases = sms.getFormatContent().replace(". ", "\r\n");
			classifier.teachPhrases(SPAM, strPhrases);
		}

		for (SMSModel sms : lstNormalSmses) {
			String strPhrases = sms.getFormatContent().replace(". ", "\r\n");
			classifier.teachPhrases(NORMAL, strPhrases);
		}

	}

	public void testTest() {
		testTrain();
		ArrayList<SmsTestModel> lstSmses = mSmstTestTableHelper.getAll();
		for (SmsTestModel sms : lstSmses) {

			String strPhrases = sms.getFormatContent().replace(". ", "\r\n");
			Map<String, Double> dicScores = classifier.classify(strPhrases);
			double spamValue = dicScores.get(SPAM) == null ? 0 : dicScores.get(SPAM);
			double normalValue = dicScores.get(NORMAL) == null ? 0 : dicScores.get(NORMAL);
			if (spamValue > normalValue) {
				sms.setSpam(true);
			} else {
				sms.setSpam(false);
			}

			mSmstTestTableHelper.update(sms);
		}

		
		ArrayList<SmsTestModel> lstSpamSmses = mSmstTestTableHelper.getListSpam();
		ArrayList<SmsTestModel> lstNormalSmses = mSmstTestTableHelper.getListNotSpam();
		lstSmses = mSmstTestTableHelper.getAll();
		
		assertTrue(lstSpamSmses.size() > 0);
		assertTrue(lstNormalSmses.size() > 0);
		assertTrue(lstSmses.size() > 0);
		assertTrue(lstSmses.size() == lstSpamSmses.size() + lstNormalSmses.size());		
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}

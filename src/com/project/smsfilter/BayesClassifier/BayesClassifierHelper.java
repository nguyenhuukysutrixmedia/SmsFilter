package com.project.smsfilter.BayesClassifier;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import android.content.Context;

import com.google.gson.Gson;
import com.project.smsfilter.MyApplication;
import com.project.smsfilter.database.SmsTableHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SMSModel;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyPreferenceUtils;

public class BayesClassifierHelper {

	private static final String FILE_NAME = "BayesClassifierHelper.bin";

	public static final String NORMAL = "Normal";
	public static final String SPAM = "Spam";

	private SmsTestTableHelper mSmsTestTableHelper;
	private SmsTableHelper mSmsTableHelper;
	private Context mContext;

	public BayesClassifierHelper(Context context) {
		mContext = context;
		mSmsTableHelper = new SmsTableHelper(mContext);
		mSmsTestTableHelper = new SmsTestTableHelper(mContext);
	}

	public void analyzeSms() {
		long startTime = System.currentTimeMillis();
		// loadClassifierFromPrefe();
		// if (MyApplication.mClassifier == null) {
		ArrayList<SmsTestModel> listSmsModels = mSmsTestTableHelper.getListNeedFilter();
		if (listSmsModels.size() > 0) {
			if (MyApplication.mClassifier == null)
				MyApplication.mClassifier = new Classifier();
			trainSms();
			filterSpamSms(listSmsModels);
		}
		// saveClassifierToPrefe();
		// }

		MyLog.iLog(String.format(Locale.getDefault(), "BayesClassifierHelper analyze Sms time: %d ms",
				System.currentTimeMillis() - startTime));
	}

	/**
	 * 
	 * @param listSmsModels
	 */
	public SmsTestModel analyzeSms(SmsTestModel smsTestModel) {
		if (MyApplication.mClassifier == null) {
			MyApplication.mClassifier = new Classifier();
			trainSms();
		}
		return filterSpamSms(smsTestModel);
	}

	public void trainSms() {

		long startTime = System.currentTimeMillis();
		ArrayList<SMSModel> lstSpamSmses = mSmsTableHelper.getListSpam();
		ArrayList<SMSModel> lstNormalSmses = mSmsTableHelper.getListNotSpam();

		MyLog.iLog(String.format(Locale.getDefault(), "Sms data size: %d", lstSpamSmses.size()));

		for (SMSModel sms : lstSpamSmses) {
			String strPhrases = sms.getFormatContent().replace(". ", "\r\n");
			MyApplication.mClassifier.teachPhrases(SPAM, strPhrases);
		}
		MyLog.iLog(String.format(Locale.getDefault(), "Train Sms spam size: %d - time: %d ms", lstSpamSmses.size(),
				System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		for (SMSModel sms : lstNormalSmses) {
			String strPhrases = sms.getFormatContent().replace(". ", "\r\n");
			MyApplication.mClassifier.teachPhrases(NORMAL, strPhrases);
		}
		MyLog.iLog(String.format(Locale.getDefault(), "Train Sms normal size: %d - time: %d ms", lstNormalSmses.size(),
				System.currentTimeMillis() - startTime));
	}

	private void filterSpamSms(ArrayList<SmsTestModel> listSmsModels) {

		long startTime = System.currentTimeMillis();

		for (SmsTestModel sms : listSmsModels) {

			String strPhrases = sms.getFormatContent().replace(". ", "\r\n");
			Map<String, Double> dicScores = MyApplication.mClassifier.classify(strPhrases);
			double spamValue = dicScores.get(SPAM) == null ? 0 : dicScores.get(SPAM);
			double normalValue = dicScores.get(NORMAL) == null ? 0 : dicScores.get(NORMAL);
			if (spamValue > normalValue) {
				sms.setSpam(true);
			} else {
				sms.setSpam(false);
			}
			sms.setReviewed(true);
			mSmsTestTableHelper.update(sms);
		}
		MyLog.iLog(String.format(Locale.getDefault(), "Filter spam Sms size %d - time: %d ms", listSmsModels.size(),
				System.currentTimeMillis() - startTime));
	}

	private SmsTestModel filterSpamSms(SmsTestModel smsTestModel) {

		long startTime = System.currentTimeMillis();

		String strPhrases = smsTestModel.getFormatContent().replace(". ", "\r\n");
		Map<String, Double> dicScores = MyApplication.mClassifier.classify(strPhrases);
		double spamValue = dicScores.get(SPAM) == null ? 0 : dicScores.get(SPAM);
		double normalValue = dicScores.get(NORMAL) == null ? 0 : dicScores.get(NORMAL);
		if (spamValue > normalValue) {
			smsTestModel.setSpam(true);
		} else {
			smsTestModel.setSpam(false);
		}
		smsTestModel.setReviewed(true);
		MyLog.iLog(String.format(Locale.getDefault(), "Filter spam Sms - time: %d ms", System.currentTimeMillis()
				- startTime));
		return smsTestModel;
	}

	// private void saveClassifierToFile() {
	// try {
	// long startTime = System.currentTimeMillis();
	// FileOutputStream fos = mContext.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
	// ObjectOutputStream os = new ObjectOutputStream(fos);
	// os.writeObject(MyApplication.mClassifier);
	// os.close();
	// fos.close();
	// MyLog.iLog("Saved Classifier To File time: " + (System.currentTimeMillis() - startTime));
	// } catch (IOException e) {
	// e.printStackTrace();
	// MyLog.eLog("BayesClassifierHelper-saveClassifierToFile error: " + e);
	// }
	// }

	private void loadClassifierFromFile() {
		try {
			long startTime = System.currentTimeMillis();
			FileInputStream fis = mContext.openFileInput(FILE_NAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			MyApplication.mClassifier = (Classifier) is.readObject();
			is.close();
			fis.close();
			MyLog.iLog("Loaded Classifier To File time: " + (System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			MyApplication.mClassifier = null;
			// saveClassifierToFile();
			e.printStackTrace();
			MyLog.eLog("BayesClassifierHelper-loadClassifierFromFile error: " + e);
		}
	}

	private void saveClassifierToPrefe() {
		try {
			long startTime = System.currentTimeMillis();

			Gson gson = new Gson();
			MyPreferenceUtils.saveString(mContext, MyPreferenceUtils.KEY_Classifier,
					gson.toJson(MyApplication.mClassifier));

			MyLog.iLog("saveClassifierToPrefe time: " + (System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eLog("BayesClassifierHelper-saveClassifierToSdcard error: " + e);
		}
	}

	private void loadClassifierFromPrefe() {
		try {
			long startTime = System.currentTimeMillis();

			Gson gson = new Gson();
			MyApplication.mClassifier = gson.fromJson(
					MyPreferenceUtils.getString(mContext, MyPreferenceUtils.KEY_Classifier, ""), Classifier.class);

			MyLog.iLog("loadClassifierFromPrefe time: " + (System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			MyApplication.mClassifier = null;
			saveClassifierToPrefe();
			e.printStackTrace();
			MyLog.eLog("BayesClassifierHelper-loadClassifierFromSdcard error: " + e);
		}
	}

}

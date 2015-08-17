package com.project.smsfilter.background;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;

import com.project.smsfilter.MyApplication;
import com.project.smsfilter.BayesClassifier.BayesClassifierHelper;
import com.project.smsfilter.BayesClassifier.Classifier;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.Defines;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyNotificationHelper;

public class SmsObserver extends ContentObserver {

	private SmsTestTableHelper mSmsTestTableHelper;
	// private SharedPreferences trackMeData;
	private Context mContext;

	public SmsObserver(Handler handler, final Context c) {
		super(handler);
		// trackMeData = c.getSharedPreferences("LockedSIM", 0);
		this.mContext = c;
		mSmsTestTableHelper = new SmsTestTableHelper(mContext);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		MyLog.iLog("SMS changed");

		//
		checkNewOrDeleteSms();

		// CommonUtils.reAnalyzeSms(mContext);

		// send broadcast update UI
		Intent in = new Intent(Defines.NEW_SMS_RECEIVER_TAG);
		mContext.sendBroadcast(in);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void checkNewOrDeleteSms() {

		long startTime = System.currentTimeMillis();
		ArrayList<SmsTestModel> listSmsOnDevice = MySMSUtils.readAllSMS(mContext);
		ArrayList<SmsTestModel> listSmsOnDB = mSmsTestTableHelper.getAll();

		// check for new sms
		for (SmsTestModel smsTestModel : listSmsOnDevice) {

			if (!mSmsTestTableHelper.checkExist(smsTestModel)) {

				analyzeNewSms(smsTestModel);

				// insert if not exist
				mSmsTestTableHelper.insert(smsTestModel);
			} 
		}

		// check for deleted sms
		ArrayList<SmsTestModel> clone = (ArrayList<SmsTestModel>) listSmsOnDB.clone();
		for (SmsTestModel smsTestModel : listSmsOnDB) {

			if (!listSmsOnDevice.contains(smsTestModel)) {
				mSmsTestTableHelper.delete(smsTestModel.getUid() + "");
				clone.remove(smsTestModel);
			}
		}
		listSmsOnDB = (ArrayList<SmsTestModel>) clone.clone();

		MyLog.iLog(String.format(Locale.getDefault(), "SMS changed checkNewOrDeleteSms time: %d ms",
				System.currentTimeMillis() - startTime));
	}

	/**
	 * 
	 */
	public void analyzeNewSms(SmsTestModel smsTestModel) {

		long startTime = System.currentTimeMillis();
		BayesClassifierHelper bayesClassifierHelper = new BayesClassifierHelper(mContext);
		if (bayesClassifierHelper.analyzeSms(smsTestModel).isSpam()) {
			smsTestModel.setSpam(true);
		} else {
			smsTestModel.setSpam(false);
			MyNotificationHelper.sendNewSMSNotification(mContext, smsTestModel.getPhoneName(),
					smsTestModel.getContent());
		}
		// smsTestModel.setReviewed(true);
		MyLog.iLog(String.format(Locale.getDefault(), "BayesClassifierHelper analyze Sms time: %d ms",
				System.currentTimeMillis() - startTime));
	}

}
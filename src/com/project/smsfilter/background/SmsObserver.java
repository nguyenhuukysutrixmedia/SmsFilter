package com.project.smsfilter.background;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;

import com.project.smsfilter.MyApplication;
import com.project.smsfilter.BayesClassifier.BayesClassifierHelper;
import com.project.smsfilter.BayesClassifier.Classifier;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.CommonUtils;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyNotificationHelper;
import com.project.smsfilter.utilities.MyUtils;

public class SmsObserver extends ContentObserver {

	SharedPreferences trackMeData;
	private Context c;

	public SmsObserver(Handler handler, final Context c) {
		super(handler);
		trackMeData = c.getSharedPreferences("LockedSIM", 0);
		this.c = c;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);

		MyLog.iLog("SMS changed");
		CommonUtils.reAnalyzeSms(c);
		// send broadcast update UI
		Intent in = new Intent(NewSmsTask.TAG);
		c.sendBroadcast(in);
	}
	
	public static void reAnalyzeSms(Context context) {
		
		long startTime = System.currentTimeMillis();
		SmsTestTableHelper mSmsTestTableHelper = new SmsTestTableHelper(context);

		// mSmsTestTableHelper.deleteAll();
		ArrayList<SmsTestModel> listSmsModels = MySMSUtils.readAllSMS(context);
		ArrayList<SmsTestModel> listNewSMS;
		// if (listSmsModels.size() <= 0)
		// CreateTestData.createTestSms(mContext);
		for (SmsTestModel smsModel : listSmsModels) {
			// MyLog.iLog("SMS:: " + smsModel.toString());
			if (!mSmsTestTableHelper.checkExist(smsModel)) {
				mSmsTestTableHelper.insert(smsModel);
			}
		}

		
		
		// loadClassifierFromPrefe();
		// if (MyApplication.mClassifier == null) {
		listSmsModels = mSmsTestTableHelper.getListNeedFilter();
		if (listSmsModels.size() > 0) {
			if (MyApplication.mClassifier == null)
				MyApplication.mClassifier = new Classifier();
//			trainSms();
//			filterSpamSms(listSmsModels);
		}
//		String phoneName = MyUtils.isEmptyString(smsModel.getPhoneName())
//				? smsModel.getPhoneNumber()
//				: smsModel.getPhoneName();
//		MyNotificationHelper.sendSynchonizeNotification(context, phoneName, smsModel.getContent());
		
		// saveClassifierToPrefe();
		// }

		MyLog.iLog(String.format(Locale.getDefault(), "BayesClassifierHelper analyze Sms time: %d ms",
				System.currentTimeMillis() - startTime));
		
		BayesClassifierHelper bayesClassifierHelper = new BayesClassifierHelper(context);
		bayesClassifierHelper.analyzeSms();
	}

}
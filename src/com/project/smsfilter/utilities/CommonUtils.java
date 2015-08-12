package com.project.smsfilter.utilities;

import java.util.ArrayList;

import android.content.Context;

import com.project.smsfilter.BayesClassifier.BayesClassifierHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.gui.SplashActivity;
import com.project.smsfilter.model.SMSModel;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.MySMSUtils;

public class CommonUtils {
	
	public static void reAnalyzeSms(Context context){
		SmsTestTableHelper  smsTestTableHelper = new SmsTestTableHelper(context);
		
		// mSmsTestTableHelper.deleteAll();
		ArrayList<SmsTestModel> listSmsModels = MySMSUtils.readAllSMS(context);
		// if (listSmsModels.size() <= 0)
		// CreateTestData.createTestSms(mContext);
		for (SmsTestModel smsModel : listSmsModels) {
			// MyLog.iLog("SMS:: " + smsModel.toString());
			smsTestTableHelper.insert(smsModel);
		}

		BayesClassifierHelper bayesClassifierHelper = new BayesClassifierHelper(context);
		bayesClassifierHelper.analyzeSms();
	}

}

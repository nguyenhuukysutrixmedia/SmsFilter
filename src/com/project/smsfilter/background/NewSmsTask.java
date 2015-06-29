package com.project.smsfilter.background;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.project.smsfilter.BayesClassifier.BayesClassifierHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.unittest.CreateTestData;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyNotificationHelper;
import com.project.smsfilter.utilities.MySMSUtils;

public class NewSmsTask extends AsyncTask<Object, Integer, String> {

	public static String TAG = "IncomingSmsReceiver";

	private String mPhoneNumber;
	private String mMessage;
	private Context mContext;

	public NewSmsTask(Context context, String phoneNumber, String message) {
		mPhoneNumber = phoneNumber;
		mMessage = message;
		mContext = context;
	}

	@Override
	protected String doInBackground(Object... params) {

		SmsTestTableHelper smsTestTableHelper = new SmsTestTableHelper(mContext);
		BayesClassifierHelper bayesClassifierHelper = new BayesClassifierHelper(mContext);

		SmsTestModel smsTestModel = new SmsTestModel();
		smsTestModel.setContent(mMessage);
		smsTestModel.setPhoneNumber(mPhoneNumber);

		// load sms data
		ArrayList<SmsTestModel> listSmsModels = MySMSUtils.readSMSInbox(mContext);
		for (SmsTestModel smsModel : listSmsModels) {
			//MyLog.iLog("SMS:: " + smsModel.toString());
			smsTestTableHelper.insert(smsModel);
		}

		if (bayesClassifierHelper.analyzeSms(smsTestModel).isSpam()) {

			SmsTestModel model = smsTestTableHelper.getSmsByContent(mMessage);
			if (model != null) {
				model.setSpam(true);
				model.setReviewed(true);
				smsTestTableHelper.update(model);
			}
			// MyNotificationHelper.sendSynchonizeNotification(mContext, smsTestModel);
		} else {

			SmsTestModel model = smsTestTableHelper.getSmsByContent(mMessage);
			if (model != null) {
				model.setSpam(false);
				model.setReviewed(true);
				smsTestTableHelper.update(model);
			}
			MyNotificationHelper.sendSynchonizeNotification(mContext, smsTestModel);
		}

		// send broadcast update UI
		Intent in = new Intent(TAG);
		in.putExtra("phoneNumber", mPhoneNumber);
		in.putExtra("message", mMessage);
		mContext.sendBroadcast(in);

		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
	}

}

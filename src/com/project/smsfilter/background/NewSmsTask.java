package com.project.smsfilter.background;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.SmsMessage;

import com.project.smsfilter.BayesClassifier.BayesClassifierHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyNotificationHelper;

public class NewSmsTask extends AsyncTask<Object, Integer, String> {

	public static String TAG = "IncomingSmsReceiver";

	private String mPhoneNumber;
	private String mPhoneName;
	private Uri mNewSmsUri;
	private long mCreateTime;
	private String mMessage;
	private Context mContext;

	// public NewSmsTask(Context context, String phoneNumber, String message) {
	// mPhoneNumber = phoneNumber;
	// mMessage = message;
	// mContext = context;
	// }

	public NewSmsTask(Context context, SmsMessage currentMessage, Uri newSmsUri) {
		mContext = context;
		mPhoneName = currentMessage.getDisplayOriginatingAddress();
		mPhoneNumber = currentMessage.getOriginatingAddress();
		mMessage = currentMessage.getDisplayMessageBody();
		mCreateTime = currentMessage.getTimestampMillis();
		mNewSmsUri = newSmsUri;
	}

	@Override
	protected String doInBackground(Object... params) {

		SmsTestTableHelper smsTestTableHelper = new SmsTestTableHelper(mContext);
		BayesClassifierHelper bayesClassifierHelper = new BayesClassifierHelper(mContext);

		SmsTestModel smsTestModel = new SmsTestModel();
		smsTestModel.setContent(mMessage);
		smsTestModel.setPhoneNumber(mPhoneNumber);
		smsTestModel.setPhoneName(mPhoneName);
		smsTestModel.setCreateTime(mCreateTime);
		try {
			smsTestModel.setId(Long.parseLong(mNewSmsUri.getLastPathSegment()));
		} catch (Exception e) {
		}
	

		// load sms data
		// ArrayList<SmsTestModel> listSmsModels = MySMSUtils.readSMSInbox(mContext);
		// for (SmsTestModel smsModel : listSmsModels) {
		// MyLog.iLog("SMS:: " + smsModel.toString());
		// smsTestTableHelper.insert(smsModel);
		// }

		if (bayesClassifierHelper.analyzeSms(smsTestModel).isSpam()) {

			// SmsTestModel model = smsTestTableHelper.getSmsByContent(mMessage);
			// if (model != null) {
			smsTestModel.setSpam(true);
			smsTestModel.setReviewed(true);
			// smsTestTableHelper.update(model);
			// }
			// MyNotificationHelper.sendSynchonizeNotification(mContext, smsTestModel);
		} else {
			// SmsTestModel model = smsTestTableHelper.getSmsByContent(mMessage);
			// if (model != null) {
			smsTestModel.setSpam(false);
			smsTestModel.setReviewed(true);
			// smsTestTableHelper.update(model);
			// }
			MyNotificationHelper.sendSynchonizeNotification(mContext, smsTestModel);
		}
		MyLog.iLog("New sms coming: " + smsTestModel);
		
		if (!smsTestTableHelper.checkExist(smsTestModel))
			smsTestTableHelper.insert(smsTestModel);

		// send broadcast update UI
		Intent in = new Intent(TAG);
		in.putExtra("phoneNumber", mPhoneNumber);
		in.putExtra("phoneName", mPhoneName);
		in.putExtra("message", mMessage);
		mContext.sendBroadcast(in);

		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);
	}

}

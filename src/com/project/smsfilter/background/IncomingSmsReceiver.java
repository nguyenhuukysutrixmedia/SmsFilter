package com.project.smsfilter.background;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.project.smsfilter.utilities.MyLog;

public class IncomingSmsReceiver extends BroadcastReceiver {

	// Get the object of SmsManager
	// final SmsManager sms = SmsManager.getDefault();

	@Override
	public void onReceive(Context context, Intent intent) {
		long startTime = System.currentTimeMillis();
		try {
			if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
					|| intent.getAction().equals(Telephony.Sms.Intents.SMS_DELIVER_ACTION)) {

				// Retrieves a map of extended data from the intent.
				final Bundle bundle = intent.getExtras();
				if (bundle != null) {
					final Object[] pdusObj = (Object[]) bundle.get("pdus");
					for (int i = 0; i < pdusObj.length; i++) {

						SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

						String phoneNumber = currentMessage.getDisplayOriginatingAddress();
						String message = currentMessage.getDisplayMessageBody();

						ContentValues values = new ContentValues();
						values.put("address", phoneNumber);
						values.put("body", message);
						context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);

						new NewSmsTask(context, phoneNumber, message).execute();

						MyLog.iLog("IncomingSmsReceiver senderNum: " + phoneNumber + "; message: " + message);
					} // end for loop
					abortBroadcast();
				}
			} // bundle is null

			// this.abortBroadcast();
		} catch (Exception e) {
			MyLog.eLog("Exception IncomingSmsReceiver: " + e);
		}
		MyLog.iLog("IncomingSmsReceiver - onReceive time: " + (System.currentTimeMillis() - startTime));
	}

}

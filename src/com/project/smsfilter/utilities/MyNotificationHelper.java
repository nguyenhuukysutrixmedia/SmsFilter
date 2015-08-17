package com.project.smsfilter.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.project.smsfilter.R;
import com.project.smsfilter.gui.MainActivity;

public class MyNotificationHelper {

	public static final int SYNC_NOTIFICATION_ID = 1;

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	// public static void sendSynchonizeNotification(Context context, SmsTestModel smsTestModel) {
	//
	// Boolean isNotifi = MyPreferenceUtils.isNewSMSNotification(context);
	//
	// String title = String.format("Received new message");
	// String phoneName = smsTestModel.getPhoneNumber();
	// if (smsTestModel.getPhoneName() != null && !smsTestModel.getPhoneName().isEmpty())
	// phoneName = smsTestModel.getPhoneName();
	// String message = phoneName + ": " + smsTestModel.getContent();
	//
	// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
	// mBuilder.setSmallIcon(R.drawable.notifi_icon);
	// mBuilder.setContentTitle(title);
	// mBuilder.setContentText(message);
	// if (isNotifi) {
	// mBuilder.setVibrate(new long[]{300, 300, 300, 300, 300});
	// Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	// mBuilder.setSound(alarmSound);
	// }
	// mBuilder.setAutoCancel(true);
	//
	// // Creates an explicit intent for an Activity in your app
	// Intent resultIntent = new Intent(context, MainActivity.class);
	// resultIntent.setAction(Intent.ACTION_MAIN);
	// resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	// resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	//
	// // PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	// PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
	// PendingIntent.FLAG_UPDATE_CURRENT);
	//
	// mBuilder.setContentIntent(resultPendingIntent);
	// NotificationManager mNotificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// // mId allows you to update the notification later on.
	// mNotificationManager.notify(SYNC_NOTIFICATION_ID, mBuilder.build());
	// }

	public static void sendNewSMSNotification(Context context, String phoneName, String content) {

		Boolean isNotifi = MyPreferenceUtils.isNewSMSNotification(context);

		String title = String.format("Received new message");
		String message = phoneName + ": " + content;

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setSmallIcon(R.drawable.notifi_icon);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(message);
		if (isNotifi) {
			mBuilder.setVibrate(new long[]{300, 300, 300, 300, 300});
			Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			mBuilder.setSound(alarmSound);
		}
		mBuilder.setAutoCancel(true);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.setAction(Intent.ACTION_MAIN);
		resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(SYNC_NOTIFICATION_ID, mBuilder.build());
	}
}

package com.project.smsfilter.sms;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.Defines.SmsColumn;
import com.project.smsfilter.sms.Defines.SmsType;
import com.project.smsfilter.sms.Defines.SmsUri;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyUtils;

public class LoadDataHelper implements SmsUri, SmsColumn, SmsType {

	public static ArrayList<SmsTestModel> loadSMSByThreadNotSpam(Context context) {

		long startTime = System.currentTimeMillis();
		ArrayList<SmsTestModel> listSMS = new ArrayList<SmsTestModel>();
		ContentResolver contentResolver = context.getContentResolver();
		// Cursor c = contentResolver.query(ALL, null, null, null, "date desc");
		Cursor c = contentResolver.query(Uri.parse("content://mms-sms/conversations"), null, null, null, "date desc");
		// activity.startManagingCursor(c);

		// Read the sms data and store it in the list

		if (c.moveToFirst()) {

			for (int i = 0; i < c.getCount(); i++) {

				// { "_id", "thread_id", "address", "person", "date", "body", "type" }

				int type = c.getInt(c.getColumnIndexOrThrow(Defines.SmsColumn.TYPE));
				String phoneNumber = c.getString(c.getColumnIndexOrThrow(Defines.SmsColumn.ADDRESS));
				if (type != MESSAGE_TYPE_DRAFT && !MyUtils.isEmptyString(phoneNumber)) {
					SmsTestModel sms = new SmsTestModel(c);
					sms.setPhoneName(MySMSUtils.getContactName(contentResolver, sms.getPhoneNumber()));

					MyLog.iLog("sms: " + sms);

					listSMS.add(sms);
				}
				c.moveToNext();
			}
		}
		c.close();
		MyLog.iLog(String.format(Locale.getDefault(), "Read SMS Inbox size: %d - time: %d ms", listSMS.size(),
				System.currentTimeMillis() - startTime));
		return listSMS;
	}

	public static ArrayList<SmsTestModel> loadSMSByThreadSpam(Context context) {

		long startTime = System.currentTimeMillis();
		ArrayList<SmsTestModel> listSMS = new ArrayList<SmsTestModel>();
		ContentResolver contentResolver = context.getContentResolver();
		// Cursor c = contentResolver.query(ALL, null, null, null, "date desc");

		Uri uri = Uri.parse("content://mms-sms/conversations");
		String selection = String.format("%s NOT IN ?", _ID);
		String[] selectionArgs = new String[]{"(SELECT Categories.id FROM Categories)"};
		String orderBy = "date desc";
		Cursor c = query(context, uri, selection, selectionArgs, orderBy);
		// Cursor c = contentResolver.query(Uri.parse("content://mms-sms/conversations"), null, null, null,
		// "date desc");
		// activity.startManagingCursor(c);

		// Read the sms data and store it in the list

		if (c.moveToFirst()) {

			for (int i = 0; i < c.getCount(); i++) {

				// { "_id", "thread_id", "address", "person", "date", "body", "type" }

				int type = c.getInt(c.getColumnIndexOrThrow(Defines.SmsColumn.TYPE));
				String phoneNumber = c.getString(c.getColumnIndexOrThrow(Defines.SmsColumn.ADDRESS));
				if (type != MESSAGE_TYPE_DRAFT && !MyUtils.isEmptyString(phoneNumber)) {
					SmsTestModel sms = new SmsTestModel(c);
					sms.setPhoneName(MySMSUtils.getContactName(contentResolver, sms.getPhoneNumber()));

					MyLog.iLog("sms: " + sms);

					listSMS.add(sms);
				}
				c.moveToNext();
			}
		}
		c.close();
		MyLog.iLog(String.format(Locale.getDefault(), "Read SMS Inbox size: %d - time: %d ms", listSMS.size(),
				System.currentTimeMillis() - startTime));
		return listSMS;
	}
	/**
	 * 
	 * @param context
	 * @param uri
	 * @param projection
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 */
	private static Cursor query(Context context, Uri uri, String selection, String selectionArgs[], String orderBy) {
		try {

			ContentResolver contentResolver = context.getContentResolver();
			Cursor cursor = contentResolver.query(uri, null, selection, selectionArgs, orderBy);
			return cursor;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

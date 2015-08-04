package com.project.smsfilter.sms;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;

import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.Defines.SmsColumn;
import com.project.smsfilter.sms.Defines.SmsType;
import com.project.smsfilter.sms.Defines.SmsUri;
import com.project.smsfilter.utilities.MyLog;

public class MySMSUtils implements SmsUri, SmsColumn, SmsType {

	// public static ArrayList<SmsTestModel> readSMSInbox(Context activity) {
	//
	// long startTime = System.currentTimeMillis();
	// ArrayList<SmsTestModel> listSMS = new ArrayList<SmsTestModel>();
	//
	// Cursor c = activity.getContentResolver().query(INBOX, null, null, null, null);
	// // activity.startManagingCursor(c);
	//
	// // Read the sms data and store it in the list
	// if (c.moveToFirst()) {
	// for (int i = 0; i < c.getCount(); i++) {
	//
	// // { "_id", "thread_id", "address", "person", "date", "body" }
	// SmsTestModel sms = new SmsTestModel();
	// sms.setContent(c.getString(c.getColumnIndexOrThrow(BODY)));
	// sms.setPhoneNumber(c.getString(c.getColumnIndexOrThrow(ADDRESS)));
	// sms.setPhoneName(getContactName(activity, sms.getPhoneNumber()));
	// sms.setId(c.getLong(c.getColumnIndexOrThrow(Defines.SmsColumn._ID)));
	// sms.setCreateTime(Long.parseLong(c.getString(c.getColumnIndexOrThrow(DATE))));
	// sms.setState(c.getString(c.getColumnIndexOrThrow(STATUS)));
	// sms.setType(c.getString(c.getColumnIndexOrThrow(TYPE)));
	//
	// MyLog.iLog("sms type: " + sms);
	//
	// listSMS.add(sms);
	// c.moveToNext();
	// }
	// }
	// c.close();
	// MyLog.iLog(String.format(Locale.getDefault(), "Read SMS Inbox size: %d - time: %d ms", listSMS.size(),
	// System.currentTimeMillis() - startTime));
	// return listSMS;
	// }

	/**
	 * 
	 * @param activity
	 * @return
	 */
	public static ArrayList<SmsTestModel> readAllSMS(Context activity) {

		long startTime = System.currentTimeMillis();
		ArrayList<SmsTestModel> listSMS = new ArrayList<SmsTestModel>();

		Cursor c = activity.getContentResolver().query(ALL, null, null, null, null);
		// activity.startManagingCursor(c);

		// Read the sms data and store it in the list

		if (c.moveToFirst()) {

			ContentResolver contentResolver = activity.getContentResolver();
			for (int i = 0; i < c.getCount(); i++) {

				// { "_id", "thread_id", "address", "person", "date", "body", "type" }
				SmsTestModel sms = new SmsTestModel();
				sms.setContent(c.getString(c.getColumnIndexOrThrow(Defines.SmsColumn.BODY)));
				sms.setPhoneNumber(c.getString(c.getColumnIndexOrThrow(Defines.SmsColumn.ADDRESS)));
				sms.setPhoneName(getContactName(contentResolver, sms.getPhoneNumber()));
				sms.setId(c.getLong(c.getColumnIndexOrThrow(Defines.SmsColumn._ID)));
				sms.setThreadId(c.getLong(c.getColumnIndexOrThrow(Defines.SmsColumn.THREAD_ID)));
				sms.setCreateTime(Long.parseLong(c.getString(c.getColumnIndexOrThrow(Defines.SmsColumn.DATE))));
				sms.setState(c.getString(c.getColumnIndexOrThrow(Defines.SmsColumn.STATUS)));
				sms.setType(c.getInt(c.getColumnIndexOrThrow(Defines.SmsColumn.TYPE)));

				MyLog.iLog("sms: " + sms);

				listSMS.add(sms);
				c.moveToNext();
			}
		}
		c.close();
		MyLog.iLog(String.format(Locale.getDefault(), "Read SMS Inbox size: %d - time: %d ms", listSMS.size(),
				System.currentTimeMillis() - startTime));
		return listSMS;
	}
	public static String getContactName(ContentResolver cr, String phoneNumber) {

		String contactName = null;
		try {

			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
			Cursor cursor = cr.query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
			if (cursor == null) {
				return null;
			}
			if (cursor.moveToFirst()) {
				contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			MyLog.iLog("sms-getContactName phone number error: " + phoneNumber);
		}
		return contactName;
	}
	public static Drawable openPhoto(Context context, long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri, new String[]{Contacts.Photo.PHOTO}, null, null,
				null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					// Bitmap bm = (new BitmapFactory()).decodeStream(new ByteArrayInputStream(data));
					Drawable bm = new BitmapDrawable(new ByteArrayInputStream(data));
					return bm;
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}

	public static Bitmap fetchThumbnail(Context context, String phoneNumber) {

		final String[] PHOTO_ID_PROJECTION = new String[]{ContactsContract.Contacts.PHOTO_ID};
		final String[] PHOTO_BITMAP_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO};

		ContentResolver contentResolver = context.getContentResolver();

		Uri uri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = contentResolver.query(uri, PHOTO_ID_PROJECTION, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		try {
			Integer thumbnailId = null;
			if (cursor.moveToFirst()) {
				thumbnailId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));

				if (thumbnailId != null) {
					uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, thumbnailId);
					cursor = contentResolver.query(uri, PHOTO_BITMAP_PROJECTION, null, null, null);

					try {
						Bitmap thumbnail = null;
						if (cursor.moveToFirst()) {
							final byte[] thumbnailBytes = cursor.getBlob(0);
							if (thumbnailBytes != null) {

								thumbnail = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
							}
						}
						return thumbnail;
					} finally {
						cursor.close();
					}
				}
			}
		} finally {
			cursor.close();
		}

		return null;
	}

	/**
	 * 
	 * @param context
	 * @param model
	 */
	public static void deleteSms(Context context, SmsTestModel model) {

		try {
			context.getContentResolver().delete(Uri.parse("content://sms/" + model.getId()), null, null);
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eLog("Error deleting sms" + e);
		}
	}

	/**
	 * 
	 * @param smsType
	 * @return
	 */
	public static boolean isInbox(int smsType) {
		return smsType == MESSAGE_TYPE_INBOX;
	}

	/**
	 * 
	 * @param smsType
	 * @return
	 */
	public static boolean isAllOut(int smsType) {

		switch (smsType) {
			case MESSAGE_TYPE_OUTBOX :
				// case MESSAGE_TYPE_QUEUED :
				// case MESSAGE_TYPE_FAILED :
			case MESSAGE_TYPE_SENT :
				return true;
			default :
				break;
		}
		return false;
	}

}

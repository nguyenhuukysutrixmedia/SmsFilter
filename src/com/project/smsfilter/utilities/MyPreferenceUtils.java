package com.project.smsfilter.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

public class MyPreferenceUtils {

	private static final String PREF_NAME = "SMS_FILTER";

	public static final String KEY_PREVIOUS_VERSION = "KEY_PREVIOUS_VERSION";
	public static final String KEY_FIRST_TIME = "KEY_FIRST_TIME";
	public static final String KEY_UP_TO_DATE = "KEY_UP_TO_DATE";
	public static final String KEY_NEW_SMS_NOTIFICATION = "KEY_NEW_SMS_NOTIFICATION";
	public static final String KEY_LAST_UPDATE_KEYWORK = "KEY_LAST_UPDATE_KEYWORK";

	public static final String KEY_Classifier = "Classifier";

	/**
	 * save string to preference memory
	 */
	public static void saveString(Context context, String key, String value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * read string from preference memory
	 */
	public static String getString(Context context, String key, String defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
		return settings.getString(key, defValue);
	}

	/**
	 * save long value to preference memory
	 */
	public static void saveLong(Context context, String key, long value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * read long value from preference memory
	 */
	public static long getLong(Context context, String key, long defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
		return settings.getLong(key, defValue);
	}

	/**
	 * save int value to preference memory
	 */
	public static void saveInt(Context context, String key, int value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * read int value from preference memory
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
		return settings.getInt(key, defValue);
	}

	/**
	 * save boolean value to preference memory
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (context == null)
			return;

		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * read boolean value from preference memory
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if (context == null)
			return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, 0);
		return settings.getBoolean(key, defValue);
	}

	public static boolean isInited(Context context) {

		int saveVersion = getInt(context, KEY_PREVIOUS_VERSION, 0);
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int currentVersion = pInfo.versionCode;
			return saveVersion >= currentVersion;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void setInited(Context context) {
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			int currentVersion = pInfo.versionCode;
			saveInt(context, KEY_PREVIOUS_VERSION, currentVersion);
		} catch (Exception e) {
			e.printStackTrace();
			saveInt(context, KEY_PREVIOUS_VERSION, 0);
		}
	}

	public static boolean isDataUpToDate(Context context) {
		return getBoolean(context, KEY_UP_TO_DATE, false);
	}

	public static void setDataUpToDate(Context context, boolean isUpToDate) {
		saveBoolean(context, KEY_UP_TO_DATE, isUpToDate);
	}

	public static void setNewSMSNotification(Context context, boolean isNotifi) {
		saveBoolean(context, KEY_NEW_SMS_NOTIFICATION, isNotifi);
	}

	public static boolean isNewSMSNotification(Context context) {
		return getBoolean(context, KEY_NEW_SMS_NOTIFICATION, true);
	}

	public static long getLastUpdateKeyword(Context context) {
		return getLong(context, KEY_LAST_UPDATE_KEYWORK, 0);
	}

	public static void setLastUpdateKeyword(Context context, long time) {
		saveLong(context, KEY_LAST_UPDATE_KEYWORK, time);
	}
}

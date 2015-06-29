package com.project.smsfilter.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceUtils {

	private static final String PREF_NAME = "SMS_FILTER";

	public static final String KEY_FIRST_TIME = "KEY_FIRST_TIME";
	public static final String KEY_UP_TO_DATE = "KEY_UP_TO_DATE";

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
		return getBoolean(context, KEY_FIRST_TIME, false);
	}

	public static void setInited(Context context, boolean  isInited) {
		saveBoolean(context, KEY_FIRST_TIME, isInited);
	}
	
	public static boolean isDataUpToDate(Context context) {
		return getBoolean(context, KEY_UP_TO_DATE, false);
	}

	public static void setDataUpToDate(Context context, boolean isUpToDate) {
		saveBoolean(context,KEY_UP_TO_DATE, isUpToDate);
	}
}

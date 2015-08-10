package com.project.smsfilter.utilities;

import android.util.Log;

public class MyLog {

	private static final String TAG = "SMS filter";

	/**
	 * 
	 * @param message
	 */
	public static void iLog(String message) {
		Log.i(TAG, message);
	}

	/**
	 * 
	 * @param message
	 */
	public static void eLog(String message) {
		Log.e(TAG, message);
	}
}

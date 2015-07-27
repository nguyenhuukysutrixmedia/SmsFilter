package com.project.smsfilter.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Telephony;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MyUtils {

	/**
	 * 
	 * @param context
	 * @param miliseconds
	 */
	public static void vibrate(Context context, long miliseconds) {
		try {
			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(miliseconds);

		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eLog("Vibrate error: " + e);
		}
	}

	/**
	 * show keyboard if isShow is true and hide keyboard otherwise
	 * 
	 * @param context
	 * @param view
	 * @param isShow
	 */
	public static void requestKeyBoard(Context context, View view, Boolean isShow) {
		try {

			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			// hide
			if (!isShow) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			} else {
				// show
				view.requestFocus();
				imm.showSoftInput(view, 0);
			}

		} catch (Exception e) {
		}
	}

	/**
	 * 
	 * @return to day String on device langue
	 */
	public static String parseDayString(String dateString) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd MMM", Locale.getDefault());
			return simpleDateFormat.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Checks if the device has Internet connection.
	 * 
	 * @return <code>true</code> if the phone is connected to the Internet.
	 */
	public static boolean hasConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param sPhoneNumber
	 * @return
	 */
	public static boolean validatePhoneNumber(String sPhoneNumber) {

		String patternString = "[-+\\d() ]{8,16}";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(sPhoneNumber);
		return matcher.matches();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isDefaultSMSApp(Context context) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			final String myPackageName = context.getPackageName();
			return Telephony.Sms.getDefaultSmsPackage(context).equals(myPackageName);
		}
		return false;
	}
}

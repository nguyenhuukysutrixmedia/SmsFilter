package com.project.smsfilter.background;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;

import com.project.smsfilter.utilities.MyLog;

public class SmsObserver extends ContentObserver {

	SharedPreferences trackMeData;
	private Context c;

	public SmsObserver(Handler handler, final Context c) {
		super(handler);
		trackMeData = c.getSharedPreferences("LockedSIM", 0);
		this.c = c;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);

		MyLog.iLog("SMS changed");
		
		
	}
}
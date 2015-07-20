package com.project.smsfilter.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDateHelper {
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeString(long time){
		String timeString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.getDefault());
		try {
			timeString = sdf.format(new Date(time));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeString;
	}

}

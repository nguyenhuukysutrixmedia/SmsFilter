package com.project.smsfilter.utilities;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class MyToast {

	// private final int TIME = 1500;
	private Toast mToast;
	private Context mContext;

	public MyToast(Context context) {
		mContext = context;
		mToast = new Toast(mContext);
	}

	/**
	 * Show msg as Toast, cancel previous toast if having
	 * 
	 * @param msg
	 *            message
	 */
	public void showToast(String msg) {
		mToast.cancel();
		mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		mToast.show();
	}

	/**
	 * 
	 * @param msg
	 * @param timeout
	 */
	public void showToast(String msg, int timeout) {
		showToast(msg);
		cancel(timeout);
	}

	/**
	 * Show msg as Toast, cancel previous toast if having
	 * 
	 * @param msg
	 *            message
	 */
	public void showToastCenter(String msg) {
		mToast.cancel();
		mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public void showToastCenter(String msg, int timeout) {
		showToastCenter(msg);
		cancel(timeout);
	}

	/**
	 * 
	 * @param timeout
	 */
	private void cancel(int timeout) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mToast.cancel();
			}
		}, timeout);
	}
}

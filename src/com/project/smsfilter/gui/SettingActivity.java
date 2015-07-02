package com.project.smsfilter.gui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.project.smsfilter.R;
import com.project.smsfilter.background.ApiResponseCode;
import com.project.smsfilter.background.UpdateKeywordAsyncTask;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyPreferenceUtils;
import com.project.smsfilter.utilities.MyToast;

public class SettingActivity extends Activity {

	private Switch notifiSwitch;
	private ProgressDialog mProgressDialog;
	private MyToast mToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		//
		mToast = new MyToast(this);

		//
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage("Updating.....");

		notifiSwitch = (Switch) findViewById(R.id.swichNotifi);
		notifiSwitch.setChecked(MyPreferenceUtils.isNewSMSNtification(this));
	}

	public void onNotificationClick(View v) {
		MyLog.iLog("Notification clicked: " + notifiSwitch.isChecked());
		MyPreferenceUtils.setNewSMSNtification(this, notifiSwitch.isChecked());
	}

	public void onUpdateClick(View v) {
		MyLog.iLog("Update key clicked ");

		UpdateKeywordAsyncTask updateKeywordAsyncTask = new UpdateKeywordAsyncTask(this, mProgressDialog);
		updateKeywordAsyncTask.execute();
	}

	public void updateDone(ApiResponseCode apiResponseCode) {

		if (apiResponseCode == ApiResponseCode.OK) {
			mToast.showToast("Update successful");
		} else {
			if (apiResponseCode == ApiResponseCode.NO_NETWORK) {
				mToast.showToast("Update fail. No network.");
			} else {
				mToast.showToast("Update fail. Having a unknow problem on server.");
			}
		}
	}
}

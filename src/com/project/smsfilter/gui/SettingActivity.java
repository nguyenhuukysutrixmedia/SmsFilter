package com.project.smsfilter.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.project.smsfilter.R;
import com.project.smsfilter.background.ApiResponseCode;
import com.project.smsfilter.background.UpdateKeywordAsyncTask;
import com.project.smsfilter.utilities.MyDateHelper;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyPreferenceUtils;
import com.project.smsfilter.utilities.MyToast;

public class SettingActivity extends Activity {

	private Switch notifiSwitch;
	private ProgressDialog mProgressDialog;
	private MyToast mToast;
	private AlertDialog dialog;
	private TextView tvLastUpdate;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mContext = this;
		//
		mToast = new MyToast(this);

		//
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMessage(getString(R.string.updating));
		
		//
		tvLastUpdate = (TextView)findViewById(R.id.tv_setting_activity_last_update);
		setLastUpdateText();
		
		//
		Builder builder = new Builder(this);
		dialog = builder.setCancelable(true).setTitle(getString(R.string.setting_update_sms_keyword))
				.setPositiveButton(R.string.ok, null).create();

		notifiSwitch = (Switch) findViewById(R.id.swichNotifi);
		notifiSwitch.setChecked(MyPreferenceUtils.isNewSMSNotification(this));
	}

	public void onNotificationClick(View v) {
		MyLog.iLog("Notification clicked: " + notifiSwitch.isChecked());
		MyPreferenceUtils.setNewSMSNotification(this, notifiSwitch.isChecked());
	}

	public void onUpdateClick(View v) {
		MyLog.iLog("Update key clicked ");

		UpdateKeywordAsyncTask updateKeywordAsyncTask = new UpdateKeywordAsyncTask(this, mProgressDialog);
		updateKeywordAsyncTask.execute();
	}

	public void updateDone(ApiResponseCode apiResponseCode) {

		if (apiResponseCode == ApiResponseCode.OK) {
			dialog.setMessage(getString(R.string.update_success));
			dialog.show();
			MyPreferenceUtils.setLastUpdateKeyword(mContext, System.currentTimeMillis());
			setLastUpdateText();
			// mToast.showToast(getString(R.string.update_success));
		} else {
			if (apiResponseCode == ApiResponseCode.NO_NETWORK) {
				dialog.setMessage(getString(R.string.update_fail_no_network));
				dialog.show();
				// mToast.showToast(getString(R.string.update_fail_no_network));
			} else if (apiResponseCode == ApiResponseCode.TIME_OUT) {
				dialog.setMessage(getString(R.string.update_fail_time_out));
				dialog.show();
			} else {
				dialog.setMessage(getString(R.string.update_fail_problem_on_server));
				dialog.show();
				// mToast.showToast(getString(R.string.update_fail_problem_on_server));
			}
		}
	}

	/**
	 * 
	 */
	private void setLastUpdateText() {
		tvLastUpdate.setText(getString(R.string.setting_last_update_sms_keyword)
				+ MyDateHelper.getTimeString(MyPreferenceUtils.getLastUpdateKeyword(mContext)));
	}
}

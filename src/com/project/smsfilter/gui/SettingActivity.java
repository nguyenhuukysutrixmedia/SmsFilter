package com.project.smsfilter.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.project.smsfilter.R;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyPreferenceUtils;

public class SettingActivity extends Activity {

	private Switch notifiSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		notifiSwitch = (Switch) findViewById(R.id.swichNotifi);
		notifiSwitch.setChecked(MyPreferenceUtils.isNewSMSNtification(this));
	}

	public void onNotificationClick(View v) {
		MyLog.iLog("Notification clicked: " + notifiSwitch.isChecked());
		MyPreferenceUtils.setNewSMSNtification(this, notifiSwitch.isChecked());
	}

	public void onUpdateClick(View v) {
		MyLog.iLog("Update key clicked ");
	}
}

package com.project.smsfilter.gui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.project.smsfilter.R;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.gui.adapter.SmsListviewArrayAdapter;
import com.project.smsfilter.model.SmsItemModel;
import com.project.smsfilter.model.SmsTestModel;

public class SpamActivity extends FragmentActivity implements OnClickListener, ConstantDefines {

	private Context mContext;

	private LinearLayout layoutButton;
	private LinearLayout btnDelete, btnMove;

	private SmsTestTableHelper mSmsTestTableHelper;

	private boolean isRequestDefaultAppAsked = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		requestDefaultSmsApp();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@SuppressLint("NewApi")
	private void requestDefaultSmsApp() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			final String myPackageName = getPackageName();
			if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {

				// change the default SMS app
				Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
				intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
				startActivityForResult(intent, 1);
				isRequestDefaultAppAsked = true;
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (isRequestDefaultAppAsked) {
				final String myPackageName = getPackageName();
				if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
					finish();
				}
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.action_spam_box :
				Intent intent = new Intent(mContext, SpamActivity.class);
				startActivity(intent);
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_delete :

				break;

			case R.id.btn_move :

				break;

			default :
				break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	/**
	 * 
	 * @param phoneNumber
	 */
//	private void gotoSmsDetail(String phoneNumber) {
//		Intent intent = new Intent(mContext, SMSDetailActivity.class);
//		intent.putExtra(PHONE_NUMBER, phoneNumber);
//		intent.putExtra(IS_SPAM_BOX, false);
//
//		startActivity(intent);
//	}

}

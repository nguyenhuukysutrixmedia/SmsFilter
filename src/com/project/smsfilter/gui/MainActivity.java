package com.project.smsfilter.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.project.smsfilter.R;
import com.project.smsfilter.gui.fragment.BaseFragment;
import com.project.smsfilter.gui.fragment.SmsBoxFragment;
import com.project.smsfilter.utilities.MyToast;

public class MainActivity extends FragmentActivity implements OnClickListener, ConstantDefines {

	private final int BACK_PRESS_TIME_OUT = 1500;

	private boolean isBackPressed = false;
	private boolean isSpamBox = false;
	private boolean isRequestDefaultAppAsked = false;

	private MyToast myToast;
	private Context mContext;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		myToast = new MyToast(this);

		isSpamBox = getIntent().getBooleanExtra(TYPE_BOX, false);

		initView();
	}

	private void initView() {
		if (isSpamBox) {

			BaseFragment currentFragment = new SmsBoxFragment(SmsBoxFragment.SPAM_BOX);
			replaceFragment(currentFragment, false);
			// Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_sms_box);
			// if (currentFragment != null && currentFragment instanceof SmsBoxFragment) {
			// ((SmsBoxFragment) currentFragment).setToSpamBox();
			// }
		} else {
			replaceFragment(new SmsBoxFragment(SmsBoxFragment.INBOX_BOX), false);
		}
	}

	public void replaceFragment(Fragment fragment, boolean isAnimation) {

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (isAnimation)
			transaction.setCustomAnimations(R.anim.come_from_right, R.anim.out_to_left, R.anim.come_from_left,
					R.anim.out_to_right);
		transaction.replace(R.id.fragment_container, fragment);
		transaction.addToBackStack(fragment.toString());

		transaction.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initActionBar();
		requestDefaultSmsApp();
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

	private void initActionBar() {
		if (isSpamBox) {
			setTitle(getString(R.string.title_activity_spam));
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setBackgroundDrawable(new ColorDrawable(0xAAFF0000));
		} else {
			getActionBar().setBackgroundDrawable(null);
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

		if (isSpamBox) {
			menu.findItem(R.id.action_spam_box).setVisible(false);
			menu.findItem(R.id.action_setting).setVisible(false);
		}
		menu.findItem(R.id.action_delete_sms).setVisible(false);
		menu.findItem(R.id.action_mark_as_spam).setVisible(false);
		menu.findItem(R.id.action_mark_as_not_spam).setVisible(false);
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
		Intent intent;
		switch (item.getItemId()) {
		case R.id.action_spam_box:
			intent = new Intent(mContext, MainActivity.class);
			intent.putExtra(TYPE_BOX, true);
			startActivity(intent);
			return true;
		case R.id.action_setting:
			intent = new Intent(mContext, SettingActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_delete:

			break;

		default:
			break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onBackPressed() {

		if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
			super.onBackPressed();
		} else if (isSpamBox && getSupportFragmentManager().getBackStackEntryCount() == 1) {
			// super.onBackPressed();
			finish();
		} else {

			if (!isBackPressed) {
				isBackPressed = true;
				myToast.showToast(getResources().getString(R.string.mess_press_again_to_exit), BACK_PRESS_TIME_OUT);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						isBackPressed = false;
					}
				}, BACK_PRESS_TIME_OUT);
			} else {
				finish();
				// super.onBackPressed();
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			final String myPackageName = getPackageName();
			if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
				finish();
			}
		}
		super.onConfigurationChanged(newConfig);
	}
}

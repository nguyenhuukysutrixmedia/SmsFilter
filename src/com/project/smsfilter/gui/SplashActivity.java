package com.project.smsfilter.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.project.smsfilter.R;
import com.project.smsfilter.BayesClassifier.BayesClassifierHelper;
import com.project.smsfilter.database.SmsTableHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SMSModel;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.unittest.CreateTestData;
import com.project.smsfilter.utilities.CsvHelper;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyPreferenceUtils;
import com.project.smsfilter.utilities.MySMSUtils;

public class SplashActivity extends Activity {

	private final int SPLASH_TIME_OUT = 1000;

	private long startTime;
	private SmsTestTableHelper mSmsTestTableHelper;
	private SmsTableHelper mSmsTableHelper;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mContext = this;
		startTime = System.currentTimeMillis();
		splashProcess();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void splashProcess() {
		initFirstTimeRunApp();

		loadData();
	}

	private void loadData() {

		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {

				mSmsTableHelper = new SmsTableHelper(mContext);
				mSmsTestTableHelper = new SmsTestTableHelper(mContext);

				if (!MyPreferenceUtils.isDataUpToDate(mContext)) {
					ArrayList<SMSModel> listSmsModelsCSV = CsvHelper.parseSmsAssetData(mContext);
					for (SMSModel smsModel : listSmsModelsCSV) {
						mSmsTableHelper.insert(smsModel);
					}

					MyPreferenceUtils.setDataUpToDate(mContext, true);
				}

				// mSmsTestTableHelper.deleteAll();
				ArrayList<SmsTestModel> listSmsModels = MySMSUtils.readSMSInbox(SplashActivity.this);
				if (listSmsModels.size() <= 0)
					CreateTestData.createTestSms(mContext);
				for (SmsTestModel smsModel : listSmsModels) {
					// MyLog.iLog("SMS:: " + smsModel.toString());
					mSmsTestTableHelper.insert(smsModel);
				}

				BayesClassifierHelper bayesClassifierHelper = new BayesClassifierHelper(mContext);
				bayesClassifierHelper.analyzeSms();

				return null;
			}

			protected void onPostExecute(String result) {
				loadDataDone();
			};
		}.execute("");
	}

	private void loadDataDone() {
		final long deltaTime = System.currentTimeMillis() - startTime;
		long delayMillis = 0;
		if (deltaTime < SPLASH_TIME_OUT) {
			delayMillis = SPLASH_TIME_OUT - deltaTime;
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				startMainScreen();
			}
		}, delayMillis);
	}

	private void startMainScreen() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
		MyLog.iLog("Splash time: " + (System.currentTimeMillis() - startTime));
	}

	/**
	 * 
	 */
	private void initFirstTimeRunApp() {
		if (!MyPreferenceUtils.isInited(mContext)) {
			CsvHelper.copyTemplateSmsData(mContext);
			MyPreferenceUtils.setInited(mContext, true);
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
	}
}

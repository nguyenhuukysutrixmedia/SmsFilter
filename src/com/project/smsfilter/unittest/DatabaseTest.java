package com.project.smsfilter.unittest;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.project.smsfilter.BayesClassifier.BayesClassifierHelper;
import com.project.smsfilter.database.SmsTableHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.gui.SplashActivity;
import com.project.smsfilter.model.SMSModel;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.ReadFileHelper;
import com.project.smsfilter.utilities.MyPreferenceUtils;

public class DatabaseTest extends AndroidTestCase {

	private SmsTableHelper mSmsTableHelper;
	// private SmsTestTableHelper mSmsTestTableHelper;

	public void setUp() {
		mSmsTableHelper = new SmsTableHelper(mContext);
		// mSmsTestTableHelper = new SmsTestTableHelper(mContext);
		ReadFileHelper.copyTemplateSmsData(mContext);
		// if (!MyPreferenceUtils.isDataUpToDate(mContext)) {
		mSmsTableHelper.deleteAll();
		ArrayList<SMSModel> listSmsModelsCSV = ReadFileHelper.parseSmsAssetData(mContext);
		for (SMSModel smsModel : listSmsModelsCSV) {
			mSmsTableHelper.insert(smsModel);
		}

		MyPreferenceUtils.setDataUpToDate(mContext, true);
		// }

		// mSmsTestTableHelper.deleteAll();
		// ArrayList<SmsTestModel> listSmsModels = MySMSUtils.readSMSInbox(SplashActivity.this);
		// for (SmsTestModel smsModel : listSmsModels) {
		// mSmsTestTableHelper.insert(smsModel);
		// }
		//
		// BayesClassifierHelper bayesClassifierHelper = new BayesClassifierHelper(mContext);
		// bayesClassifierHelper.analyzeSms();

	}

	// public void testGetAllData() {
	//
	// ArrayList<SMSModel> listSMS = mSmsTableHelper.getAll();
	// ArrayList<SMSModel> listSpamSMS = mSmsTableHelper.getListSpam();
	// ArrayList<SMSModel> listNotSpamSMS = mSmsTableHelper.getListNotSpam();
	//
	// assertTrue(listSMS.size() > 0);
	// assertTrue(listSpamSMS.size() > 0);
	// assertTrue(listNotSpamSMS.size() > 0);
	// assertTrue(listSMS.size() == listSpamSMS.size() + listNotSpamSMS.size());
	// }

	public void testSmsTestTable() {

		ArrayList<SMSModel> listSMS = mSmsTableHelper.getAll();
		ArrayList<SMSModel> listSpamSMS = mSmsTableHelper.getListSpamOrderDate();
		ArrayList<SMSModel> listNotSpamSMS = mSmsTableHelper.getListNotSpamOrderDate();

		assertTrue(listSpamSMS.size() > 0);
		assertTrue(listNotSpamSMS.size() > 0);

		assertTrue(listSpamSMS.get(0).getCreateTime().getTime() >= listSpamSMS.get(1).getCreateTime().getTime());
		assertTrue(listSpamSMS.get(1).getCreateTime().getTime() >= listSpamSMS.get(2).getCreateTime().getTime());
		assertTrue(listSpamSMS.get(2).getCreateTime().getTime() >= listSpamSMS.get(3).getCreateTime().getTime());
		assertTrue(listSpamSMS.get(3).getCreateTime().getTime() >= listSpamSMS.get(4).getCreateTime().getTime());
		assertTrue(listSpamSMS.get(4).getCreateTime().getTime() >= listSpamSMS.get(5).getCreateTime().getTime());

		assertTrue(listNotSpamSMS.size() > 0);

		SMSModel model = new SMSModel();
		model.setContent("hehe");
		model.setId(1);
		assertTrue(mSmsTableHelper.insert(model) > 0);
		assertTrue(mSmsTableHelper.insert(model) <= 0);
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}

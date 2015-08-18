package com.project.smsfilter.unittest;

import java.util.ArrayList;

import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.MyLog;

import android.test.AndroidTestCase;

public class MySmsTest extends AndroidTestCase {

	public void setUp() {

	}

	public void testGetSmsData() {

		MySMSUtils.readAllSMSByThread(mContext);
	}

	public void testSmsTestTable() {

		ArrayList<SmsTestModel> listSmsTest = MySMSUtils.readAllSMS(mContext);
		MyLog.iLog("SMS: size: " + listSmsTest.size());
		for (SmsTestModel smsTestModel : listSmsTest) {
			// MyLog.iLog("SMS: " + smsTestModel);
		}
		// assertEquals(listSmsTest.size(), );

	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}

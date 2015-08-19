package com.project.smsfilter.unittest;

import android.test.AndroidTestCase;

import com.project.smsfilter.sms.MySMSUtils;

public class LinhTinhTest extends AndroidTestCase {

	public void setUp() {

	}

	public void test() {

		assertNotNull(MySMSUtils.fetchThumbnail(mContext, "+841262955365"));
		assertNotNull(MySMSUtils.fetchThumbnail(mContext, "Mi.mobi Keke"));
		assertNull(MySMSUtils.fetchThumbnail(mContext, "01287524253"));

	}

	public void tearDown() throws Exception {
		super.tearDown();
	}
}

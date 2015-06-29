package com.project.smsfilter.unittest;

import java.util.ArrayList;

import android.content.Context;

import com.project.smsfilter.database.SmsTableHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.model.SMSModel;
import com.project.smsfilter.model.SmsTestModel;

public class CreateTestData {

	public static void createTestSms(Context context) {

		int N = 5;
		SmsTestTableHelper smsTestTableHelper = new SmsTestTableHelper(context);
		SmsTableHelper smsTableHelper = new SmsTableHelper(context);

		ArrayList<SMSModel> lstSpam = smsTableHelper.getByQuery();
		int countSpam = 0;
		int countNotSpam = 0;
		for (SMSModel smsModel : lstSpam) {

			if (smsModel.isSpam() && countSpam < N) {
				SmsTestModel smsTestModel = new SmsTestModel();
				smsTestModel.setContent(smsModel.getContent());
				smsTestModel.setId(smsModel.getId());
				smsTestModel.setPhoneNumber(smsModel.getPhoneNumber());
				smsTestModel.setType(smsModel.getType());

				smsTestTableHelper.insert(smsTestModel);
				countSpam++;
			} else if (countNotSpam < N) {

				SmsTestModel smsTestModel = new SmsTestModel();
				smsTestModel.setContent(smsModel.getContent());
				smsTestModel.setId(smsModel.getId());
				smsTestModel.setPhoneNumber(smsModel.getPhoneNumber());
				smsTestModel.setType(smsModel.getType());
				smsTestTableHelper.insert(smsTestModel);
				countNotSpam++;
			}
			if (countNotSpam >= N && countSpam >= N)
				break;
		}

	}

}

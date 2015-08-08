package com.project.smsfilter.gui;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;

import com.project.smsfilter.R;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.gui.adapter.SmsDetailListviewArrayAdapter;
import com.project.smsfilter.model.SmsItemModel;
import com.project.smsfilter.model.SmsTestModel;

public class SMSDetailActivity extends BaseActivity {

	private String mPhoneNumber;
	private boolean isSpamBox;
	private ArrayList<SmsItemModel> listItemModels;

	private SmsTestTableHelper mSmsTestTableHelper;
	
	
	//
	private ListView lvSms;
	private SmsDetailListviewArrayAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smsdetail);

		mPhoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
		isSpamBox = getIntent().getBooleanExtra(IS_SPAM_BOX, false);

		initView();

		loadSmsData();
	}

	private void initView() {

		//
		lvSms = (ListView)findViewById(R.id.lv_sms);
		
		
	}

	private void loadSmsData() {

		mSmsTestTableHelper = new SmsTestTableHelper(mContext);
		ArrayList<SmsTestModel> listSms = mSmsTestTableHelper.getListNotSpamByPhoneNumber(mPhoneNumber);
		
		listItemModels = new ArrayList<SmsItemModel>();
		for (SmsTestModel smsTestModel : listSms) {
			SmsItemModel smsItemModel = new SmsItemModel();
			smsItemModel.setChecked(false);
			smsItemModel.setSmsModel(smsTestModel);
			
			listItemModels.add(smsItemModel);
		}
		
		mAdapter = new SmsDetailListviewArrayAdapter(mContext, 0, listItemModels);
		lvSms.setAdapter(mAdapter);
	}

}

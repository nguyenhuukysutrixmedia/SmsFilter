package com.project.smsfilter.gui.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.project.smsfilter.R;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.gui.ConstantDefines;
import com.project.smsfilter.gui.adapter.SmsListviewArrayAdapter;
import com.project.smsfilter.model.SmsItemModel;
import com.project.smsfilter.model.SmsTestModel;

public class SmsBoxFragment extends BaseFragment implements OnClickListener, OnItemClickListener, ConstantDefines {

	public static final int INBOX_BOX = 1;
	public static final int SPAM_BOX = 2;

	private Context mContext;
	private int mTypeBox = INBOX_BOX;
	private ListView mListViewSms;
	private SmsListviewArrayAdapter mSmsArrayAdapter;
	private ArrayList<SmsItemModel> listItemModels;

	private SmsTestTableHelper mSmsTestTableHelper;

	public SmsBoxFragment() {
		super();
		mTypeBox = INBOX_BOX;
	}

	public SmsBoxFragment(int typeBox) {
		mTypeBox = typeBox;
	}

	public void setTypeBox(int typeBox) {
		mTypeBox = typeBox;
	}

	public void setToSpamBox() {
		mTypeBox = SPAM_BOX;
		loadListView();
	}

	@Override
	public void onResume() {
		if (isNeedReloadData)
			loadListView();
		super.onResume();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sms_box, container, false);

		setHasOptionsMenu(true);
		mContext = getActivity();

		initView(v);

		return v;
	}

	private void initView(View v) {

		mSmsTestTableHelper = new SmsTestTableHelper(mContext);

		//
		mListViewSms = (ListView) v.findViewById(R.id.lv_sms);
		mListViewSms.setOnItemClickListener(this);
		loadListView();

		//
		mIncomingSmsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				// MyLog.iLog("New SMS - SmsBoxFragment: " + intent.getStringExtra("phoneNumber") + "-"
				// + intent.getStringExtra("message"));

				try {
					loadListView();
				} catch (Exception e) {
				}
			}
		};
	}

	/**
	 * 
	 */
	private void loadListView() {

		isNeedReloadData = false;
		ArrayList<SmsTestModel> listSMS = new ArrayList<SmsTestModel>();
		switch (mTypeBox) {
			case INBOX_BOX :
				listSMS = mSmsTestTableHelper.getListNotSpam();
				break;

			case SPAM_BOX :
				listSMS = mSmsTestTableHelper.getListSpam();
				break;
		}

		listItemModels = new ArrayList<SmsItemModel>();
		for (int i = 0; i < listSMS.size(); i++) {
			SmsItemModel smsItemModel = new SmsItemModel();
			smsItemModel.setSmsModel(listSMS.get(i));
			listItemModels.add(smsItemModel);
		}
		mSmsArrayAdapter = new SmsListviewArrayAdapter(mContext, 0, listItemModels);

		mListViewSms.setAdapter(mSmsArrayAdapter);
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
	public void onPrepareOptionsMenu(Menu menu) {

		// MenuItem doneItem = (MenuItem) menu.findItem(R.id.action_done);
		// if (mSmsArrayAdapter != null && mSmsArrayAdapter.isEditAble()) {
		// doneItem.setVisible(true);
		// } else {
		// doneItem.setVisible(false);
		// }
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// gotoSmsDetail(listItemModels.get(position).getSmsModel().getPhoneNumber());
		String phoneNumber = listItemModels.get(position).getSmsModel().getPhoneNumber();
		String phoneName = listItemModels.get(position).getSmsModel().getPhoneName();
		startFragment(new SmsDetailFragment(phoneNumber, phoneName, mTypeBox == SPAM_BOX));
		isNeedReloadData = true;
	}
}

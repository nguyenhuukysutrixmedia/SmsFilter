package com.project.smsfilter.gui.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.project.smsfilter.R;
import com.project.smsfilter.database.SmsTableHelper;
import com.project.smsfilter.database.SmsTestTableHelper;
import com.project.smsfilter.gui.ConstantDefines;
import com.project.smsfilter.gui.adapter.SmsDetailListviewArrayAdapter;
import com.project.smsfilter.model.SMSModel;
import com.project.smsfilter.model.SmsItemModel;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.MyLog;
import com.project.smsfilter.utilities.MyUtils;

public class SmsDetailFragment extends BaseFragment
		implements
			OnClickListener,
			OnItemLongClickListener,
			OnItemClickListener,
			ConstantDefines {

	private String mPhoneNumber;
	private String mPhoneName;
	private boolean isSpamBox;
	private ArrayList<SmsItemModel> listItemModels;

	private SmsTestTableHelper mSmsTestTableHelper;
	private SmsTableHelper mSmsTableHelper;
	private int optionSeleted = OPTION_NONE;

	//
	private ListView lvSms;
	private SmsDetailListviewArrayAdapter mAdapter;

	private ActionMode mActionMode;

	public SmsDetailFragment(String phoneNumber, String phoneName, boolean isSpamBox) {
		super();
		mPhoneNumber = phoneNumber;
		mPhoneName = phoneName;
		this.isSpamBox = isSpamBox;
	}

	public SmsDetailFragment() {
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		initActionBar();
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void initActionBar() {

		if (mPhoneName != null && !mPhoneName.isEmpty()) {
			getActivity().getActionBar().setTitle(mPhoneName);
			getActivity().getActionBar().setSubtitle(mPhoneNumber);
		} else {
			getActivity().getActionBar().setTitle(mPhoneNumber);
		}

		Bitmap bm = MySMSUtils.fetchThumbnail(mContext, mPhoneNumber);
		if (bm != null) {
			getActivity().getActionBar().setIcon(new BitmapDrawable(bm));
		} else {
			getActivity().getActionBar().setIcon(R.drawable.ic_contact_picture);
		}
		getActivity().getActionBar().setHomeButtonEnabled(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onDestroy() {
		restoreActionbar();
		super.onDestroy();
		if (this.mActionMode != null) {
			this.mActionMode.finish();
		}
	}

	private void restoreActionbar() {
		getActivity().getActionBar().setSubtitle(null);
		if (isSpamBox) {
			getActivity().getActionBar().setTitle(getString(R.string.title_activity_spam));
			getActivity().getActionBar().setIcon(R.drawable.app_icon);
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			getActivity().getActionBar().setHomeButtonEnabled(true);
		} else {
			getActivity().getActionBar().setTitle(getString(R.string.app_name));
			getActivity().getActionBar().setIcon(R.drawable.app_icon);
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
			getActivity().getActionBar().setHomeButtonEnabled(false);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_sms_detail, container, false);

		setHasOptionsMenu(true);
		mContext = getActivity();

		initView(v);

		return v;
	}

	private void initView(View v) {

		mSmsTableHelper = new SmsTableHelper(mContext);

		//
		lvSms = (ListView) v.findViewById(R.id.lv_sms);
		lvSms.setOnItemClickListener(this);
		lvSms.setOnItemLongClickListener(this);
		loadListView();

		//
		// btnDelete = (LinearLayout) v.findViewById(R.id.btn_delete);
		// btnMove = (LinearLayout) v.findViewById(R.id.btn_move);
		// btnDelete.setOnClickListener(this);
		// btnMove.setOnClickListener(this);

		//
		// layoutButton = (LinearLayout) v.findViewById(R.id.layout_button);

		//
		mIncomingSmsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				MyLog.iLog("New SMS - SmsDetailFragment: " + intent.getStringExtra("phoneNumber") + "-"
						+ intent.getStringExtra("message"));
				try {
					loadListView();
				} catch (Exception e) {
				}

			}
		};
	}

	private void loadListView() {

		mSmsTestTableHelper = new SmsTestTableHelper(mContext);

		ArrayList<SmsTestModel> listSms;
		if (isSpamBox) {
			listSms = mSmsTestTableHelper.getListSpamByPhoneNumber(mPhoneNumber);
		} else {
			listSms = mSmsTestTableHelper.getListNotSpamByPhoneNumber(mPhoneNumber);
		}

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_delete :

				break;

			default :
				break;
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

		menu.findItem(R.id.action_spam_box).setVisible(false);
		menu.findItem(R.id.action_setting).setVisible(false);
		menu.findItem(R.id.action_delete_sms).setVisible(true);
		if (isSpamBox) {
			menu.findItem(R.id.action_mark_as_not_spam).setVisible(true);
		} else {
			menu.findItem(R.id.action_mark_as_spam).setVisible(true);
		}

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items

		MyLog.iLog("onOptionsItemSelected: " + item);

		switch (item.getItemId()) {
			case android.R.id.home :
				getActivity().onBackPressed();
				return true;

			case R.id.action_delete_sms :
				optionSeleted = OPTION_DELETE;
				makeDeleteSms();
				return true;

			case R.id.action_mark_as_not_spam :
				optionSeleted = OPTION_MARK_NOT_SPAM;
				makeMarkNotSpamSms();
				return true;

			case R.id.action_mark_as_spam :
				optionSeleted = OPTION_MARK_SPAM;
				makeMarkSpamSms();
				return true;

			default :
				return super.onOptionsItemSelected(item);
		}
	}

	private void makeDeleteSms() {
		makeListviewEditable();
		mActionMode = getActivity().startActionMode(mActionModeCallback);
	}

	private void makeMarkSpamSms() {
		makeListviewEditable();
		mActionMode = getActivity().startActionMode(mActionModeCallback);
	}

	private void makeMarkNotSpamSms() {
		makeListviewEditable();
		mActionMode = getActivity().startActionMode(mActionModeCallback);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MyLog.iLog("onItemClick: " + position);

		if (mActionMode != null) {
			mAdapter.toggleSelected(position);
			updateSelected();
		}
	}

	private void updateSelected() {
		mActionMode.setTitle(mAdapter.getNumberItemSelected() + " selected");
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		MyLog.iLog("onItemLongClick: " + position);
		if (mActionMode != null) {
			return false;
		}
		MyUtils.vibrate(mContext, 50);
		makeListviewEditable();
		// Start the CAB using the ActionMode.Callback defined above
		mActionMode = getActivity().startActionMode(mActionModeCallback);
		mAdapter.toggleSelected(position);
		view.setSelected(true);
		updateSelected();
		return true;
	}

	/**
	 * 
	 */
	private void makeListviewEditable() {
		mAdapter.setEditAble(true);
	}

	private void listviewEditDone() {
		mAdapter.setEditAble(false);
		loadListView();
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.modify_menu, menu);
			return true;
		}

		// Called each time the action mode is shown. Always called after onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

			menu.findItem(R.id.action_all).setVisible(true);
			menu.findItem(R.id.action_delete).setVisible(false);
			menu.findItem(R.id.action_spam).setVisible(false);
			menu.findItem(R.id.action_not_spam).setVisible(false);

			switch (optionSeleted) {

				case OPTION_DELETE :
					menu.findItem(R.id.action_delete).setVisible(true);
					break;

				case OPTION_MARK_SPAM :
					menu.findItem(R.id.action_spam).setVisible(true);
					break;

				case OPTION_MARK_NOT_SPAM :
					menu.findItem(R.id.action_not_spam).setVisible(true);
					break;

				case OPTION_NONE :
					menu.findItem(R.id.action_delete).setVisible(true);
					if (isSpamBox) {
						menu.findItem(R.id.action_not_spam).setVisible(true);
					} else {
						menu.findItem(R.id.action_spam).setVisible(true);
					}
					break;

				default :
					break;
			}

			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {

				case R.id.action_all :
					mAdapter.selecteAll();
					updateSelected();
					return true;

				case R.id.action_delete :
					deleteSms();
					return true;

				case R.id.action_spam :
					makeSpam();
					return true;

				case R.id.action_not_spam :
					makeNotSpam();
					return true;
				default :
					return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			listviewEditDone();
			optionSeleted = OPTION_NONE;
		}

	};

	private void deleteSms() {
		ArrayList<SmsItemModel> listSms = mAdapter.getSelectedObjects();

		if (listSms.size() > 0) {

			for (SmsItemModel smsItemModel : listSms) {
				mSmsTestTableHelper.delete(smsItemModel.getSmsModel().getUid() + "");
				MySMSUtils.deleteSms(mContext, smsItemModel.getSmsModel());
			}

			loadListView();

			mActionMode.finish(); // Action picked, so close the CAB
		} else {
			mMyToast.showToast("Please seletect SMS want to delete first");
		}
	}

	private void makeSpam() {
		ArrayList<SmsItemModel> listSms = mAdapter.getSelectedObjects();

		if (listSms.size() > 0) {

			for (SmsItemModel smsItemModel : listSms) {
				smsItemModel.getSmsModel().setSpam(true);
				mSmsTestTableHelper.update(smsItemModel.getSmsModel());
				mSmsTableHelper.insert(new SMSModel(smsItemModel.getSmsModel()));
			}

			loadListView();

			mActionMode.finish(); // Action picked, so close the CAB
		} else {
			mMyToast.showToast("Please seletect SMS want to make spam first");
		}
	}

	private void makeNotSpam() {

		ArrayList<SmsItemModel> listSms = mAdapter.getSelectedObjects();

		if (listSms.size() > 0) {

			for (SmsItemModel smsItemModel : listSms) {
				smsItemModel.getSmsModel().setSpam(false);
				mSmsTestTableHelper.update(smsItemModel.getSmsModel());
				mSmsTableHelper.insert(new SMSModel(smsItemModel.getSmsModel()));
			}

			loadListView();

			mActionMode.finish(); // Action picked, so close the CAB
		} else {
			mMyToast.showToast("Please seletect SMS want to make not spam first");
		}
	}
}

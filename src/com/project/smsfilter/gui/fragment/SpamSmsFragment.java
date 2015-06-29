package com.project.smsfilter.gui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
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
import com.project.smsfilter.utilities.MyLog;

public class SpamSmsFragment extends Fragment implements OnClickListener, OnItemLongClickListener, OnItemClickListener {

	public static final String ARG_OBJECT = "object";
	public static final int INBOX_BOX = 1;
	public static final int SPAM_BOX = 2;

	private Context mContext;
	private int mTypeBox;
	private ListView mListViewSms;
	private SmsListviewArrayAdapter mSmsArrayAdapter;
	private ArrayList<SmsItemModel> listItemModels;

	private LinearLayout layoutButton;
	private LinearLayout btnDelete, btnMove;

	private SmsTestTableHelper mSmsTestTableHelper;
	private ActionMode mActionMode;

	public SpamSmsFragment(int typeBox) {
		mTypeBox = typeBox;
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
		loadData();
		initView(v);

		return v;
	}

	private void initView(View v) {

		//
		mListViewSms = (ListView) v.findViewById(R.id.lv_sms);
		mListViewSms.setAdapter(mSmsArrayAdapter);
		mListViewSms.setOnItemClickListener(this);
		mListViewSms.setOnItemLongClickListener(this);
//		mListViewSms.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		mListViewSms.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				// Here you can do something when items are selected/de-selected,
				// such as update the title in the CAB
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// Respond to clicks on the actions in the CAB
				switch (item.getItemId()) {
					case R.id.action_delete :
						mode.finish(); // Action picked, so close the CAB
						return true;
					default :
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// Inflate the menu for the CAB
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.modify_menu, menu);
				mSmsArrayAdapter.setEditAble(true);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// Here you can make any necessary updates to the activity when
				// the CAB is removed. By default, selected items are deselected/unchecked.
				mSmsArrayAdapter.setEditAble(false);
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// Here you can perform updates to the CAB due to
				// an invalidate() request
				return false;
			}
		});

		//
		btnDelete = (LinearLayout) v.findViewById(R.id.btn_delete);
		btnMove = (LinearLayout) v.findViewById(R.id.btn_move);
		btnDelete.setOnClickListener(this);
		btnMove.setOnClickListener(this);

		//
		layoutButton = (LinearLayout) v.findViewById(R.id.layout_button);

	}

	private void loadData() {

		mSmsTestTableHelper = new SmsTestTableHelper(mContext);
		loadSmsData();

		switch (mTypeBox) {
			case INBOX_BOX :

				break;
			case SPAM_BOX :

				break;

			default :
				break;
		}
	}
	private void loadSmsData() {

		ArrayList<SmsTestModel> listSMS;
		switch (mTypeBox) {
			case INBOX_BOX :
				listSMS = mSmsTestTableHelper.getListNotSpam();
				break;

			case SPAM_BOX :
				listSMS = mSmsTestTableHelper.getListSpam();
				break;

			default :
				listSMS = mSmsTestTableHelper.getAll();
				break;
		}

		listItemModels = new ArrayList<SmsItemModel>();
		for (int i = 0; i < listSMS.size(); i++) {
			SmsItemModel smsItemModel = new SmsItemModel();
			smsItemModel.setSmsModel(listSMS.get(i));
			listItemModels.add(smsItemModel);
		}

		mSmsArrayAdapter = new SmsListviewArrayAdapter(mContext, 0, listItemModels);
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

		super.onPrepareOptionsMenu(menu);
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu items for use in the action bar
		inflater.inflate(R.menu.main_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items

		MyLog.iLog("onOptionsItemSelected: " + item);

		switch (item.getItemId()) {

			default :
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		MyLog.iLog("onItemLongClick: " + position);
		if (mActionMode != null) {
			return false;
		}

		// Start the CAB using the ActionMode.Callback defined above
		mActionMode = getActivity().startActionMode(mActionModeCallback);
		view.setSelected(true);
		return true;
		// makeListviewEditable();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		MyLog.iLog("onItemClick: " + position);
		mSmsArrayAdapter.toggleSelected(position);
	}

	/**
	 * 
	 */
	private void makeListviewEditable() {

		mSmsArrayAdapter.setEditAble(true);
		getActivity().invalidateOptionsMenu();
		layoutButton.setVisibility(View.VISIBLE);

	}

	private void listviewEditDone() {
		mSmsArrayAdapter.setEditAble(false);
		getActivity().invalidateOptionsMenu();
		layoutButton.setVisibility(View.GONE);
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
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
//				case R.id.action_done :
//					mode.finish(); // Action picked, so close the CAB
//					return true;
				default :
					return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};
}

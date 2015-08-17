package com.project.smsfilter.gui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.project.smsfilter.R;
import com.project.smsfilter.background.NewSmsTask;
import com.project.smsfilter.gui.MainActivity;
import com.project.smsfilter.sms.Defines;
import com.project.smsfilter.utilities.MyToast;
import com.project.smsfilter.utilities.MyUtils;

public class BaseFragment extends Fragment implements OnClickListener {

	protected Context mContext;
	protected MyToast mMyToast;
	protected BroadcastReceiver mIncomingSmsReceiver;
	protected boolean isNeedReloadData;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = getActivity();
		mMyToast = new MyToast(getActivity());
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		// MyDialog.resizeDialog(mContext);

		getActivity().registerReceiver(mIncomingSmsReceiver, new IntentFilter(Defines.NEW_SMS_RECEIVER_TAG));
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		getActivity().unregisterReceiver(this.mIncomingSmsReceiver);
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(getActivity(), cls);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.come_from_right, R.anim.out_to_left);
	}

	/**
	 * 
	 * @param fragment
	 * @param isAnimation
	 */
	public void replaceFragment(Fragment fragment, boolean isAnimation) {

		((MainActivity) getActivity()).replaceFragment(fragment, isAnimation);

		// FragmentManager fm = getActivity().getSupportFragmentManager();
		// FragmentTransaction transaction = fm.beginTransaction();
		// if (isAnimation)
		// transaction.setCustomAnimations(R.anim.come_from_right, R.anim.out_to_left, R.anim.come_from_left,
		// R.anim.out_to_right);
		// transaction.replace(R.id.fragment_container, fragment);
		// transaction.addToBackStack(fragment.toString());
		//
		// transaction.commit();
	}

	public void startFragment(Fragment fragment) {
		replaceFragment(fragment, true);
	}

	public void clearTopFragment() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		MyUtils.requestKeyBoard(getActivity(), getView(), false);
		super.onStop();
	}

	/**
	 * 
	 */
	public void setText(final EditText edt, String text) {
		edt.setText(text);
		edt.setSelection(edt.getText().length());
		edt.setSelection(0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// MyLog.iGeneral("BaseFragment-onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// MyLog.iGeneral("BaseFragment-onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
	}

	public void setNeedReloadData(boolean isNeedReloadData) {
		this.isNeedReloadData = isNeedReloadData;
	}
	
	
}

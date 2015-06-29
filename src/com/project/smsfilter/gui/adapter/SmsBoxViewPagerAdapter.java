package com.project.smsfilter.gui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.smsfilter.gui.fragment.SmsBoxFragment;

public class SmsBoxViewPagerAdapter extends FragmentStatePagerAdapter {
	
	public SmsBoxViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		
		Fragment fragment = new SmsBoxFragment(i == 0? SmsBoxFragment.INBOX_BOX: SmsBoxFragment.SPAM_BOX);
		// Bundle args = new Bundle();
		// // Our object is just an integer :-P
		// args.putInt(SmsBoxFragment.ARG_OBJECT, i + 1);
		// fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	// @Override
	// public CharSequence getPageTitle(int position) {
	// return "OBJECT " + (position + 1);
	// }
}

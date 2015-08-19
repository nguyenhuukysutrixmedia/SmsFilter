package com.project.smsfilter.gui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.smsfilter.R;
import com.project.smsfilter.model.SmsItemModel;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.MyConstants;
import com.project.smsfilter.utilities.MyLog;

public class SmsListviewArrayAdapter extends ArrayAdapter<SmsItemModel> {

	private List<SmsItemModel> mObjects;
	private Context mContext;
	private boolean isEditAble;
	private boolean blockedMutilClick;

	public SmsListviewArrayAdapter(@NonNull Context context, int resource, @NonNull List<SmsItemModel> objects) {
		super(context, R.layout.sms_item, objects);

		if (objects == null)
			objects = new ArrayList<SmsItemModel>();
		mObjects = objects;
		mContext = context;
		isEditAble = false;
		blockedMutilClick = false;
	}

	// @SuppressLint("NewApi")
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		final ViewHolder viewHolder;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.sms_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.layoutRootItem = (LinearLayout) v.findViewById(R.id.layout_root_item);
			viewHolder.cbSelection = (CheckBox) v.findViewById(R.id.cb_selection);
			viewHolder.tvContent = (TextView) v.findViewById(R.id.tv_content);
			viewHolder.tvDate = (TextView) v.findViewById(R.id.tv_date);
			viewHolder.tvPhoneNumber = (TextView) v.findViewById(R.id.tv_phone_number);
			viewHolder.imgContactIcon = (ImageView) v.findViewById(R.id.img_contact_icon);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		// set data detail
		final SmsItemModel itemModel = mObjects.get(position);
		if (itemModel != null) {

			final SmsTestModel smsModel = itemModel.getSmsModel();
			if (isEditAble) {
				viewHolder.cbSelection.setVisibility(View.VISIBLE);
				viewHolder.cbSelection.setChecked(itemModel.isChecked());
			} else {
				viewHolder.cbSelection.setVisibility(View.GONE);
			}

			viewHolder.tvContent.setText(smsModel.getContent());
			viewHolder.tvDate.setText(smsModel.getCreateTimeString(MyConstants.DATE_SHORT_FORMAT));

			String phone = smsModel.getPhoneName();
			if (phone == null || phone.isEmpty())
				phone = smsModel.getPhoneNumber();
			viewHolder.tvPhoneNumber.setText(phone);

			Bitmap bm = MySMSUtils.fetchThumbnail(mContext, smsModel.getPhoneNumber());
			MyLog.iLog("getPhoneNumber: " + smsModel.getPhoneNumber() );
			if (bm != null) {
				MyLog.iLog("Bitmap: " + bm );
				viewHolder.imgContactIcon.setImageBitmap(bm);
			} else {
				viewHolder.imgContactIcon.setImageResource(R.drawable.ic_contact_picture);
			}
		}

		return v;
	}

	class ViewHolder {

		LinearLayout layoutRootItem;
		ImageView imgContactIcon;
		TextView tvPhoneNumber;
		TextView tvContent;
		TextView tvDate;
		CheckBox cbSelection;
	}

	@Override
	public void add(SmsItemModel object) {
		mObjects.add(object);
		notifyDataSetChanged();
		super.add(object);
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public SmsItemModel getItem(int position) {
		if (position >= mObjects.size())
			return null;
		return mObjects.get(position);
	}

	@Override
	public void remove(SmsItemModel object) {
		mObjects.remove(object);
		notifyDataSetChanged();
		super.remove(object);
	}

	public void remove(int smsId) {
		for (SmsItemModel item : mObjects) {
			if (item.getSmsModel().getUid() == smsId) {
				mObjects.remove(item);
				notifyDataSetChanged();
			}
		}
	}

	public boolean isEditAble() {
		return isEditAble;
	}

	public void setEditAble(boolean isEditAble) {

		this.isEditAble = isEditAble;
		if (!this.isEditAble)
			for (SmsItemModel item : mObjects) {
				item.setChecked(false);
			}
		notifyDataSetChanged();
	}

	public void toggleSelected(int position) {

		if (isEditAble && mObjects.get(position) != null) {
			mObjects.get(position).setChecked(!mObjects.get(position).isChecked());
			notifyDataSetChanged();
		}
	}

}

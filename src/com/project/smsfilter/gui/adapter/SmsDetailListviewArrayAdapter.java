package com.project.smsfilter.gui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.smsfilter.R;
import com.project.smsfilter.database.DatabaseDefinition.SmsDefine;
import com.project.smsfilter.model.SmsItemModel;
import com.project.smsfilter.model.SmsTestModel;
import com.project.smsfilter.sms.Defines;
import com.project.smsfilter.sms.Defines.SmsType;
import com.project.smsfilter.sms.Defines.SmsUri;
import com.project.smsfilter.sms.MySMSUtils;
import com.project.smsfilter.utilities.MyConstants;

public class SmsDetailListviewArrayAdapter extends ArrayAdapter<SmsItemModel> implements SmsType {

	private List<SmsItemModel> mObjects;
	private Context mContext;
	private boolean isEditAble;

	public SmsDetailListviewArrayAdapter(@NonNull Context context, int resource, @NonNull List<SmsItemModel> objects) {
		super(context, R.layout.sms_item, objects);

		if (objects == null)
			objects = new ArrayList<SmsItemModel>();
		mObjects = objects;
		mContext = context;
		isEditAble = false;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		final ViewHolder viewHolder;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.sms_detail_item, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.layoutRootItem = (LinearLayout) v.findViewById(R.id.layout_root_item);
			// viewHolder.cbSelection = (CheckBox) v.findViewById(R.id.cb_selection);
			viewHolder.tvContent = (TextView) v.findViewById(R.id.tv_content);
			viewHolder.tvDate = (TextView) v.findViewById(R.id.tv_date);
			// viewHolder.layoutCheckBox = (FrameLayout) v.findViewById(R.id.layout_checkbox);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		// set data detail
		final SmsItemModel itemModel = mObjects.get(position);
		if (itemModel != null) {

			SmsTestModel smsModel = itemModel.getSmsModel();
			if (isEditAble) {
				if (itemModel.isChecked()) {
					viewHolder.layoutRootItem.setBackgroundResource(R.drawable.shape_sms_detail_selected);
				} else {
					if (MySMSUtils.isInbox(itemModel.getSmsModel().getType())) {
						viewHolder.layoutRootItem.setBackgroundResource(R.drawable.shape_sms_detail_not_selected_inbox);
					} else {
						viewHolder.layoutRootItem
								.setBackgroundResource(R.drawable.shape_sms_detail_not_selected_outbox);
					}
				}
				// viewHolder.layoutCheckBox.setVisibility(View.VISIBLE);
				// viewHolder.cbSelection.setChecked(itemModel.isChecked());
			} else {
				if (MySMSUtils.isInbox(itemModel.getSmsModel().getType())) {
					viewHolder.layoutRootItem.setBackgroundResource(R.drawable.shape_sms_detail_not_selected_inbox);
				} else {
					viewHolder.layoutRootItem.setBackgroundResource(R.drawable.shape_sms_detail_not_selected_outbox);
				}
				// viewHolder.layoutCheckBox.setVisibility(View.GONE);
				// viewHolder.cbSelection.setChecked(false);
			}

			viewHolder.tvContent.setText(smsModel.getContent());
			viewHolder.tvDate.setText(smsModel.getCreateTimeString(MyConstants.DATE_FORMAT));
		}

		return v;
	}

	class ViewHolder {

		LinearLayout layoutRootItem;
		// FrameLayout layoutCheckBox;
		TextView tvContent;
		TextView tvDate;
		// CheckBox cbSelection;
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

	public int getNumberItemSelected() {
		int count = 0;
		for (SmsItemModel item : mObjects) {
			if (item.isChecked()) {
				count++;
			}
		}
		return count;
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
			}
		}
		notifyDataSetChanged();
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

	public void selecteAll() {

		if (isEditAble) {

			for (SmsItemModel item : mObjects) {
				item.setChecked(true);
			}
			notifyDataSetChanged();
		}
	}

	public ArrayList<SmsItemModel> getSelectedObjects() {

		ArrayList<SmsItemModel> rs = new ArrayList<SmsItemModel>();
		for (SmsItemModel item : mObjects) {
			if (item.isChecked()) {
				rs.add(item);
			}
		}
		return rs;
	}
}

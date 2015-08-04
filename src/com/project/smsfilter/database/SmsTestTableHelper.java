package com.project.smsfilter.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.project.smsfilter.database.DatabaseDefinition.BaseDefine;
import com.project.smsfilter.database.DatabaseDefinition.SmsDefine;
import com.project.smsfilter.database.DatabaseDefinition.SmsTestDefine;
import com.project.smsfilter.model.SmsTestModel;

public class SmsTestTableHelper extends BaseTableHelper<SmsTestModel, SmsTestDefine> {

	public SmsTestTableHelper(Context context) {
		super(context);
		mTableName = "sms_test";

	}

	@Override
	public BaseDefine getTableDefine() {
		return new SmsDefine();
	}

	@Override
	public ContentValues getValuesObject(SmsTestModel model) {
		ContentValues values = new ContentValues();
		// values.put(COL_UID, model.getUid());
		if (model.getId() > 0)
			values.put(SmsTestDefine.COL_ID, model.getId());
		
		if (model.getThreadId() > 0)
			values.put(SmsTestDefine.COL_THREAD_ID, model.getThreadId());
		
		values.put(SmsTestDefine.COL_CONTENT, model.getContent());
		values.put(SmsTestDefine.COL_CREATE_TIME, model.getCreateTime());
		// values.put(SmsTestDefine.COL_FORMAT_CONTENT, model.getFormatContent());
		values.put(SmsTestDefine.COL_IS_SPAM, model.isSpam());
		values.put(SmsTestDefine.COL_PHONE_NUMBER, model.getPhoneNumber());
		values.put(SmsTestDefine.COL_PHONE_NAME, model.getPhoneName());
		values.put(SmsTestDefine.COL_STATE, model.getState());
		values.put(SmsTestDefine.COL_TYPE, model.getType());
		values.put(SmsTestDefine.COL_REVIEWED, model.isReviewed());
		return values;
	}

	@Override
	public ArrayList<SmsTestModel> parseCursor(Cursor c) {
		ArrayList<SmsTestModel> list = new ArrayList<SmsTestModel>();
		if (c != null && c.moveToFirst()) {
			do {

				SmsTestModel model = new SmsTestModel();
				model.setUid(c.getInt(c.getColumnIndex(SmsTestDefine.COL_UID)));
				model.setId(c.getInt(c.getColumnIndex(SmsTestDefine.COL_ID)));
				model.setThreadId(c.getInt(c.getColumnIndex(SmsTestDefine.COL_THREAD_ID)));
				model.setContent(c.getString(c.getColumnIndex(SmsTestDefine.COL_CONTENT)));
				model.setCreateTime(c.getLong(c.getColumnIndex(SmsTestDefine.COL_CREATE_TIME)));
				// model.setFormatContent(c.getString(c.getColumnIndex(SmsTestDefine.COL_FORMAT_CONTENT)));
				model.setPhoneNumber(c.getString(c.getColumnIndex(SmsTestDefine.COL_PHONE_NUMBER)));
				model.setPhoneName(c.getString(c.getColumnIndex(SmsTestDefine.COL_PHONE_NAME)));
				model.setSpam(c.getInt(c.getColumnIndex(SmsTestDefine.COL_IS_SPAM)) > 0);
				model.setState(c.getString(c.getColumnIndex(SmsTestDefine.COL_STATE)));
				model.setType(c.getInt(c.getColumnIndex(SmsTestDefine.COL_TYPE)));
				model.setReviewed(c.getInt(c.getColumnIndex(SmsTestDefine.COL_REVIEWED)) > 0);

				list.add(model);
			} while (c.moveToNext());
		}
		return list;
	}

	public boolean update(SmsTestModel model) {

		mSqlWhereClause = String.format(SmsTestDefine.COL_UID + " = %d", model.getUid());
		return super.update(model);
	}

	public boolean delete(String uid) {
		mSqlWhereClause = String.format(SmsTestDefine.COL_UID + " = %s ", uid);
		return super.delete(uid);
	}

	public ArrayList<SmsTestModel> getAll() {

		mSqlOrderClause = SmsTestDefine.COL_UID + " DESC";
		return super.getAll();
	}

	public ArrayList<SmsTestModel> getListNeedFilter() {

		mSqlWhereClause = SmsTestDefine.COL_REVIEWED + " = 0";
		mSqlOrderClause = SmsTestDefine.COL_UID + " DESC";
		return super.getByQuery();
	}

	public ArrayList<SmsTestModel> getListSpam() {

		mSqlWhereClause = SmsTestDefine.COL_IS_SPAM + " = 1";
		mSqlOrderClause = SmsTestDefine.COL_CREATE_TIME + " DESC";
		mSqlGroupClause = SmsTestDefine.COL_THREAD_ID;
		mSqlHavingClause = " max(" + SmsTestDefine.COL_CREATE_TIME + ")";
		return super.getByQuery();
	}

	public ArrayList<SmsTestModel> getListNotSpam() {

		mSqlWhereClause = SmsTestDefine.COL_IS_SPAM + " = 0 ";
		mSqlOrderClause = SmsTestDefine.COL_CREATE_TIME + " DESC";
		mSqlGroupClause = SmsTestDefine.COL_THREAD_ID;
		mSqlHavingClause = " max(" + SmsTestDefine.COL_CREATE_TIME + ")";
		return super.getByQuery();
	}

	public ArrayList<SmsTestModel> getListNotSpamByPhoneNumber(String phoneNumber) {

		mSqlOrderClause = SmsTestDefine.COL_CREATE_TIME + " DESC";
		mSqlWhereClause = SmsTestDefine.COL_PHONE_NUMBER + " = '" + phoneNumber + "' and " + SmsTestDefine.COL_IS_SPAM
				+ " = 0 ";
		return super.getByQuery();
	}

	public ArrayList<SmsTestModel> getListSpamByPhoneNumber(String phoneNumber) {

		mSqlOrderClause = SmsTestDefine.COL_CREATE_TIME + " DESC";
		mSqlWhereClause = SmsTestDefine.COL_PHONE_NUMBER + " = '" + phoneNumber + "' and " + SmsTestDefine.COL_IS_SPAM
				+ " = 1 ";
		return super.getByQuery();
	}

	public SmsTestModel getObject(String uid) {

		mSqlWhereClause = String.format(SmsTestDefine.COL_UID + " = %d", uid);
		return super.getObject(uid);
	}

	public SmsTestModel getSmsByContent(String content) {

		mSqlHavingClause = null;
		mSqlWhereClause = String.format(SmsTestDefine.COL_CONTENT + " = '%s'", content);
		mSqlOrderClause = SmsTestDefine.COL_CREATE_TIME + " DESC";
		ArrayList<SmsTestModel> list = getByQuery();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public boolean checkExist(SmsTestModel model) {

		mSqlWhereClause = String.format(" %s = '%s' and %s = '%s' and %s = '%d' ", SmsTestDefine.COL_PHONE_NUMBER,
				model.getPhoneNumber(), SmsDefine.COL_CONTENT, model.getContent(), SmsDefine.COL_CREATE_TIME,
				model.getCreateTime());
		ArrayList<SmsTestModel> list = getByQuery();
		return list.size() > 0;
	}

	// public ArrayList<SmsTestModel> getListSpamGroupByPhoneNumber() {
	//
	// ArrayList<SmsTestModel> listSpam = getListSpam();
	// ArrayList<SmsTestModel> listResult;
	//
	// String query = String.format("Select distinct * from %s where %s = 1 and %s <= 0 ", SmsTestDefine.TABLE_NAME,
	// SmsTestDefine.COL_IS_SPAM, SmsTestDefine.COL_REVIEWED);
	//
	// return super.getByQuery(query);
	// }

}

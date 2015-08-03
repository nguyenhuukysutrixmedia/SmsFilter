package com.project.smsfilter.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.project.smsfilter.database.DatabaseDefinition.BaseDefine;
import com.project.smsfilter.database.DatabaseDefinition.SmsDefine;
import com.project.smsfilter.model.SMSModel;

public class SmsTableHelper extends BaseTableHelper<SMSModel, SmsDefine> {

	//
	protected MySQLiteHelper mSqLiteHelper;
	protected String[] mSelectColumns;

	public SmsTableHelper(Context context) {
		super(context);
	}

	@Override
	public BaseDefine getTableDefine() {
		return new SmsDefine();
	}

	@Override
	public ContentValues getValuesObject(SMSModel model) {

		ContentValues values = new ContentValues();
		// values.put(COL_UID, model.getUid());
		values.put(SmsDefine.COL_ID, model.getId());
		values.put(SmsDefine.COL_CONTENT, model.getContent());
		values.put(SmsDefine.COL_CREATE_TIME, model.getCreateTimeString());
		// values.put(COL_FORMAT_CONTENT, model.getFormatContent());
		values.put(SmsDefine.COL_IS_SPAM, model.isSpam());
		values.put(SmsDefine.COL_PHONE_NUMBER, model.getPhoneNumber());
		values.put(SmsDefine.COL_STATE, model.getState());
		values.put(SmsDefine.COL_TYPE, model.getType());
		return values;
	}

	@Override
	public ArrayList<SMSModel> parseCursor(Cursor c) {

		ArrayList<SMSModel> list = new ArrayList<SMSModel>();
		if (c != null && c.moveToFirst()) {
			do {

				SMSModel model = new SMSModel();
				model.setUid(c.getInt(c.getColumnIndex(SmsDefine.COL_UID)));
				model.setId(c.getInt(c.getColumnIndex(SmsDefine.COL_ID)));
				model.setContent(c.getString(c.getColumnIndex(SmsDefine.COL_CONTENT)));
				model.setCreateTimeDatabase(c.getString(c.getColumnIndex(SmsDefine.COL_CREATE_TIME)));
				// model.setFormatContent(c.getString(c.getColumnIndex(COL_FORMAT_CONTENT)));
				model.setPhoneNumber(c.getString(c.getColumnIndex(SmsDefine.COL_PHONE_NUMBER)));
				model.setSpam(c.getInt(c.getColumnIndex(SmsDefine.COL_IS_SPAM)) > 0);
				model.setState(c.getString(c.getColumnIndex(SmsDefine.COL_STATE)));
				model.setType(c.getInt(c.getColumnIndex(SmsDefine.COL_TYPE)));

				list.add(model);
			} while (c.moveToNext());
		}
		return list;
	}

	@Override
	public long insert(SMSModel model) {

		long id = super.insert(model);
		if (id <= 0) {
			update(model);
		}
		return id;
	}

	public boolean update(SMSModel model) {

		mSqlWhereClause = String.format(SmsDefine.COL_UID + " = %d", model.getUid());
		return super.update(model);
	}

	public boolean delete(String uid) {
		mSqlWhereClause = String.format(SmsDefine.COL_UID + " = %d", uid);
		return super.delete(uid);
	}

	public ArrayList<SMSModel> getAll() {

		mSqlOrderClause = SmsDefine.COL_UID + " DESC";
		return super.getAll();
	}

	public ArrayList<SMSModel> getListSpam() {

		mSqlWhereClause = SmsDefine.COL_IS_SPAM + " = 1";
		mSqlOrderClause = SmsDefine.COL_UID + " ASC";
		return super.getByQuery();
	}

	public ArrayList<SMSModel> getListNotSpam() {

		mSqlWhereClause = SmsDefine.COL_IS_SPAM + " = 0 ";
		mSqlOrderClause = SmsDefine.COL_UID + " ASC";
		return super.getByQuery();
	}

	public ArrayList<SMSModel> getListSpamOrderDate() {

		mSqlWhereClause = SmsDefine.COL_IS_SPAM + " = 1";
		mSqlOrderClause = String.format(" %s DESC", SmsDefine.COL_CREATE_TIME);
		// mSqlGroupClause = SmsDefine.COL_PHONE_NUMBER;
		return super.getByQuery();
	}

	public ArrayList<SMSModel> getListNotSpamOrderDate() {

		mSqlWhereClause = SmsDefine.COL_IS_SPAM + " = 0 ";
		mSqlOrderClause = String.format(" %s DESC", SmsDefine.COL_CREATE_TIME);
		// mSqlGroupClause = SmsDefine.COL_PHONE_NUMBER;
		return super.getByQuery();
	}

	public SMSModel getSmsByContent(String content) {

		mSqlWhereClause = String.format(SmsDefine.COL_CONTENT + " = %s", content);
		return super.getObject(content);
	}

	public SMSModel getObject(String uid) {

		mSqlWhereClause = String.format(SmsDefine.COL_UID + " = %d", uid);
		return super.getObject(uid);
	}
}

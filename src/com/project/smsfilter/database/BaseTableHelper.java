package com.project.smsfilter.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.project.smsfilter.database.DatabaseDefinition.BaseDefine;
import com.project.smsfilter.utilities.MyLog;

public abstract class BaseTableHelper<M, D> {

	protected String mTableName;
	protected MySQLiteHelper mSqLiteHelper;
	protected Context mContext;
	protected String[] mSelectColumns;

	protected String mSqlWhereClause;
	protected String mSqlOrderClause;
	protected String mSqlGroupClause;
	protected String mSqlHavingClause;

	protected BaseDefine mDefine;

	public BaseTableHelper(Context context) {
		mContext = context;
		mSqLiteHelper = new MySQLiteHelper(context);
		mDefine = getTableDefine();
		mSelectColumns = mDefine.getColoumns();
		mTableName = mDefine.getTableName();
	}

	abstract public BaseDefine getTableDefine();

	abstract public ContentValues getValuesObject(M model);

	abstract public ArrayList<M> parseCursor(Cursor c);

	public long insert(M model) {

		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mSqLiteHelper.getWritableDatabase();
				try {
					db.beginTransaction();
					long id = db.insertOrThrow(mTableName, null, getValuesObject(model));
					db.setTransactionSuccessful();
					// MyLog.iLog("BaseTableHelper-insert id: " + id);
					return id;
				} catch (SQLException e) {
					// update(model);
					e.printStackTrace();
					MyLog.eLog(this + "-BaseTableHelper-insert error: " + e + "-" + model);
					return -1;
				} catch (Exception e) {
					e.printStackTrace();
					MyLog.eLog(this + "-BaseTableHelper-insert error: " + e + "-" + model);
					return -1;
				} finally {
					if (db != null) {
						db.endTransaction();
					}
				}
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public boolean update(M model) {

		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mSqLiteHelper.getWritableDatabase();
				try {
					db.beginTransaction();
					db.update(mTableName, getValuesObject(model), mSqlWhereClause, null);
					db.setTransactionSuccessful();
					// MyLog.iLog("BaseTableHelper-update id: " + model);
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
					MyLog.eLog("BaseTableHelper-update error: " + e + "-" + model);
					return false;
				} catch (Exception e) {
					e.printStackTrace();
					MyLog.eLog("BaseTableHelper-update error: " + e + "-" + model);
					return false;
				}

				finally {
					db.endTransaction();
				}
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public boolean delete(String uid) {
		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mSqLiteHelper.getWritableDatabase();
				try {

					db.beginTransaction();
					db.delete(mTableName, mSqlWhereClause, new String[] {});
					db.setTransactionSuccessful();
					// MyLog.iLog("BaseTableHelper-delete id: " + uid);
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
					MyLog.eLog(this + "-BaseTableHelper-delete error: " + e);
					return false;
				} catch (Exception e) {
					e.printStackTrace();
					MyLog.eLog(this + "-BaseTableHelper-delete error: " + e);
					return false;
				} finally {
					db.endTransaction();
				}
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public boolean deleteAll() {
		SQLiteDatabase db = null;
		synchronized (this) {
			try {
				db = mSqLiteHelper.getWritableDatabase();
				try {

					db.beginTransaction();
					db.delete(mTableName, " 1 ", new String[] {});
					db.setTransactionSuccessful();

					// MyLog.iLog("BaseTableHelper-deleteAll done");
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
					MyLog.eLog(this + "-BaseTableHelper-deleteAll error: " + e);
					return false;
				} catch (Exception e) {
					e.printStackTrace();
					MyLog.eLog(this + "-BaseTableHelper-deleteAll error: " + e);
					return false;
				} finally {
					db.endTransaction();
				}
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
	}

	public ArrayList<M> getAll() {
		mSqlWhereClause = null;
		return getByQuery();
	}

	public ArrayList<M> getByQuery() {

		ArrayList<M> lst = new ArrayList<M>();

		SQLiteDatabase db = null;
		Cursor c = null;

		try {
			db = mSqLiteHelper.getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(mTableName);

			c = qb.query(db, // Database
					null, //
					mSqlWhereClause, //
					null, // argument[]
					mSqlGroupClause, // groupBy
					mSqlHavingClause, // having
					mSqlOrderClause// order
			);
			lst = parseCursor(c);
		} catch (SQLException e) {
			e.printStackTrace();
			MyLog.eLog(this + "-BaseTableHelper-getAll error: " + e);
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eLog(this + "-BaseTableHelper-getAll error: " + e);
		} finally {
			if (db != null) {
				db.close();
			}
			if (c != null) {
				c.close();
			}
		}
		return lst;
	}

	public ArrayList<M> getByQuery(String selectQuery) {

		ArrayList<M> lstResult = new ArrayList<M>();
		SQLiteDatabase db = null;
		Cursor c = null;

		try {
			db = mSqLiteHelper.getReadableDatabase();
			c = db.rawQuery(selectQuery, new String[] {});
			lstResult = parseCursor(c);
		} catch (SQLException e) {
			e.printStackTrace();
			MyLog.eLog(this + "-BaseTableHelper-getByQuery error: " + e);
		} catch (Exception e) {
			e.printStackTrace();
			MyLog.eLog(this + "-BaseTableHelper-getByQuery error: " + e);
		} finally {
			if (db != null) {
				db.close();
			}
			if (c != null) {
				c.close();
			}
		}
		return lstResult;
	}

	public M getObject(String uid) {

		ArrayList<M> lst = getByQuery();
		if (lst.size() > 0)
			return lst.get(0);
		return null;
	}

}

package com.project.smsfilter.database;

public class DatabaseDefinition {

	public static final int DATABASE_VERSION = 1;
	// public static final String DATABASE_NAME = "SMS_Filter_db.db";
	public static final String DATABASE_NAME = "SMS_Filter_db_2";

	public static final String CREATE_QUERY_METHOD = "createQuery";
	public static final String UPDATE_QUERY_METHOD = "updateQuery";
	public static final String INIT_DATA_METHOD = "initData";

	public final static String CREATE_TABLE = "CREATE TABLE ";
	public final static String ALTER_TABLE = "ALTER TABLE ";
	public final static String ADD_COLUMN = " ADD COLUMN ";
	public final static String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

	public final static String T_REAL = " REAL ";
	public final static String T_INTEGER = " INTEGER ";
	public final static String T_TEXT = " TEXT ";

	public final static String T_UNIQUE = " UNIQUE ";
	public final static String T_PRIMARY_KEY_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT ";
	public final static String T_PRIMARY_KEY = " PRIMARY KEY ";
	public final static String T_NOCASE = " COLLATE NOCASE ";

	public final static String O_ASC = " ASC";
	public final static String O_DESC = " DESC";

	/** tables in database */
	public static final BaseDefine[] Tables = {new SmsDefine(), new SmsTestDefine()};

	public interface BaseDefine {

		public String createQuery();
		public String updateQuery();
		public String[] getColoumns();
		public String getTableName();
	}

	public static final class SmsDefine implements BaseDefine {

		public static final String TABLE_NAME = "sms";
		//
		public static final String COL_ID = "c_id"; // 1
		public static final String COL_PHONE_NUMBER = "c_phone_number"; // 2
		public static final String COL_CREATE_TIME = "c_create_time"; // 3
		public static final String COL_CONTENT = "c_content"; // 4
		public static final String COL_TYPE = "c_type"; // 5
		public static final String COL_STATE = "c_state"; // 6
		public static final String COL_IS_SPAM = "c_is_spam"; // 7
		public static final String COL_UID = "c_uid"; // 8
		// public static final String COL_FORMAT_CONTENT = "c_format_content";

		@Override
		public String createQuery() {
			return CREATE_TABLE + TABLE_NAME + " (" //
					+ COL_ID + T_INTEGER + "," // 1
					+ COL_PHONE_NUMBER + T_TEXT + "," // 2
					+ COL_CREATE_TIME + T_INTEGER + "," // 3
					+ COL_CONTENT + T_TEXT + T_UNIQUE + "," // 4
					+ COL_TYPE + T_INTEGER + "," // 5
					+ COL_STATE + T_TEXT + "," // 6
					+ COL_IS_SPAM + T_INTEGER + "," // 7
					+ COL_UID + T_INTEGER + T_PRIMARY_KEY_AUTOINCREMENT // 8
					+ ") ";// 8
		}

		@Override
		public String updateQuery() {
			return DROP_TABLE_IF_EXISTS + TABLE_NAME;
		}

		@Override
		public String[] getColoumns() {
			return new String[]{ //
			COL_ID, // 1
					COL_PHONE_NUMBER, // 2
					COL_CREATE_TIME, // 3
					COL_CONTENT, // 4
					COL_TYPE, // 5
					COL_STATE, // 6
					COL_IS_SPAM, // 7
					COL_UID // 8
			};
		}

		@Override
		public String getTableName() {
			return TABLE_NAME;
		}
	}

	public static final class SmsTestDefine implements BaseDefine {

		public static final String TABLE_NAME = "sms_test";
		//
		public static final String COL_ID = "c_id";
		public static final String COL_THREAD_ID = "c_thread_id"; //
		public static final String COL_PHONE_NUMBER = "c_phone_number";
		public static final String COL_PHONE_NAME = "c_phone_name";
		public static final String COL_CREATE_TIME = "c_create_time";
		public static final String COL_CONTENT = "c_content";
		public static final String COL_TYPE = "c_type";
		public static final String COL_STATE = "c_state";
		public static final String COL_IS_SPAM = "c_is_spam";
		public static final String COL_UID = "c_uid";
		public static final String COL_REVIEWED = "c_reviewed";

		@Override
		public String createQuery() {
			return CREATE_TABLE + TABLE_NAME + " (" //
					+ COL_ID + T_INTEGER + T_UNIQUE + "," // 1
					+ COL_THREAD_ID + T_INTEGER + "," //
					+ COL_PHONE_NUMBER + T_TEXT + "," // 2
					+ COL_PHONE_NAME + T_TEXT + "," //
					+ COL_CREATE_TIME + T_INTEGER + "," // 3
					+ COL_CONTENT + T_TEXT + "," // 4
					+ COL_TYPE + T_INTEGER + "," // 5
					+ COL_STATE + T_TEXT + "," // 6
					+ COL_IS_SPAM + T_INTEGER + "," // 7
					+ COL_UID + T_INTEGER + T_PRIMARY_KEY_AUTOINCREMENT + ","// 8
					+ COL_REVIEWED + T_INTEGER // 9
					+ ") ";// 8
		}

		@Override
		public String updateQuery() {
			return DROP_TABLE_IF_EXISTS + TABLE_NAME;
		}

		@Override
		public String[] getColoumns() {
			return new String[]{ //
			COL_ID,// 1
					COL_THREAD_ID, //
					COL_PHONE_NUMBER, // 2
					COL_PHONE_NAME, //
					COL_CREATE_TIME, // 3
					COL_CONTENT, // 4
					COL_TYPE, // 5
					COL_STATE, // 6
					COL_IS_SPAM, // 7
					COL_UID, // 8
					COL_REVIEWED // 9
			};
		}

		@Override
		public String getTableName() {
			return TABLE_NAME;
		}
	}
}

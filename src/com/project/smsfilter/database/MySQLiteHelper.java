package com.project.smsfilter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.smsfilter.database.DatabaseDefinition.BaseDefine;
import com.project.smsfilter.utilities.MyLog;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// private static String DB_PATH = "";
	// private SQLiteDatabase myDataBase;
	// private Context myContext;

	public MySQLiteHelper(Context context) {
		super(context, DatabaseDefinition.DATABASE_NAME, null, DatabaseDefinition.DATABASE_VERSION);
		// myContext = context;
		// DB_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
		// createDataBase();
	}

	// /**
	// * Creates a empty database on the system and rewrites it with your own database.
	// * */
	// public void createDataBase() {
	//
	// boolean dbExist = checkDataBase();
	// if (dbExist) {
	// // do nothing - database already exist
	// } else {
	// // By calling this method and empty database will be created into the default system path
	// // of your application so we are gonna be able to overwrite that database with our database.
	// this.getReadableDatabase();
	// try {
	// copyDataBase();
	// } catch (IOException e) {
	// // throw new Error("Error copying database");
	// MyLog.eLog("MySQLiteHelper - error copying database: " + e);
	// }
	// }
	// }
	//
	// /**
	// * Check if the database already exist to avoid re-copying the file each time you open the application.
	// *
	// * @return true if it exists, false if it doesn't
	// */
	// private boolean checkDataBase() {
	//
	// SQLiteDatabase checkDB = null;
	//
	// try {
	// checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
	// } catch (SQLiteException e) {
	// // database does't exist.
	// }
	// if (checkDB != null) {
	// checkDB.close();
	// }
	//
	// return checkDB != null ? true : false;
	// }
	//
	// /**
	// * Copies your database from your local assets-folder to the just created empty database in the system folder,
	// from
	// * where it can be accessed and handled. This is done by transfering bytestream.
	// * */
	// private void copyDataBase() throws IOException {
	//
	// // Open your local db as the input stream
	// InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
	//
	// // Path to the just created empty db
	// String outFileName = DB_PATH;
	//
	// // Open the empty db as the output stream
	// OutputStream myOutput = new FileOutputStream(outFileName);
	//
	// // transfer bytes from the inputfile to the outputfile
	// byte[] buffer = new byte[1024];
	// int length;
	// while ((length = myInput.read(buffer)) > 0) {
	// myOutput.write(buffer, 0, length);
	// }
	//
	// // Close the streams
	// myOutput.flush();
	// myOutput.close();
	// myInput.close();
	//
	// }
	//
	// public void openDatabase() throws SQLException {
	//
	// // Open the database
	// myDataBase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
	// }
	//
	// @Override
	// public synchronized void close() {
	//
	// if (myDataBase != null)
	// myDataBase.close();
	// super.close();
	// }

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (BaseDefine define : DatabaseDefinition.Tables) {
			try {
				db.execSQL(define.createQuery());
			} catch (Exception e) {
				e.printStackTrace();
				MyLog.eLog(String.format("MySQLiteHelper- create table %s error:", define, e.toString()));
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (BaseDefine define : DatabaseDefinition.Tables) {
			try {
				db.execSQL(define.updateQuery());
			} catch (Exception e) {
				e.printStackTrace();
				MyLog.eLog(String.format("MySQLiteHelper- update table %s error:", define, e.toString()));
			}
		}
		onCreate(db);
	}

}

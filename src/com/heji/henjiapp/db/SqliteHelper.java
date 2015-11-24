package com.heji.henjiapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	private static final String DataBaseName = "henji.db";
	private static final int DataBaseVersion = 1;
	private static final String tableSql = "CREATE TABLE IF NOT EXISTS guide_table(_id INTEGER PRIMARY KEY, link_id VARCHAR, link VARCHAR, logo VARCHAR, location VARCHAR, time VARCHAR, category_id VARCHAR,category VARCHAR,isdefault INTEGER)";
	private static final String updateSql = "CREATE TABLE IF NOT EXISTS update_table(_id INTEGER PRIMARY KEY, isupdate INTEGER)";

	private static SqliteHelper mInstance;

	public SqliteHelper(Context context) {
		super(context, DataBaseName, null, DataBaseVersion);
	}

	public static SqliteHelper getInstance(Context context) {
		if (mInstance == null) {
			synchronized (SqliteHelper.class) {
				mInstance = new SqliteHelper(context);
			}

		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(tableSql);
		db.execSQL(updateSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}

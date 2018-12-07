package com.db;

import android.database.sqlite.SQLiteDatabase;

public class ISqliteDataBase {

	private static SQLiteDatabase sqLiteDatabase = null;

	private final static Object object = new Object();
	
	public static SQLiteDatabase getSqLiteDatabase(){
		if (null == sqLiteDatabase) {
			synchronized (object) {
				if (null == sqLiteDatabase) {
					sqLiteDatabase = new IDbHelper().getWritableDatabase();
				}
			}
		}
		return sqLiteDatabase;
	}

}

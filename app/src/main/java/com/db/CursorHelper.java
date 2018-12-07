package com.db;

import android.database.Cursor;

public class CursorHelper {

	public static String getString(Cursor cursor , String columnName){
		return cursor.getString(cursor.getColumnIndex(columnName));
	}
	
	public static int getInt(Cursor cursor , String columnName){
		return cursor.getInt(cursor.getColumnIndex(columnName));
	}
	
	public static long getLong(Cursor cursor , String columnName){
		return cursor.getLong(cursor.getColumnIndex(columnName));
	}
	
	public static double getDouble(Cursor cursor , String columnName){
		return cursor.getDouble(cursor.getColumnIndex(columnName));
	}	
	
	public static float getFloat(Cursor cursor , String columnName){
		return cursor.getFloat(cursor.getColumnIndex(columnName));
	}	
}

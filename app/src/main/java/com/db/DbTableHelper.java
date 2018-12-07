package com.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by E on 2017/11/22.
 */
public final class DbTableHelper {

    private static DbTableHelper dbTableHelper = null;
    private StringBuilder builder = null;

    private DbTableHelper(){
    }

    private DbTableHelper(String dbName){
        builder = new StringBuilder();
        builder.append("create table if not exists " + dbName);
        builder.append("(id INTEGER PRIMARY KEY AUTOINCREMENT,");
    }

    public static DbTableHelper fromTableName(String dbName){
        dbTableHelper = new DbTableHelper(dbName);
        return dbTableHelper;
    }

    public DbTableHelper addColumn_Integer(String columnName){
        builder.append(columnName + " Integer,");
        return dbTableHelper;
    }

    public DbTableHelper addColumn_Long(String columnName){
        builder.append(columnName + " Long,");
        return dbTableHelper;
    }

    public DbTableHelper addColumn_Float(String columnName){
        builder.append(columnName + " Float,");
        return dbTableHelper;
    }

    public DbTableHelper addColumn_Double(String columnName){
        builder.append(columnName + " Double,");
        return dbTableHelper;
    }

    public DbTableHelper addColumn_Varchar(String columnName , int length){
        builder.append(columnName + " varchar(" + length + "),");
        return dbTableHelper;
    }

    public void buildTable(SQLiteDatabase db){
        builder.deleteCharAt(builder.lastIndexOf(","));
        builder.append(")");
        db.execSQL(builder.toString());
    }

}

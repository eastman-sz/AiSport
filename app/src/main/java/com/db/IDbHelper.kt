package com.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.application.IApplication
import com.sportdata.GpsInfoDbHelper
import com.sportdata.SportInfoDbHelper

class IDbHelper : SQLiteOpenHelper {

    constructor() : super(IApplication.context , "aiSport" , null , 1)

    override fun onCreate(db: SQLiteDatabase) {
        SportInfoDbHelper.createTable(db)
        GpsInfoDbHelper.createTable(db)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        SportInfoDbHelper.dropTable(db)
        GpsInfoDbHelper.dropTable(db)
    }
}
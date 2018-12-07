package com.sportdata

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.db.CursorHelper
import com.db.DbTableHelper
import com.db.ISqliteDataBase
import com.gaode.SportParam
import java.lang.Exception

class SportInfoDbHelper {

    companion object {

        fun onStart(){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("startTime" , SportParam.sportId)

            ISqliteDataBase.getSqLiteDatabase().insert(DBNAME , null , values)
        }

        fun onFinish(){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("endTime" , System.currentTimeMillis()/1000)

            ISqliteDataBase.getSqLiteDatabase().update(DBNAME , values , "sportId = ? " , arrayOf(SportParam.sportId.toString()))
        }

        fun getSports() : List<SportInfo>{
            val list = ArrayList<SportInfo>()
            var cursor : Cursor ?= null
            try {
                cursor = ISqliteDataBase.getSqLiteDatabase().query(DBNAME , null , null , null , null , null ,null)
                cursor?.let {
                    while (it.moveToNext()){
                        list.add(fromCursor(it))
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                cursor?.close()
            }
            return list
        }

        fun getSports(sportId : Long) : List<SportInfo>{
            val list = ArrayList<SportInfo>()
            var cursor : Cursor ?= null
            try {
                cursor = ISqliteDataBase.getSqLiteDatabase().query(DBNAME , null , "sportId = ? " , arrayOf(sportId.toString()) , null , null ,null)
                cursor?.let {
                    while (it.moveToNext()){
                        list.add(fromCursor(it))
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                cursor?.close()
            }
            return list
        }

        fun fromCursor(cursor: Cursor) : SportInfo{
            val sportId = CursorHelper.getLong(cursor , "sportId")
            val startTime = CursorHelper.getLong(cursor , "startTime")
            val endTime = CursorHelper.getLong(cursor , "endTime")

            val sportInfo = SportInfo()
            sportInfo.sportId = sportId
            sportInfo.startTime = startTime
            sportInfo.endTime = endTime

            return sportInfo
        }


        private const val DBNAME = "sportInfo"

        fun createTable(db : SQLiteDatabase){
            DbTableHelper.fromTableName(DBNAME)
                .addColumn_Long("sportId")
                .addColumn_Long("startTime")
                .addColumn_Long("endTime")
                .buildTable(db)
        }

        fun dropTable(db : SQLiteDatabase){
            db.execSQL("drop table if exists $DBNAME")
        }
    }

}
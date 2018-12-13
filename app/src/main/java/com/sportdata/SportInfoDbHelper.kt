package com.sportdata

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.db.CursorHelper
import com.db.DbTableHelper
import com.db.ISqliteDataBase
import com.gaode.SportParam
import kotlin.Exception

class SportInfoDbHelper {

    companion object {

        fun onStart(){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("startTime" , SportParam.sportId)

            val count = ISqliteDataBase.getSqLiteDatabase().update(DBNAME , values , "sportId = ? " , arrayOf(SportParam.sportId.toString()))
            if (count < 1){
                ISqliteDataBase.getSqLiteDatabase().insert(DBNAME , null , values)
            }
        }

        fun onFinish(){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("endTime" , System.currentTimeMillis()/1000)
            values.put("complete" , 1)

            ISqliteDataBase.getSqLiteDatabase().update(DBNAME , values , "sportId = ? " , arrayOf(SportParam.sportId.toString()))
        }

        fun getSports() : List<SportInfo>{
            val list = ArrayList<SportInfo>()
            var cursor : Cursor ?= null
            try {
                cursor = ISqliteDataBase.getSqLiteDatabase().query(DBNAME , null , null , null , null , null ,"sportId desc")
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

        //最后一次未完成的ID。最后一条数据，是否已完成
        fun getLatestUnCompleteSportId() : Long{
            var sportId = 0L
            var cursor : Cursor ?= null
            try {
                cursor = ISqliteDataBase.getSqLiteDatabase().query(DBNAME , null , null , null , null , null ,"sportId desc")
                cursor?.let {
                    if (it.count > 0){
                        it.moveToFirst()
                        val sportInfo = fromCursor(it)

                        sportId = if (0 == sportInfo.complete) sportInfo.sportId else 0
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                cursor?.close()
            }
            return sportId
        }

        fun delete(sportId : Long){
            ISqliteDataBase.getSqLiteDatabase().delete(DBNAME , "sportId = ? " , arrayOf(sportId.toString()))
        }

        private fun fromCursor(cursor: Cursor) : SportInfo{
            val sportId = CursorHelper.getLong(cursor , "sportId")
            val startTime = CursorHelper.getLong(cursor , "startTime")
            val endTime = CursorHelper.getLong(cursor , "endTime")
            val complete = CursorHelper.getInt(cursor , "complete")

            val sportInfo = SportInfo()
            sportInfo.sportId = sportId
            sportInfo.startTime = startTime
            sportInfo.endTime = endTime
            sportInfo.complete = complete

            return sportInfo
        }


        private const val DBNAME = "sportInfo"

        fun createTable(db : SQLiteDatabase){
            DbTableHelper.fromTableName(DBNAME)
                .addColumn_Long("sportId")
                .addColumn_Long("startTime")
                .addColumn_Long("endTime")
                .addColumn_Integer("complete")
                .buildTable(db)
        }

        fun dropTable(db : SQLiteDatabase){
            db.execSQL("drop table if exists $DBNAME")
        }
    }

}
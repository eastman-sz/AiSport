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

        fun onUpdate(){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("endTime" , System.currentTimeMillis()/1000)

            ISqliteDataBase.getSqLiteDatabase().update(DBNAME , values , "sportId = ? " , arrayOf(SportParam.sportId.toString()))
        }

        fun onUpdateDistance(distance : Double){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("distance" , distance)
            //计算卡路里
            val duration = System.currentTimeMillis()/1000 - SportParam.sportId
            val calorie = CalorieCalHelper.getRunningCalorieByDis(distance , duration.toInt()).toInt()
            values.put("calorie" , calorie)

            ISqliteDataBase.getSqLiteDatabase().update(DBNAME , values , "sportId = ? " , arrayOf(SportParam.sportId.toString()))
        }

        fun onUpdatePaceInfo(minPace : Int , maxPace : Int){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("avgPace" , (minPace + maxPace)/2)
            values.put("maxPace" , maxPace)
            values.put("minPace" , minPace)

            ISqliteDataBase.getSqLiteDatabase().update(DBNAME , values , "sportId = ? " , arrayOf(SportParam.sportId.toString()))
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

        fun getSports(sportId : Long) : SportInfo{
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
            return if (list.isEmpty()) SportInfo() else list[0]
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
            val distance = CursorHelper.getFloat(cursor , "distance")
            val avgPace = CursorHelper.getInt(cursor , "avgPace")
            val maxPace = CursorHelper.getInt(cursor , "maxPace")
            val minPace = CursorHelper.getInt(cursor , "minPace")
            val calorie = CursorHelper.getInt(cursor , "calorie")

            val sportInfo = SportInfo()
            sportInfo.sportId = sportId
            sportInfo.startTime = startTime
            sportInfo.endTime = endTime
            sportInfo.complete = complete
            sportInfo.distance = distance
            sportInfo.avgPace = avgPace
            sportInfo.maxPace = maxPace
            sportInfo.minPace = minPace
            sportInfo.calorie = calorie

            return sportInfo
        }

        private const val DBNAME = "sportInfo"

        fun createTable(db : SQLiteDatabase){
            DbTableHelper.fromTableName(DBNAME)
                .addColumn_Long("sportId")
                .addColumn_Long("startTime")
                .addColumn_Long("endTime")
                .addColumn_Integer("complete")
                .addColumn_Float("distance")
                .addColumn_Integer("avgPace")
                .addColumn_Integer("maxPace")
                .addColumn_Integer("minPace")
                .addColumn_Integer("calorie")
                .buildTable(db)
        }

        fun dropTable(db : SQLiteDatabase){
            db.execSQL("drop table if exists $DBNAME")
        }
    }

}
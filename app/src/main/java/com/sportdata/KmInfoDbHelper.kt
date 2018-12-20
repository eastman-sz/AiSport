package com.sportdata

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.db.CursorHelper
import com.db.DbTableHelper
import com.db.ISqliteDataBase
import com.gaode.SportParam

class KmInfoDbHelper {

    companion object {

        fun save(km : Int , duration : Int , latitude : Double , longitude : Double , steps : Int){
            val values = ContentValues()
            values.put("sportId" , SportParam.sportId)
            values.put("km" , km)
            values.put("duration" , duration)
            values.put("latitude" , latitude)
            values.put("longitude" , longitude)
            values.put("steps" , steps)

            val count = ISqliteDataBase.getSqLiteDatabase().update(DBNAME, values , "sportId = ? and km = ? " ,
                arrayOf(SportParam.sportId.toString() , km.toString()))
            if (count < 1){
                ISqliteDataBase.getSqLiteDatabase().insert(DBNAME, null , values)
            }
        }

        fun getKmInfos(sportId : Long) : List<KmInfo>{
            val list = ArrayList<KmInfo>()
            var cursor : Cursor ?= null
            try {
                cursor = ISqliteDataBase.getSqLiteDatabase().query(DBNAME, null , "sportId = ? " , arrayOf(sportId.toString()) , null , null ,null)
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

        private fun fromCursor(cursor: Cursor) : KmInfo{
            val sportId = CursorHelper.getLong(cursor , "sportId")
            val km = CursorHelper.getInt(cursor , "km")
            val duration = CursorHelper.getInt(cursor , "duration")
            val latitude = CursorHelper.getDouble(cursor , "latitude")
            val longitude = CursorHelper.getDouble(cursor , "longitude")
            val steps = CursorHelper.getInt(cursor , "steps")

            val kmInfo = KmInfo()
            kmInfo.sportId = sportId
            kmInfo.km = km
            kmInfo.duration = duration
            kmInfo.latitude = latitude
            kmInfo.longitude = longitude
            kmInfo.steps = steps

            return kmInfo
        }

        private const val DBNAME = "sportKmInfo"

        fun createTable(db : SQLiteDatabase){
            DbTableHelper.fromTableName(DBNAME)
                .addColumn_Long("sportId")
                .addColumn_Integer("km")
                .addColumn_Integer("duration")
                .addColumn_Double("latitude")
                .addColumn_Double("longitude")
                .addColumn_Integer("steps")
                .buildTable(db)
        }

        fun dropTable(db : SQLiteDatabase){
            db.execSQL("drop table if exists $DBNAME")
        }

    }

}
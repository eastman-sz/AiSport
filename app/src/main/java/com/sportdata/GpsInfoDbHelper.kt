package com.sportdata

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.LatLng
import com.amap.api.trace.TraceLocation
import com.db.CursorHelper
import com.db.DbTableHelper
import com.db.ISqliteDataBase
import com.gaode.SportParam
import java.lang.Exception

class GpsInfoDbHelper {

    companion object {

        fun save(location: AMapLocation){
            val values = ContentValues()
            values.put("bearing" , location.bearing)
            values.put("speed" , location.speed)
            values.put("accuracy" , location.accuracy)
            values.put("latitude" , location.latitude)
            values.put("longitude" , location.longitude)
            values.put("time" , location.time)
            values.put("sportId" , SportParam.sportId)

            ISqliteDataBase.getSqLiteDatabase().insert(DBNAME , null , values)
        }

        fun getTraceLocation(sportId : Long) : List<TraceLocation>{
            val list = ArrayList<TraceLocation>()
            var cursor : Cursor ?= null
            try {
                cursor = ISqliteDataBase.getSqLiteDatabase().query(DBNAME , null , "sportId = ?" , arrayOf(sportId.toString()) , null ,null, null)
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

        fun getLatLng(sportId : Long) : List<LatLng>{
            val list = ArrayList<LatLng>()
            var cursor : Cursor ?= null
            try {
                cursor = ISqliteDataBase.getSqLiteDatabase().query(DBNAME , null , "sportId = ?" , arrayOf(sportId.toString()) , null ,null, null)
                cursor?.let {
                    while (it.moveToNext()){
                        list.add(latLngFromCursor(it))
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                cursor?.close()
            }
            return list
        }

        private fun fromCursor(cursor: Cursor) : TraceLocation{
            val bearing = CursorHelper.getFloat(cursor , "bearing")
            val speed = CursorHelper.getFloat(cursor , "speed")
            val accuracy = CursorHelper.getFloat(cursor , "accuracy")
            val latitude = CursorHelper.getDouble(cursor , "latitude")
            val longitude = CursorHelper.getDouble(cursor , "longitude")
            val time = CursorHelper.getLong(cursor , "time")

            val traceLocation = TraceLocation()
            traceLocation.bearing = bearing
            traceLocation.speed = speed
            traceLocation.latitude = latitude
            traceLocation.longitude = longitude
            traceLocation.time = time

            return traceLocation
        }

        private fun latLngFromCursor(cursor: Cursor) : LatLng{
            val latitude = CursorHelper.getDouble(cursor , "latitude")
            val longitude = CursorHelper.getDouble(cursor , "longitude")
            return LatLng(latitude , longitude)
        }

        private const val DBNAME = "gpsinfo"

        fun createTable(db : SQLiteDatabase){
            DbTableHelper.fromTableName(DBNAME)
                .addColumn_Float("bearing")
                .addColumn_Float("speed")
                .addColumn_Float("accuracy")
                .addColumn_Double("latitude")
                .addColumn_Double("longitude")
                .addColumn_Long("time")
                .addColumn_Float("sportId")
                .buildTable(db)
        }

        fun dropTable(db : SQLiteDatabase){
            db.execSQL("drop table if exists $DBNAME")
        }

    }

}
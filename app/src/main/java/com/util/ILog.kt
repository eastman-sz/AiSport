package com.util

import android.util.Log

class ILog {

    companion object {

        private val logKey = "ilog"

        fun e(msg : String){
            Log.e(logKey , msg)
        }

        fun d(msg : String){
            Log.d(logKey , msg)
        }



    }

}
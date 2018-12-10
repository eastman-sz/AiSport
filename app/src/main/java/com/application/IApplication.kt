package com.application

import android.content.Context
import android.support.multidex.MultiDexApplication

class IApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {

        var context : Context ?= null

    }

}
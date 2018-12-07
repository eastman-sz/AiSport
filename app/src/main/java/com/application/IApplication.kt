package com.application

import android.app.Application
import android.content.Context

class IApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {

        var context : Context ?= null

    }

}
package com.application

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.smilefuns.sdk.ad.MobileAdsHelper
import com.utils.lib.ss.common.StrictModeHelper

class IApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = this

        StrictModeHelper.initStrictMode()
        //mobile
        MobileAdsHelper.init(this)
    }

    companion object {

        var context : Context ?= null

    }

}
package com.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

open class BaseAppCompatActivityActivity : AppCompatActivity() {

    var context : Context ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.context = this
    }
}

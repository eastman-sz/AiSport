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

    open fun initActivity(){
        init()
    }

    private fun init() {
        getIntentData()
        initTitle()
        initViews()
        initListener()
    }

    open fun getIntentData() {}

    /**
     * 初始化标题
     */
    open fun initTitle() {}

    /**
     * 初始化VIEW
     */
    open fun initViews() {}

    /**
     * 初始化Listener
     */
    open fun initListener() {}


}

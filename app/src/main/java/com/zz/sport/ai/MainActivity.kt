package com.zz.sport.ai

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amap.locationservicedemo.JniBroadActivity
import com.history.SportHistoryActivity
import com.sport.SportActivity
import com.util.ILog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private var context : Context ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        initListeners()
    }

    private fun initListeners(){
        sportBtn.setOnClickListener {
            startActivity<SportActivity>()
        }

        historyBtn.setOnClickListener {
            startActivity<SportHistoryActivity>()
        }

        jniBtn.setOnClickListener {
            startActivity<JniBroadActivity>()
        }
    }

    override fun onStop() {
        ILog.e("-----------页面----onStop-------------------")
        super.onStop()
    }

}

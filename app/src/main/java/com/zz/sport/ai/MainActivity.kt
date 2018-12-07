package com.zz.sport.ai

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amap.locationservicedemo.JniBroadActivity
import com.history.SportHistoryActivity
import com.sport.SportActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

}

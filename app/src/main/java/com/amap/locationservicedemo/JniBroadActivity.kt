package com.amap.locationservicedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.activity_jni_broad.*
import org.jetbrains.anko.startActivity

class JniBroadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni_broad)

        initListeners()
    }

    private fun initListeners(){
        jniBtn.setOnClickListener {
            startActivity<JniMainActivity>()
        }
        jniMapBtn.setOnClickListener {
            startActivity<JniMapActivity>()
        }
    }
}

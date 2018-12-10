package com.amap.locationservicedemo

import android.os.Bundle
import com.base.BaseAppCompatActivityActivity
import com.permission.OnLocationPermissionRequestListener
import com.permission.PermissionRequestHelper
import com.util.PermissionHelpler
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.activity_jni_broad.*
import org.jetbrains.anko.startActivity

class JniBroadActivity : BaseAppCompatActivityActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jni_broad)

        initListeners()

        PermissionHelpler.requestPermissions(this)
    }

    private fun initListeners(){
        jniBtn.setOnClickListener {
            startActivity<JniMainActivity>()
        }
        jniMapBtn.setOnClickListener {
            PermissionRequestHelper.requestSportPermission(context!! , object : OnLocationPermissionRequestListener(){
                override fun onGranted() {
                    startActivity<JniMapActivity>()
                }
            })
        }
    }
}

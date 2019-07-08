package com.start

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.base.BaseAppCompatActivityActivity
import com.zz.sport.ai.MainActivity
import com.zz.sport.ai.R
import org.jetbrains.anko.startActivity

class StartActivity : BaseAppCompatActivityActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

/*        Handler(Looper.getMainLooper()).postDelayed({

            startActivity<MainActivity>()

            finish()

        } , 250)*/

        startActivity<MainActivity>()
        finish()
    }








}

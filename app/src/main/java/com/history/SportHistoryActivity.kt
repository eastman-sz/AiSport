package com.history

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sportdata.SportInfo
import com.sportdata.SportInfoDbHelper
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.activity_sport_history.*
import org.jetbrains.anko.startActivity

class SportHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_history)
        title = "记录"

        initViews()
    }

    private fun initViews(){
        val list = ArrayList<SportInfo>()

        list.addAll(SportInfoDbHelper.getSports())

        val adapter = SportHistoryAdapter(this , list)

        listView.adapter = adapter

        adapter?.onCommonItemClickListener = object : OnCommonItemClickListener<SportInfo>{
            override fun onClick(it: SportInfo) {
                startActivity<SportDetailActivity>("sportId" to  it.sportId)
            }
        }
    }
}

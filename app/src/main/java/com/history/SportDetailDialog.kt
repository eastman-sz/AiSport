package com.history

import android.content.Context
import android.os.Bundle
import com.common.base.BasePagerAdapter
import com.common.dialog.BaseFullScreenDialog
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.sport_detail_dialog.*

class SportDetailDialog : BaseFullScreenDialog {

    val list = ArrayList<KmInfoView>()

    constructor(context: Context) : super(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sport_detail_dialog)

        init()
    }

    override fun initViews() {
        list.add(KmInfoView(context))
        val adapter = BasePagerAdapter<KmInfoView>(context, list)
        viewPager.adapter = adapter
    }

    fun setSportId(sportId : Long){
        list[0].setSportId(sportId)
    }

}
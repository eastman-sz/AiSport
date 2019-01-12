package com.history

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.base.BaseKotlinRelativeLayout
import com.sportdata.KmInfo
import com.sportdata.KmInfoDbHelper
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.km_info_view.view.*

class KmInfoView : BaseKotlinRelativeLayout {

    private var adapter : KmInfoAdapter ?= null
    private val list = ArrayList<KmInfo>()

    constructor(context: Context?) : super(context){init()}

    constructor(context: Context?, attrs : AttributeSet) : super(context , attrs){init()}

    override fun initViews() {
        View.inflate(context , R.layout.km_info_view , this)
        adapter = KmInfoAdapter(context , list)
        listView.adapter = adapter
    }

    fun setSportId(sportId : Long){
        val kmInfoList = KmInfoDbHelper.getKmInfos(sportId)
        list.clear()
        list.addAll(kmInfoList)
        adapter?.notifyDataSetChanged()
    }

}
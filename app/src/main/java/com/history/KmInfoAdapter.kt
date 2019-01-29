package com.history

import android.content.Context
import android.view.View
import com.common.base.CustomFontTextView
import com.common.base.IBaseAdapter
import com.common.base.ViewHolder
import com.sportdata.KmInfo
import com.util.DateUtil
import com.zz.sport.ai.R

class KmInfoAdapter : IBaseAdapter<KmInfo> {

    constructor(context: Context , list: List<KmInfo>) : super(context , list , R.layout.km_info_adapter)

    override fun getConvertView(convertView: View, list: List<KmInfo>, position: Int) {
        val kmSeqTextView = ViewHolder.getView<CustomFontTextView>(convertView , R.id.kmSeqTextView)
        val durationTextView = ViewHolder.getView<CustomFontTextView>(convertView , R.id.durationTextView)
        val stepsTextView = ViewHolder.getView<CustomFontTextView>(convertView , R.id.stepsTextView)

        val kmInfo = list[0]
        val steps = kmInfo.steps

        kmSeqTextView.text = kmInfo.km.toString().plus(" 公里")
        durationTextView.text = "用时: ".plus(DateUtil.fomatSpecialTime(kmInfo.duration.toLong()))
        stepsTextView.text = "步数: ".plus(steps.toString())

    }

}
package com.history

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import com.common.base.CustomFontDigitTextView
import com.common.base.CustomFontTextView
import com.common.base.IBaseAdapter
import com.common.base.ViewHolder
import com.common.swipe.SwipeMenuLayoutA
import com.sportdata.SportInfo
import com.utils.lib.ss.common.DateHepler
import com.utils.lib.ss.common.MathUtil
import com.zz.sport.ai.R

class SportHistoryAdapter : IBaseAdapter<SportInfo> {

    var onCommonAdapterClickListener : OnCommonAdapterClickListener<SportInfo> ?= null

    constructor(context: Context , list: List<SportInfo>) : super(context, list, R.layout.sport_history_adapter)

    override fun getConvertView(conterView: View, list: List<SportInfo>, position: Int) {
        val swipeMenuLayoutA = ViewHolder.getView<SwipeMenuLayoutA>(conterView , R.id.swipeMenuLayoutA)
        val infoLayout = ViewHolder.getView<RelativeLayout>(conterView , R.id.infoLayout)
        val infoTextView = ViewHolder.getView<CustomFontDigitTextView>(conterView , R.id.infoTextView)
        val distanceTextView = ViewHolder.getView<CustomFontDigitTextView>(conterView , R.id.distanceTextView)
        val delBtnTextView = ViewHolder.getView<CustomFontTextView>(conterView , R.id.delBtnTextView)

        val sportInfo = list[position]
        val startTime = sportInfo.startTime
        val endTime = sportInfo.endTime
        val duration = endTime - startTime
        val distance = sportInfo.distance

        val minute = duration/60
        val seconds = duration%60

        val timeText = (if (minute > 0) "${minute}分" else "").plus(if (seconds > 0) "${seconds}秒" else "")

        val info = DateHepler.timestampFormat(startTime , "yyyy年MM月dd日").plus("  ")
            .plus(DateHepler.timestampFormat(startTime, "HH:mm:ss")).plus(" ~ ")
            .plus(DateHepler.timestampFormat(endTime, "HH:mm:ss"))
            .plus("  ").plus(timeText)

        infoTextView.text = info
        distanceTextView.text = MathUtil.meter2KmF(distance).toString()

        infoLayout.setOnClickListener {
            onCommonAdapterClickListener?.onMainItemClick(sportInfo)
        }

        delBtnTextView.setOnClickListener {
            swipeMenuLayoutA.quickClose()
            onCommonAdapterClickListener?.onSubItemClick(position)
        }

    }
}
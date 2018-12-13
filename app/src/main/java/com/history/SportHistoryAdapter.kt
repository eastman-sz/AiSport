package com.history

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.common.base.CustomFontDigitTextView
import com.common.base.CustomFontTextView
import com.common.base.IBaseAdapter
import com.common.base.ViewHolder
import com.common.swipe.SwipeMenuLayoutA
import com.sportdata.SportInfo
import com.utils.lib.ss.common.DateHepler
import com.zz.sport.ai.R

class SportHistoryAdapter : IBaseAdapter<SportInfo> {

    var onCommonAdapterClickListener : OnCommonAdapterClickListener<SportInfo> ?= null

    constructor(context: Context , list: List<SportInfo>) : super(context, list, R.layout.sport_history_adapter)

    override fun getConvertView(conterView: View, list: List<SportInfo>, position: Int) {
        val swipeMenuLayoutA = ViewHolder.getView<SwipeMenuLayoutA>(conterView , R.id.swipeMenuLayoutA)
        val infoLayout = ViewHolder.getView<LinearLayout>(conterView , R.id.infoLayout)
        val infoTextView = ViewHolder.getView<CustomFontDigitTextView>(conterView , R.id.infoTextView)
        val delBtnTextView = ViewHolder.getView<CustomFontTextView>(conterView , R.id.delBtnTextView)

        val sportInfo = list[position]
        val startTime = sportInfo.startTime
        val endTime = sportInfo.endTime
        val duration = endTime - startTime

        val minute = duration/60
        val seconds = duration%60

        val timeText = (if (minute > 0) "${minute}分" else "").plus(if (seconds > 0) "${seconds}秒" else "")

        val info = DateHepler.timestampFormat(startTime , "yyyy年MM月dd日").plus("    ").plus(timeText).plus("\n")
            .plus(DateHepler.timestampFormat(startTime, "HH:mm:ss")).plus(" ~ ")
            .plus(DateHepler.timestampFormat(endTime, "HH:mm:ss"))

        infoTextView.text = info

        infoLayout.setOnClickListener {
            onCommonAdapterClickListener?.onMainItemClick(sportInfo)
        }

        delBtnTextView.setOnClickListener {
            swipeMenuLayoutA.quickClose()
            onCommonAdapterClickListener?.onSubItemClick(position)
        }

    }
}
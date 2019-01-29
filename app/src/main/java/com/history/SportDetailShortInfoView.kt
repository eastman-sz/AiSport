package com.history

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.base.BaseKotlinRelativeLayout
import com.sportdata.SportInfoDbHelper
import com.util.DateUtil
import com.utils.lib.ss.common.DateHepler
import com.utils.lib.ss.common.MathUtil
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.sport_detail_short_info_view.view.*

class SportDetailShortInfoView : BaseKotlinRelativeLayout {

    constructor(context: Context?) : super(context){init()}

    constructor(context: Context?, attrs : AttributeSet) : super(context , attrs){init()}

    override fun initViews() {
        View.inflate(context , R.layout.sport_detail_short_info_view , this)
    }

    fun setSportInfo(sportId : Long){
        val sportInfo = SportInfoDbHelper.getSports(sportId)
        val startTime = sportInfo.startTime
        val distance = sportInfo.distance
        val avgPace = sportInfo.avgPace
        val duration = sportInfo.endTime - sportInfo.startTime
        val calorie = sportInfo.calorie

        timeTextView.text = DateHepler.timestampFormat(startTime , "yyyy年MM月dd日 HH:mm")
        distanceTextView.text = if (0F == distance) "--" else MathUtil.meter2KmF(distance).toString()
        avgPaceTextView.text = DateUtil.seconds2RunningPace(avgPace)
        durationTextView.text = DateUtil.secondsFormatHours1(duration.toInt())
        calorieTextView.text = if (0 == calorie) "--" else calorie.toString()
    }


}
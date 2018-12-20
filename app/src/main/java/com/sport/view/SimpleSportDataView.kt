package com.sport.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.base.BaseKotlinRelativeLayout
import com.gaode.SportParam
import com.sportdata.SportInfoDbHelper
import com.util.DateUtil
import com.util.ILog
import com.utils.lib.ss.common.MathUtil
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.simple_sport_data_view.view.*
/**
 * 显示简单的实时运动数据.
 * @author E
 */
class SimpleSportDataView : BaseKotlinRelativeLayout {

    //应用是否处于前台显示
    var onAppForeground = true

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context? , attrs : AttributeSet) : super(context , attrs){
        init()
    }

    override fun initViews() {
        View.inflate(context , R.layout.simple_sport_data_view , this)
    }

    fun setDuration(duration : Int){
        if (onAppForeground){
            durationTextView.text = if (0 == duration) "--" else DateUtil.secondsFormatHours1(duration)
        }
    }

    fun setDistance(distance : Float){
        if (onAppForeground){
            distanceTextView.text = if (0F == distance) "--" else MathUtil.meter2KmF(distance).toString()
        }
    }

    fun setPace(pace : Int){
        if (onAppForeground){
            paceTextView.text = if (0 == pace) "--" else DateUtil.seconds2RunningPace(pace)
        }
    }

    fun setAppForeground(onAppForeground : Boolean){
        this.onAppForeground = onAppForeground

        val distance = SportInfoDbHelper.getSports(SportParam.sportId).distance

        ILog.e("-------恢复--------:: ${SportParam.sportId}  $distance")

        setDistance(distance)
    }

}
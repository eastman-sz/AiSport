package com.sportdata

import com.application.IApplication
import com.zz.sport.ai.R

class GpsPaceColorUtils {

    companion object {
        /**
         * 根据配速的快慢来标记对应的色值。
         */
        fun getPaceColor(pace: Int): Int {
            if (pace > 540) {//9分钟
                return IApplication.context!!.resources.getColor(R.color.path_color_c9)
            } else if (pace > 500) {//8分钟
                return IApplication.context!!.resources.getColor(R.color.path_color_c8)
            } else if (pace > 460) {//7分钟
                return IApplication.context!!.resources.getColor(R.color.path_color_c7)
            } else if (pace > 420) {//6分钟
                return IApplication.context!!.resources.getColor(R.color.path_color_c6)
            } else if (pace > 390) {//5分钟
                return IApplication.context!!.resources.getColor(R.color.path_color_c5)
            } else if (pace > 360) {//2分钟30秒
                return IApplication.context!!.resources.getColor(R.color.path_color_c4)
            } else if (pace > 330) {//2分钟30秒
                return IApplication.context!!.resources.getColor(R.color.path_color_c3)
            } else if (pace > 320) {//2分钟30秒
                return IApplication.context!!.resources.getColor(R.color.path_color_c2)
            } else if (pace > 300) {//2分钟30秒
                return IApplication.context!!.resources.getColor(R.color.path_color_c1)
            }
            return IApplication.context!!.resources.getColor(R.color.path_color_c1)
        }
    }


}
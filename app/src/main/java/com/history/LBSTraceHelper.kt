package com.history

import com.amap.api.maps.model.LatLng
import com.amap.api.trace.LBSTraceClient
import com.amap.api.trace.TraceListener
import com.amap.api.trace.TraceLocation
import com.application.IApplication
import com.util.ILog

class LBSTraceHelper {

    companion object {
        /**
         *
         */
        fun startTrace(traceLocations : List<TraceLocation> , onTraceListener : OnTraceListener?){
            val lBSTraceClient = LBSTraceClient.getInstance(IApplication.context)
            lBSTraceClient.queryProcessedTrace(1 , traceLocations ,LBSTraceClient.TYPE_AMAP ,  object : TraceListener{
                override fun onFinished(lineID: Int, linepoints: List<LatLng>, distance: Int, waitingtime: Int) {
                    onTraceListener?.onFinished(lineID , linepoints , distance , waitingtime)
                }
                override fun onRequestFailed(lineID: Int, errorInfo: String) {
                        ILog.e("-------轨迹纠正失败------------------:$errorInfo")
                }
                override fun onTraceProcessing(lineID: Int, index: Int, segments: List<LatLng>) {
                    ILog.e("-------轨迹纠正进行中------------------:$index  $lineID")
                }
            })
        }
    }

}
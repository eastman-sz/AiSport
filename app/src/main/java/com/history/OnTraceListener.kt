package com.history

import com.amap.api.maps.model.LatLng

interface OnTraceListener {

    fun onFinished(lineID: Int, linePoints: List<LatLng>, distance: Int, waitingtime: Int)


}
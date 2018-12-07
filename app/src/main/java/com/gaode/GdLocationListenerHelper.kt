package com.gaode

import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import java.lang.Exception

class GdLocationListenerHelper {

    private var totalDistance = 0f
    private val latLngList = ArrayList<LatLng>()

    var gdLocationChangeListener : OnGdLocationChangeListener ?= null

    constructor(){
        totalDistance = 0f
        latLngList.clear()
    }

    fun onLocationChanged(aMapLocation: AMapLocation){
        val errorCode = aMapLocation.errorCode
        if (errorCode != 0){
            return
        }
        if (aMapLocation.latitude == 0.0 || aMapLocation.longitude == 0.0){
            return
        }
        val accuracy = aMapLocation.accuracy
        if (!latLngList.isEmpty() && accuracy > SportParam.accuracy){
            return
        }
        try {
            val latLng = LatLng(aMapLocation.latitude , aMapLocation.longitude)

            if (latLngList.isEmpty()){
                //只有合乎标准的点才放入数组中，去计算两点的距离
                val isValid = accuracy <= SportParam.accuracy
                if (isValid){
                    latLngList.add(latLng)
                }
                onLocationChanged(if (isValid) LatLngState.NORMAL else LatLngState.FOR_LOC , aMapLocation , latLng , 0f)
                return
            }
            val equals = latLng == latLngList[0]
            if (equals){
                return
            }
            //位置变化后计算两点间的距离
            val distance = AMapUtils.calculateLineDistance(latLng, latLngList[0])
            if (distance == 0.0f) {//位置没有变化或者GPS点出现异常
                return
            }

            totalDistance += distance

            latLngList.clear()
            latLngList.add(latLng)

            onLocationChanged(LatLngState.NORMAL, aMapLocation, latLng, distance)

        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun onLocationChanged(latLngState: LatLngState, location: AMapLocation, latLng: LatLng, distancePerLatLng: Float){
        gdLocationChangeListener?.onLocationChange(latLngState , location , latLng , totalDistance , distancePerLatLng)
    }

}
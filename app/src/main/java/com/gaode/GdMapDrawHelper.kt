package com.gaode

import android.util.Log
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.application.IApplication
import com.zz.sport.ai.R
import java.util.ArrayList

class GdMapDrawHelper {

    private var aMap : AMap ?= null

    //用于画线或点，最多只保留两个位置点
    private val latLngs = ArrayList<LatLng>()
    //起点
    private var startPointOverlayOptions: MarkerOptions? = null
    //终点
    private var endPointOverlayOptions: MarkerOptions? = null
    private var startPointMarker: Marker? = null
    private var endPointMarker: Marker? = null
    //画线的色值
    private var polylineOptions: PolylineOptions? = null
    //收到的Gps点的个数，因为在地图上画点太多，会引起页面卡顿,控制在300个点为宜
    private var gpsPointCount = 0

    constructor(aMap : AMap){
        this.aMap = aMap

        latLngs.clear()

        init()
    }

    //isNormal 0正常 1，不正常 。只有在起点定位是才会返回不正常的点，所以不正常的点仅用来定位
    fun drawLine(latLng: LatLng, isNormal: Int) {
        if (1 == isNormal) {
            latLngs.clear()
        } else {
            gpsPointCount++

            if (1 == gpsPointCount) {
                //首次收到正常的GPS点时，将之前的异常点清空
                latLngs.clear()
            }
        }
        latLngs.add(latLng)
        val size = latLngs.size
        if (1 == size) {
            //画起点
            drawStartPoint(latLng)
            return
        }
        //最多只保留两个位置点
        if (size > 2) {
            latLngs.removeAt(0)
        }
        //两点可画线
        drawTraceLine(latLngs)
        drawEndPoint(latLng)
    }

    private fun init(){
        //地图设置
        val uiSettings = aMap?.uiSettings
        uiSettings?.isZoomControlsEnabled = false
//        aMap?.minZoomLevel = 12f
        aMap?.moveCamera(CameraUpdateFactory.zoomBy(10f))

        //起点
        val startDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.map_start_point_icon)
        startPointOverlayOptions = MarkerOptions().icon(startDescriptor).draggable(false)
        //终点
        val endDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.moving_end_point_icon)
        endPointOverlayOptions = MarkerOptions().icon(endDescriptor).draggable(false)
        //画线的色值
        val lineColor = IApplication.context?.resources!!.getColor(R.color.c18)
        polylineOptions = PolylineOptions().width(8f).color(lineColor)
    }

    //画起点
    private fun drawStartPoint(latLng: LatLng?) {
        if (null == latLng) {
            return
        }
        Log.e("sport" , "---------画起点----0--------------")
        try {
            if (null == startPointMarker) {
                startPointMarker = aMap?.addMarker(startPointOverlayOptions?.position(latLng))
            }
            startPointMarker?.position = latLng
            //定位
            reloc(latLng)
        } catch (e: Exception) {
        }
    }

    //画终点
    private fun drawEndPoint(latLng: LatLng) {
        if (null == aMap) {
            return
        }
        try {
            if (null == endPointMarker) {
                endPointMarker = aMap?.addMarker(endPointOverlayOptions?.position(latLng))
            }
            endPointMarker?.position = latLng

            //定位
            reloc(latLng)
        } catch (e: Exception) {
        }

    }

    //画线
    private fun drawTraceLine(latLngs: List<LatLng>) {
        if (null == aMap) {
            return
        }
        try {
//            if (gpsPointCount > SportParam.maxGpsPoints) {
//                //大于上限值时，需要清空地图上的点，不需要密集的画点
//                clearAndRedrawMapGps()
//            }

            polylineOptions?.points = latLngs
            aMap?.addPolyline(polylineOptions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //定位到某个点
    private fun reloc(latLng: LatLng) {
        if (null == aMap) {
            return
        }
        try {
            aMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}
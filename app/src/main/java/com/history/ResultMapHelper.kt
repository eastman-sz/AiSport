package com.history

import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.amap.api.maps.utils.SpatialRelationUtil
import com.amap.api.maps.utils.overlay.SmoothMoveMarker
import com.application.IApplication
import com.utils.lib.ss.info.DeviceInfo
import com.zz.sport.ai.R

class ResultMapHelper {

    private var mapView : MapView ?= null
    private var aMap : AMap ?= null

    constructor(mapView : MapView){
        this.mapView = mapView
        this.aMap = mapView.map
    }

    fun setPoints(points: List<LatLng>){
        if (points.size < 2){
            return
        }
        val ooPolyline = PolylineOptions().width(DeviceInfo.dip2px(IApplication.context, 5f).toFloat())
            .addAll(points)
            .color(IApplication.context!!.resources.getColor(R.color.c18))
            .useGradient(true)
            .zIndex(10f)

        aMap?.addPolyline(ooPolyline)
        //画起点
        setStartPoint(points[0])
        //画终点
        setEndPoint(points[points.size - 1])
        //地理围栏
        newLatLngBounds(points)
        //平滑移动
//        smoothMove(points)
    }

    private fun setStartPoint(latLng: LatLng) {
        val endDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.map_start_point_icon)
        val ooA = MarkerOptions().position(latLng).icon(endDescriptor)
            .draggable(false)
        aMap?.addMarker(ooA)
    }

    private fun setEndPoint(latLng: LatLng) {
        val endDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.map_end_point_icon)
        val ooA = MarkerOptions().position(latLng).icon(endDescriptor)
            .draggable(false)
        aMap?.addMarker(ooA)
    }

    //地理围栏
    private fun newLatLngBounds(points: List<LatLng>){
        val builder = LatLngBounds.Builder()
        points.forEach {
            builder.include(it)
        }
        val latLngBounds = builder.build()
        val mapStatusUpdate = CameraUpdateFactory.newLatLngBounds(
            latLngBounds, mapView!!.width, mapView!!.height, 50
        )
        aMap?.moveCamera(mapStatusUpdate)
    }

    //平滑移到
    private fun smoothMove(points: List<LatLng>){
        // 实例 SmoothMoveMarker 对象
        val smoothMarker = SmoothMoveMarker(aMap)
//        // 设置 平滑移动的 图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.map_end_point_icon))

        //        // 取轨迹点的第一个点 作为 平滑移动的启动
        val drivePoint = points[0]
        val pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint)
//        points.set(pair.first, drivePoint)
        val subList = points.subList(pair.first, points.size)

        // 设置轨迹点
        smoothMarker.setPoints(subList)
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(10)
        smoothMarker.setMoveListener { distance ->
            //到终点后消失
            if (distance < 0.03) {
                smoothMarker.setVisible(false)
            }
        }
        //开始移动
        smoothMarker.startSmoothMove()
    }




}
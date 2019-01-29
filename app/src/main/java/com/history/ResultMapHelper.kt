package com.history

import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.amap.api.maps.utils.SpatialRelationUtil
import com.amap.api.maps.utils.overlay.SmoothMoveMarker
import com.application.IApplication
import com.sportdata.GpsInfo
import com.sportdata.GpsPaceColorUtils
import com.util.ILog
import com.utils.lib.ss.info.DeviceInfo
import com.zz.sport.ai.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

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

//        val ooPolyline = polylineOptions(points)
//        aMap?.addPolyline(ooPolyline)
        //画起点
        setStartPoint(points[0])
        //画终点
        setEndPoint(points[points.size - 1])
        //地理围栏
        newLatLngBounds(points)
        //平滑移动
//        smoothMove(points)
    }

    fun setPointsN(points : List<GpsInfo>){
        if (points.size < 2){
            return
        }
        val list = ArrayList<LatLng>()
        points.forEach {
            list.add(it.newLatLng())
        }

        setPoints(list)

        //将异常线画为虚线
        newLines(points)
    }

    //将异常线画为虚线
    //两点之间时长过长，判定为异常点，两点之间画虚线
    private fun newLines(points : List<GpsInfo>){
        doAsync {
            //异常Gps点
            val list = ArrayList<List<LatLng>>()
            var subList = ArrayList<LatLng>()

            //正常Gps点
            val list0 = LinkedList<List<GpsInfo>>()
            var subList0 = LinkedList<GpsInfo>()

            val size = points.size -1
            for (i in 0 until size){
                val f = points[i].time
                val s = points[i + 1].time
                val timeDiff = s - f

                ILog.e("----------两点之间的时间差------------: $timeDiff")

                if (timeDiff > 40000){
                    if (subList.isEmpty()){
                        subList.add(points[i].newLatLng())
                        subList.add(points[i + 1].newLatLng())
                    }else{
                        subList.add(points[i + 1].newLatLng())
                    }

                    //正常Gps点
                    if (!subList0.isEmpty()){
                        list0.add(subList0)
                        subList0 = LinkedList()
                    }
                }else{
                    if (!subList.isEmpty()){
                        list.add(subList)
                        subList = ArrayList()
                    }

                    //正常Gps点
                    if (subList0.isEmpty()){
                        subList0.add(points[i])
                        subList0.add(points[i + 1])
                    }else{
                        subList0.add(points[i + 1])
                    }

                }
            }
            list.add(subList)
            list0.add(subList0)

            ILog.e("----------两点之间的时间差-O-----------: ${list.size}")

            uiThread {
                list.forEach {
                    val ooPolyline = newPolylineOptions(it)
                    aMap?.addPolyline(ooPolyline)
                }

                //正常Gps点
                list0.forEach {
                    if (it.size >= 2){
                        val colorValues = colorValues(it)

                        val points = ArrayList<LatLng>()
                        it.forEach {
                            points.add(it.newLatLng())
                        }

                        val ooPolyline = polylineOptions(points , colorValues)
                        aMap?.addPolyline(ooPolyline)
                    }

                    ILog.e("----------正常点-O-----------: ${it.size}")
                }
            }
        }
    }

    //正常Gps点边线
    private fun polylineOptions(points: List<LatLng> , colorValues : List<Int>) : PolylineOptions{
        return  PolylineOptions().width(DeviceInfo.dip2px(IApplication.context, 5f).toFloat())
                .addAll(points)
                .colorValues(colorValues)
                .color(IApplication.context!!.resources.getColor(R.color.c18))
                .useGradient(true)
                .zIndex(10f)
    }

    //异常Gps点边线
    private fun newPolylineOptions(points: List<LatLng>) : PolylineOptions{
        return PolylineOptions().width(DeviceInfo.dip2px(IApplication.context, 5f).toFloat())
               .addAll(points)
               .color(IApplication.context!!.resources.getColor(R.color.sfs_c2))
               .useGradient(true)
               .setDottedLine(true)
               .zIndex(10f)
    }

    //colorValues点对应的色值
    private fun colorValues(gpsInfos : List<GpsInfo>) : List<Int>{
        val colorValues = ArrayList<Int>()
        gpsInfos.forEach {
            val pace = it.pace
            val color = GpsPaceColorUtils.getPaceColor(pace)
            colorValues.add(color)
        }
        return colorValues
    }

    //画起点
    private fun setStartPoint(latLng: LatLng) {
        val endDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.map_start_point_icon)
        val ooA = MarkerOptions().position(latLng).icon(endDescriptor)
            .draggable(false)
        aMap?.addMarker(ooA)
    }

    //画终点
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
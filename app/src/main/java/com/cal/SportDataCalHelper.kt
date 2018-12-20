package com.cal

import com.amap.api.maps.model.LatLng
import com.amap.locationservicedemo.SportBroadcastHelper
import com.sportdata.KmInfoDbHelper
import java.util.*

class SportDataCalHelper {

    constructor()

    private var duration = 0
    private var distance = 0.0
    //配速:一公里把需要的时间
    private var pace = 0
    //上一公里时所花费的时长
    private var lastKmDuration = 0
    //上一公里计算时的距离(有可能公里不是连续的)
    private var lastKmDistance = 0.0
    //第几公里
    private var nextKm = 1
    private var latLng : LatLng?= null
    //总步数
    private var totalSteps = 0
    //步频(1分钟走了多少步)
    private var pitch = 0
    //用一个集合收集最近60秒内的数据
    private val stepList = LinkedList<Int>()
    //上一秒为止时的总步数
    private var lastSecondTotalSteps = 0
    //上一公里为止的总步数
    private var lastKmSteps = 0

    fun onDurationChange(newDuration : Int){
        this.duration = newDuration

        calPitch()
    }

    fun onDistanceChange(newDistance : Double , latLng : LatLng?){
        this.distance = newDistance
        this.latLng = latLng

        calPace()
    }

    fun onStepsChange(steps : Int){
        totalSteps += steps
    }

    //计算配速
    private fun calPace(){
        val kmMeters = nextKm*1000
        if (distance >= kmMeters){
            val curKm = (distance/kmMeters).toInt()
            nextKm = curKm + 1

            //当前一公时的时长
            val curKmDuration = duration - lastKmDuration
            lastKmDuration = duration
            pace = curKmDuration
            //标记公里
            lastKmDistance = distance

            //计算步数
            val curKmSteps = totalSteps - lastKmSteps
            lastKmSteps = totalSteps

            //更新DB
            KmInfoDbHelper.save(curKm , curKmDuration , latLng?.latitude!! , latLng?.longitude!! , curKmSteps)
        }else{
            //不足一公里时，计算配速
            val curDuration = duration - lastKmDuration
            if (curDuration < 2){
                return
            }
            val curDistance = distance - lastKmDistance
            if (curDistance < 5){
                //小于某个阀值时不计算
                return
            }
            //当前配速
            pace = ((1000*curDuration)/curDistance).toInt()
        }

    }

    //计算步频//每2秒钟计算一次
    private fun calPitch(){
        val curSecondSteps = totalSteps - lastSecondTotalSteps
        lastSecondTotalSteps = totalSteps
        //保存
        stepList.add(curSecondSteps)
        if (stepList.size > 60){
            stepList.removeAt(0)
        }
        if (0 == duration%2){
            //Send broadcast
            SportBroadcastHelper.sendPace(pace)

            val size = stepList.size
            if (size == 60){
                pitch = getTotalSteps()

            }else{
                val curSteps = getTotalSteps()
                pitch = (60*curSteps)/size

            }

            if (0 == duration%60){
                //一分钟

            }
            //Send broadcast
            SportBroadcastHelper.sendCurPitch(pitch)
        }

    }

    private fun getTotalSteps() : Int{
        var steps = 0
        stepList.forEach {
            steps += it
        }
        return steps
    }




}
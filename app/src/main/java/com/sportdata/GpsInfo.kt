package com.sportdata

import com.amap.api.maps.model.LatLng
/**
 * Gps point info
 */
class GpsInfo {

    var bearing = 0f
    var speed = 0f
    var accuracy = 0f
    var latitude = 0.0
    var longitude = 0.0
    var time = 0L
    var pace = 0

    fun newLatLng() : LatLng{
        return LatLng(latitude , longitude)
    }

}
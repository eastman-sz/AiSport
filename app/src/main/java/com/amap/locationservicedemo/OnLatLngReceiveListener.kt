package com.amap.locationservicedemo

import com.amap.api.maps.model.LatLng

interface OnLatLngReceiveListener {

    fun onReceive(latLng : LatLng)

}
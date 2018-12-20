package com.amap.locationservicedemo

import com.amap.api.maps.model.LatLng

open class OnLatLngReceiveListener {

    open fun onReceive(latLng : LatLng , distance : Float){}

    open fun onDurationChg(duration: Int){}

    open fun onPitchChg(pitch : Int){}

    open fun onPaceChg(pace : Int){}
}
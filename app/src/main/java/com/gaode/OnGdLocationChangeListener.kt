package com.gaode

import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.LatLng

interface OnGdLocationChangeListener {

    fun onLocationChange(latLngState: LatLngState, location: AMapLocation, latLng: LatLng, totalDistance: Float, distancePerLatLng: Float)

}
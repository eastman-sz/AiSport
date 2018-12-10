package com.permission

open interface OnPermissionRequestListener {

    fun onGranted()

    fun onDenied()

}
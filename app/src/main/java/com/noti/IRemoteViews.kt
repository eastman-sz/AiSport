package com.noti

import android.widget.RemoteViews
import com.application.IApplication
import com.zz.sport.ai.R

class IRemoteViews : RemoteViews {

    constructor() : super(IApplication.context?.packageName , R.layout.i_remote_views){
    }

}
package com.history

import android.content.Context
import android.view.View
import com.common.base.IBaseAdapter
import com.sportdata.KmInfo
import com.zz.sport.ai.R

class KmInfoAdapter : IBaseAdapter<KmInfo> {

    constructor(context: Context , list: List<KmInfo>) : super(context , list , R.layout.km_info_adapter)

    override fun getConvertView(convertView: View, list: List<KmInfo>, position: Int) {

    }

}
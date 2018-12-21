package com.history

import android.content.Context
import android.os.Bundle
import com.common.dialog.BaseFullScreenDialog
import com.zz.sport.ai.R

class SportDetailDialog : BaseFullScreenDialog {

    constructor(context: Context) : super(context){
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sport_detail_dialog)
    }

    fun setSportId(sportId : Long){

    }


}
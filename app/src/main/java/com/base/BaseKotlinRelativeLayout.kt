package com.base

import android.content.Context
import android.util.AttributeSet
import com.common.base.BaseRelativeLayout
/**
 * Created by E on 2018/2/6.
 */
open class BaseKotlinRelativeLayout : BaseRelativeLayout {

    constructor(context: Context?) : super(context)

    constructor(context: Context? , attrs : AttributeSet) : super(context , attrs)

}
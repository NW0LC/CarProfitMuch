package com.exz.carprofitmuch.bean

import android.content.Context
import com.exz.carprofitmuch.R

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
class ExpressBean{
    var expressId=""
    var expressName=""
    var expressPrice=""
    var isSelect=false
    fun toString(context: Context): String = expressName+expressPrice+context.getString(R.string.YUAN)
}
package com.exz.carprofitmuch.bean

import com.exz.carprofitmuch.R
import com.vilyever.resource.Resource

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
class ExpressBean(var expressName: String = "") :KeyAndValueBean() {
    override fun absKey(): String = expressId

    override fun absValue(): String =expressPrice
    var expressId=""
    var expressPrice=""
    override  fun toString(): String = expressName+expressPrice+ Resource.getString(R.string.YUAN)
}
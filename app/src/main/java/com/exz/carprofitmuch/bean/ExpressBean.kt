package com.exz.carprofitmuch.bean

import com.exz.carprofitmuch.R
import com.vilyever.resource.Resource

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
class ExpressBean(private var transportName: String = "", var transportMoney: String = "") : KeyAndValueBean() {
    override fun absKey(): String = transportId

    override fun absValue(): String = transportMoney
    var transportId = ""
    override fun toString(): String =
            if ((transportMoney.toDoubleOrNull()?:0.toDouble())!= 0.toDouble()) {
                transportName + transportMoney + Resource.getString(R.string.YUAN)
            } else {
                Resource.getString(R.string.goods_confirm_express_item)
            }
}
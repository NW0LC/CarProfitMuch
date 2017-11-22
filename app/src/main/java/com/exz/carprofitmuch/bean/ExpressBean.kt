package com.exz.carprofitmuch.bean

import com.exz.carprofitmuch.R
import com.vilyever.resource.Resource

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
class ExpressBean(private var expressName: String = "", var expressPrice: String = "") :KeyAndValueBean() {
    override fun absKey(): String = expressId

    override fun absValue(): String =expressPrice
    var expressId=""
    override  fun toString(): String = if (expressPrice.isNotEmpty()){expressName+expressPrice+ Resource.getString(R.string.YUAN)}else{
        Resource.getString(R.string.goods_confirm_express_item)
    }
}
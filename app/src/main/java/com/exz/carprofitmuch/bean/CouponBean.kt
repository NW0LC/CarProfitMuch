package com.exz.carprofitmuch.bean

import com.exz.carprofitmuch.R
import com.vilyever.resource.Resource

/**
 * Created by 史忠文
 * on 2017/4/26.
 */

open class CouponBean : KeyAndValueBean() {
    override fun absKey(): String = couponId

    override fun absValue(): String =discount
//    "couponId":"优惠劵id"
//    "discount":"10"
//    "limitMoney":"200"
//    "beginDate":"2016.01.25"
//    "invalidDate":"2016.02.25"


    var couponId = ""
    var discount = ""
    var limitMoney = ""
    var beginDate = ""
    var invalidDate = ""

    var receivedCount: String= ""//红包属性
    var surplusCount: String= ""//红包属性
    var isGet: String= ""//红包属性
    override fun toString(): String = if (discount.isNotEmpty()||discount.toDoubleOrNull()==0.toDouble()){String.format(Resource.getString(R.string.coupon_toString),limitMoney,discount)}else Resource.getString(R.string.goods_confirm_coupon_item)




}

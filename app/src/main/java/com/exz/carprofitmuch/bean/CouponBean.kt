package com.exz.carprofitmuch.bean

import com.exz.carprofitmuch.R
import com.vilyever.resource.Resource

/**
 * Created by 史忠文
 * on 2017/4/26.
 */

open class CouponBean(var couponName: String = "") : KeyAndValueBean() {
    override fun absKey(): String = couponId

    override fun absValue(): String =couponPrice

    /**
     * couponPrice :
     * couponInfo : 满多少可用
     * couponFullPrice
     * couponTime : 有效期
     * couponState :
     * couponName :
     * receivedCount :"已领取"数量
     * receivedCount :"已领取"数量
     * surplusCount :"剩余" 数量
     * isGet :是否领取过 0未领取，1 去使用
     * typeId :"商品类型id"
     */

    var couponId = ""
    var couponPrice = ""
    var couponFullPrice = ""
    var couponInfo = "不使用"
    var couponTime = ""
    var couponState: String= ""
    var receivedCount: String= ""
    var surplusCount: String= ""
    var isGet: String= ""
    var typeId: String= ""
    override fun toString(): String = String.format(Resource.getString(R.string.coupon_toString),couponPrice,couponName)
}

package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/4/26.
 */

open class CouponBean (){
    /**
     * couponPrice :
     * couponInfo : 满多少可用
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
    var couponInfo = "不使用"
    var couponTime = ""
    var couponState: String= ""
    var couponName: String= ""
    var receivedCount: String= ""
    var surplusCount: String= ""
    var isGet: String= ""
    var typeId: String= ""
    var isSelect = false
}

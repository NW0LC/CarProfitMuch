package com.exz.carprofitmuch.bean

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
open class GoodsConfirmSubBean {
    var shopId="0"
    var couponId="0"
    var discount=""
    var transportMoney=""
    var transportId="0"

    var shopName=""
    var goodsCount=""
    var isShowExpress=false
    var isShowCoupon=false
    var sendWay=""
    var coupon=""
    var couponInfo=ArrayList<CouponBean>()
    var transportInfo=ArrayList<ExpressBean>()
    var buyerMsg=""

    var totalPrice=0.toDouble()
    var goodsInfo=ArrayList<GoodsBean>()

}
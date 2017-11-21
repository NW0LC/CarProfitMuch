package com.exz.carprofitmuch.bean

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
open class GoodsConfirmSubBean {
    var goodsShopId=""
    var couponId=""
    var expressId=""

    var goodsShopName="鱼干店"
    var goodsCount="2"
    var isShowExpress=false
    var isShowCoupon=false
    var sendWay=""
    var coupon=""
    var goodsCoupons=ArrayList<CouponBean>()
    var sendWays=ArrayList<ExpressBean>()
    var msg=""

    var totalPrice=0.toDouble()
    var goods=ArrayList<GoodsBean>()

}
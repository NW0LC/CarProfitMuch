package com.exz.carprofitmuch.bean

import com.exz.carprofitmuch.adapter.GoodsConfirmScoreBean

/**
 * Created by 史忠文
 * on 2017/12/5.
 */

class ServiceConfirmBean {
    /**
     * address : {}
     * shopInfo : {"shopId":"店铺id","goodsInfo":{}}
     */

    var totalPrice=0.toDouble()
    var scores=0.toDouble()
    var scoreInfo: GoodsConfirmScoreBean? = null
    val couponInfo: ArrayList<CouponBean>? = null
    var shopInfo: ServiceConfirmShopBean? = null

    class ServiceConfirmShopBean {
        /**
         * shopId : 店铺id
         * goodsInfo : {}
         */

        var shopId: String? = null
        var goodsInfo: ServiceGoodsBean? = null

    }
}

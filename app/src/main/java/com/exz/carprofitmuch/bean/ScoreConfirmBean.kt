package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/12/5.
 */

class ScoreConfirmBean {
    /**
     * address : {}
     * shopInfo : {"shopId":"店铺id","goodsInfo":{}}
     */

    var totalScore=0.toDouble()
    var score=0.toDouble()
    var address: AddressBean? = null
    var shopInfo: ScoreShopBean? = null

    class ScoreShopBean {
        /**
         * shopId : 店铺id
         * goodsInfo : {}
         */

        var shopId: String? = null
        var goodsInfo: GoodsBean? = null

    }
}

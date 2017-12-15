package com.exz.carprofitmuch.bean

/**
 * Created by pc on 2017/12/1.
 */

open class FootprintBean {

    /**
     * sectionId : 1
     * sectionName : 10月12号
     * goodsList : [{"goodsId":"商品id","goodsName":"商品名称","imgUrl":"商品图片地址","goodsPrice":"商品价格","payMark":"1"}]
     */

    var sectionId: String=""
    var sectionName: String=""
    var goodsList=ArrayList<GoodsBean>()

}

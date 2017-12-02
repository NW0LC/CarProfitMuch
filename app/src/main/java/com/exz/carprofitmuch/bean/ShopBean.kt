package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/12/1.
 */

class ShopBean {
    /**
     * shopId : 店铺id
     * shopName : 店铺名称
     * shopImgUrl : 店铺图片地址
     * shopPhone : 店铺电话
     * shopMark : 自营店铺
     * collectedCount : 被关注数量
     * isCollected : 0:未关注、1:已关注
     * allCount : 全部商品数
     * newCount : 上新商品数
     * hotCount : 热销数
     * recommendGoods : [{"goodsId":"1","imgUrl":"1.png"},{"goodsId":"1","imgUrl":"1.png"}]
     * newGoodsList : [{"goodsId":"商品id","imgUrl":"商品图片,地址","goodsName":"商品名称","goodsPrice":"商品价格","oldPrice":"商品价格","payMark":"1"}]
     * hotGoodsList : [{"goodsId":"商品id","imgUrl":"商品图片地址","goodsName":"商品名称","goodsPrice":"商品价格","oldPrice":"商品价格","payMark":"1"}]
     */

    var shopId: String=""
    var shopName: String=""
    var shopImgUrl: String=""
    var shopPhone: String=""
    var shopMark: String=""
    var collectedCount: String=""
    var isCollected: String=""
    var allCount: String=""
    var newCount: String=""
    var hotCount: String=""
    var recommendGoods= ArrayList<GoodsBean>()
    var newGoodsList= ArrayList<GoodsBean>()
    var hotGoodsList= ArrayList<GoodsBean>()
    
}

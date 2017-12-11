package com.exz.carprofitmuch.bean

import java.net.URLEncoder

/**
 * Created by pc on 2017/11/17.
 */

class GoodsOrderCommentBean {
    /**
     * goodsId : 商品id
     * payMark : 1
     * goodsName : 商品名称
     * imgUrl : 商品图片
     * goodsPrice : 100
     * count : 2
     * skuid : 规格id
     * goodsType : 规格内容
     */
    var photos = ArrayList<String>()
    var goodsId: String = ""
    var payMark: String = ""
    var goodsName: String = ""
    var imgUrl: String = ""
    var goodsPrice: String = ""
    var count: String = ""
    var skuid: String = ""

    var score = "5"
    var content = ""
        get() = URLEncoder.encode(field, "utf-8")
    var imgUrls = ArrayList<String>()
}

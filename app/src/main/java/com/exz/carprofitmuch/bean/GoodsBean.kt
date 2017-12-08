package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/21.
 */
open class GoodsBean : AbsMark(), AbsBanner {
    override fun getPayM() = payMark
    override fun getImgUri() = imgUrl
    override fun getMarkId() = goodsId
//            "goodsId":"商品id"
//            "payMark":"1",
//            "goodsName":"商品名称"
//            "goodsPrice":"商品价格"(价格区间)
//            "oldPrice":"商品原价"(价格区间)
//            "expressPrice":"快递价格"
//            "saleCount":"销量"
//            "address":"所在地"
//            "score":"赠送积分"
//            "isCoupon":"0"
//            "isCollected":"0:未关注、1:已关注"
//            "mainImgs":["banner图地址1","banner图地址",...]
//            "allStock":"总库存"
//            "isHaveRank":"0否 1是"
//            "isDelete":"0"
//            "commentCount":"评论数量"
//            "commentList":[{  //最多两条
//                 "userIcon":"http://123.png"
//                 "userName":"评论者名称"
//                 "score":"评分"
//                 "content":"评论内容"
//                 "images":["image1","image1",..."],
//                 "commentDate":"评价时间"
//                 "goodsRank":"商品规格"
//            },...]
//            "shopId":"店铺id"
//
//
//
//
//
//


    var imgUrl = ""//商品属性
    var goodsId: String = ""  //商品属性
    var payMark = ""//商品属性
    var goodsName = ""//商品属性
    var goodsPrice: String = ""//商品属性
    var oldPrice = ""//商品属性
    var expressPrice = ""//商品属性
    var saleCount = ""//商品属性
    var address = ""//商品属性
    var score = ""//商品属性
    var isCoupon = ""//商品属性
    var isCollected = ""//商品属性
    var mainImgs = ArrayList<String>()//商品属性
    var allStock = ""//商品属性
    var isHaveRank = ""//商品属性
    var isDelete = ""//商品属性
    var commentCount = ""//商品属性
    var commentList = ArrayList<CommentBean>()//商品属性
    var shopId = ""//商品属性

    var shopCarId: String = ""//商品购物车属性
    var goodsType: String = ""//商品购物车属性

    var scorePrice = ""//积分商品属性
    var goodsCount = ""
        get() {
            if (field.isEmpty()) {
                field = count
            }
            return field
        }
    var isCheck = false

    var goodsCollectionPriceChangeInfo = "goodsCollectionPriceChangeInfo"


    var id = ""//积分商品属性
    var title = ""//积分商品属性
    var price = ""//积分商品属性
    var subTitle = ""//积分商品属性

    var date: String = "" //历史足迹属性


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

    var count: String = ""
    var skuid: String = ""
    var downPrice: String = ""
    var state: String = ""


}
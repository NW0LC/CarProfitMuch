package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/25.
 */
open class ServiceShopBean(var goodsList: ArrayList<ServiceGoodsBean> = ArrayList()) {
//    "shopId":"店铺id"
//    "shopName":"店铺名称"
//    "shopImgUrl":"http://123.png"
//    "shopPhone":"12345678909"
//    "shopMark":"自营店铺"
//    "shopAddress":"淮海西路120号"
//    "isCoupon":"0"
//    "longitude":"店铺所在经度"
//    "latitude":"店铺所在纬度"
//    "score":"4"
//    "goodsCount":"20"
//    "goodsList": [{
//        "goodsId":"商品id"
//        "goodsName":"商品名称"
//        "goodsPrice":"8.9"
//        "oldPrice":"9.9"
//        "saleCount":"1.3万"
//        "payMark":"1",
//    },...]
//    "commentCount":"评论数量"
//    "commentList":[{  //最多两条
//        "userIcon":"http://123.png"
//        "userName":"评论者名称"
//        "score":"评分"
//        "content":"评论内容"
//        "images":["image1","image1",..."],
//        "commentDate":"评价时间"
//        "goodsRank":"商品规格"
//    },...]
//}


    var shopId = ""
    var shopName = "车享家汽车生活馆"
    var shopIcon = ""
    var shopImgUrl = ""
    var shopPhone = ""
    var shopMark = ""
    var shopAddress = ""
    var isCoupon = ""
    var longitude = ""
    var latitude = ""
    var goodsCount = ""
    var commentCount = ""
    var commentList = ArrayList<CommentBean>()
    var categoryName = "汽车美容"
    var score = "0"
    var distance = ""
    var district = ""
}
package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/25.
 */
open class ServiceGoodsBean {
//    "goodsId":"商品id"
//    "payMark":"1",
//    "goodsName":"商品名称"
//    "subTitle":"副标题"
//    "mainImgs":["banner图地址1","banner图地址",...]
//    "goodsPrice":"商品价格"(价格区间)
//    "oldPrice":"商品原价"(价格区间)
//    "saleCount":"销量"
//    "allStock":"总库存"
//    "shopInfo":{
//        "shopId":"店铺id"
//        "shopName":"店铺名称"
//        "shopAddress":"淮海西路120号"
//        "shopPhone":"12345678909"
//        "longitude":"店铺所在经度"
//        "latitude":"店铺所在纬度"
//        "distance":"3.2km"
//    },
//    "isDelete":"0"
//    "commentCount":"评论数量"
//    "commentAverage":"评论平均分"
//    "commentList":[{  //最多两条
//        "userIcon":"http://123.png"
//        "userName":"评论者名称"
//        "score":"评分"
//        "content":"评论内容"
//        "images":["image1","image1",..."],
//        "commentDate":"评价时间"
//    },...]
//    "goodsList": [{
//        "goodsId":"商品id"
//        "goodsName":"商品名称"
//        "goodsPrice":"8.9"
//        "oldPrice":"9.9"
//        "saleCount":"1.3万"
//        "payMark":"1",
//    },...]
//}


    var goodsId = ""
    var goodsPrice = ""
    var oldPrice = ""
    var img = ""
    var goodsName = ""
    var saleCount = ""
    var payMark = ""
    var count = ""
    var goodsCount = ""
    var subTitle = ""
    var allStock = ""
    var isDelete = ""
    var commentCount = ""
    var commentAverage = ""
    var mainImgs = ArrayList<String>()
    var shopInfo = ServiceShopBean()
    var commentList = ArrayList<CommentBean>()
    var goodsList = ArrayList<ServiceGoodsBean>()



}
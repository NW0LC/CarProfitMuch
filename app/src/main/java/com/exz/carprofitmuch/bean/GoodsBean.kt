package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/21.
 */
open class GoodsBean(var goodsId: String = "", var getGoodsCarId: String = "0", var date: String = "12月12", var goodsPrice: String = "25.00", var goodsType: String = "1") {
//    "goodsId":"1",
//    "imgUrl":"http://123.png",
//    "goodsName":"商品名称"
//    "goodsPrice":"25.00",
//    "oldPrice":"98.00"
//    "payMark":"1"



    var imgUrl="https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg"
    var goodsName="吃撑迷你车载垃圾桶汽车"
    var title="加油卡充值500元"
    var price="加油卡充值500元"
    var subTitle="加油卡充值500元"
    var oldPrice="98.00"
    var scorePrice="25.00"
    var goodsInventory="10"
    var goodsCount="1"
    var goodsChooseClassify="蓝色 草莓味 500g 饼干"
    var isCheck=false
    var goodsCollectionPriceChangeInfo="goodsCollectionPriceChangeInfo"
    var goodsBanner=ArrayList<BannersBean>()
    var goodsClassify=ArrayList<GoodsClassifyBean>()
    var goodsClassifyPool=ArrayList<GoodsClassifyPoolBean>()

}
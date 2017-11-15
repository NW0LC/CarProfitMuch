package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/21.
 */
open class GoodsBean(var id: String = "", var date: String = "12月12") {
    var img="https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg"
    var title="吃撑迷你车载垃圾桶汽车"
    var info="加油卡充值500元"
    var oldPrice="98.00"
    var price="25.00"
    var scorePrice="25.00"
    var goodsInventory="10"
    var goodsType="1"
    var goodsCount="1"
    var isCheck=false
    var goodsCollectionPriceChangeInfo="goodsCollectionPriceChangeInfo"
    var goodsBanner=ArrayList<BannersBean>()
    var goodsClassify=ArrayList<GoodsClassifyBean>()
    var goodsClassifyPool=ArrayList<GoodsClassifyPoolBean>()


}
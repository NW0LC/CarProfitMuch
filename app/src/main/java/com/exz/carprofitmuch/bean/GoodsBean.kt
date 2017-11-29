package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/21.
 */
open class GoodsBean(var id: String = "", var getGoodsCarId: String = "0", var date: String = "12月12", var price: String = "25.00", var goodsType: String = "1") {

    var img="https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg"
    var title="吃撑迷你车载垃圾桶汽车"
    var info="加油卡充值500元"
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

    private var goodsId: String = ""
    private var payMark: String = ""
    private var goodsName: String = ""
    private var imgUrl: String = ""
    private var goodsPrice: String = ""
    private var count: String = ""
    private var skuid: String = ""



}
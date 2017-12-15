package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/9.
 */
open class ServiceOrderBean {
//    "orderId":"1"
//    "orderState":"订单状态"
//    "shopId":"1"
//    "shopPhone":"店铺联系电话"
//    "shopName":"店铺名称"
//    "ticketDate":"券码有效期",
//    "isEvaluation":"是否评价",
//    "payMark":"1",
//    "goodsInfo":{
//        "goodsId":"商品id"
//        "payMark":"1",
//        "goodsName":"商品名称"
//        "imgUrl":"商品图片"
//        "goodsPrice":"100"
//        "count":"2"

    var orderId = ""
    var shopId = ""
    var shopPhone = ""
    var shopName = ""
    var orderState = ""
    var orderNum = ""
    var orderImg = ""
    var payMark = ""
    var ticketDate = ""
    var actualMoney = ""
    var isEvaluation = ""
    var goodsInfo = ArrayList<ServiceGoodsBean>()
    var moneyInfo = ArrayList<GoodsOrderDetailEntity.MoneyInfoBean>()


    var dateInfo = ArrayList<DateBean>()

   var ticketInfo= ArrayList<TicketInfoBean>()

    class TicketInfoBean{
        var ticket = ""
        var ticketState = ""
    }
}
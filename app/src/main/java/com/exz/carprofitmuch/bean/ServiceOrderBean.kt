package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/9.
 */
open class  ServiceOrderBean {
    var serviceShopName=""
    var orderState="2"
    var orderImg=""
    var orderPrice="100"
    var serviceCodes =ArrayList<String>()
    var time="2017.12.25-2018.2.2"
    var date=ArrayList<DateBean>()
    var goods=ServiceGoodsBean()
}
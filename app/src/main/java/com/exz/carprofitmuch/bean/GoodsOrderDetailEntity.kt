package com.exz.carprofitmuch.bean

/**
 * Created by pc on 2017/11/29.
 */

class GoodsOrderDetailEntity {

    /**
     * orderId : 1
     * orderNum : 订单编号
     * logisticsNum : 物流单号
     * orderState : 订单状态
     * shopPhone : 店铺联系电话
     * shopId : 店铺id
     * payMark : 1
     * shopName : 店铺名称
     * moneyInfo : [{"key":"总额","value":"200"}]
     * actualMoney : 180（实付）
     * buyerMessage : 买家留言
     * goodsInfo : [{"goodsId":"商品id","payMark":"1","goodsName":"商品名称","imgUrl":"商品图片","goodsPrice":"100","count":"2","skuid":"规格id","goodsType":"规格内容"}]
     * addressInfo : {"name":"姓名","phone":"电话号码","area":"所在地区（省市）","detailAddress":"详细地址"}
     * dateInfo : [{"key":"创建时间","value":"2016-03-1813: 53: 39"}]
     */

    var orderId: String? = null
    var orderNum: String? = null
    var logisticsNum: String? = null
    var orderState: String? = null
    var shopPhone: String? = null
    var shopId: String? = null
    var payMark: String? = null
    var shopName: String? = null
    var actualMoney: String? = null
    var buyerMessage: String? = null
    var addressInfo: AddressInfoBean? = null
    var moneyInfo: List<MoneyInfoBean>? = null
    var goodsInfo: List<GoodsBean>? = null
    var dateInfo: List<DateInfoBean>? = null

    class AddressInfoBean {
        /**
         * name : 姓名
         * phone : 电话号码
         * area : 所在地区（省市）
         * detailAddress : 详细地址
         */

        var name: String? = null
        var phone: String? = null
        var area: String? = null
        var detailAddress: String? = null
    }

    class MoneyInfoBean {
        /**
         * key : 总额
         * value : 200
         */

        var key: String? = null
        var value: String? = null
    }


    class DateInfoBean {
        /**
         * key : 创建时间
         * value : 2016-03-1813: 53: 39
         */

        var key: String? = null
        var value: String? = null
    }
}

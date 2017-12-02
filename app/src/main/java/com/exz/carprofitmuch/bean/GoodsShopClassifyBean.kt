package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/1.
 */
open class GoodsShopClassifyBean(var selfTypeId: String = "", var selfTypeName: String = "", var subType: ArrayList<GoodsShopClassifyBean> = ArrayList()) : KeyAndValueBean() {
    override fun absKey()=selfTypeId
    override fun absValue()=selfTypeName
}
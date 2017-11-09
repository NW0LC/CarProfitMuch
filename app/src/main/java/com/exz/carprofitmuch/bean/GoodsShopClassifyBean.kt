package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/1.
 */
open class GoodsShopClassifyBean(var id: String = "", var name: String = "", var list: ArrayList<GoodsShopClassifyBean> = ArrayList()) : KeyAndValueBean() {
    override fun absKey()=id
    override fun absValue()=name
}
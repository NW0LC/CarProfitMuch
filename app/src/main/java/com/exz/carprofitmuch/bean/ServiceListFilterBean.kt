package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/25.
 */
class ServiceListFilterBean(var id: String = "", var name: String = "") : KeyAndValueBean() {

    override fun absKey()=id
    override fun absValue()=name

}
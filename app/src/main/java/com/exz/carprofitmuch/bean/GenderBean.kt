package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/7.
 */
class GenderBean(var id: String = "", private var gender: String = "") : KeyAndValueBean(){
    override fun absKey(): String =id

    override fun absValue():String =gender
    override fun toString(): String = gender
}
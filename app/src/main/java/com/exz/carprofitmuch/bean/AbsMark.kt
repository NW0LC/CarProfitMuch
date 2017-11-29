package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/29.
 */
abstract class AbsMark{
    abstract fun getMarkId():String
    open  fun getAdUri():String = ""
    open  fun getAdsID():String = ""
}
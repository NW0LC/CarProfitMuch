package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/29.
 */
abstract class AbsMark{
    open fun getMarkId():String=""
    open fun getIdM():String=""
    open fun getClassM():String=""
    open fun getPayM():String=""
    open  fun getAdUri():String = ""
    open  fun getAdsID():String = ""
}
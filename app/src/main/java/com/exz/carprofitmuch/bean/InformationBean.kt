package com.exz.carprofitmuch.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by 史忠文
 * on 2017/10/20.
 */

open class InformationBean(var type: Int = 2) : MultiItemEntity{
    companion object {
        val TYPE_1=1
        val TYPE_2=2
    }
    override fun getItemType(): Int = type
//    "id":"1",
//    "title":"长城这款车两个版本。。。",
//    "date":"2017-10-12 17:00",
//    "imgUrl":"http://123.png"
//    "url":"咨询链接地址"

    var id=""
    var title=""
    var date=""
    var imgUrl=""
    var url=""

}

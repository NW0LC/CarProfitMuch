package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/21.
 */
class MainStoreServiceBean: AbsMark() {
//    "id":"1","imgUrl":"http://123.png","idMark":"1","payMark":"1","classMark":"1"
    var id=""
    var imgUrl=""
    var idMark=""
    var payMark=""
    var classMark=""

    override fun getMarkId()=id
    override fun getIdM()=idMark
    override fun getPayM()=payMark
    override fun getClassM()=classMark
}
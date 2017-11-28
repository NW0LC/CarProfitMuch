package com.exz.carprofitmuch.bean

import java.util.*

/**
 * Created by pc on 2017/4/21.
 */

class GoodsClassifyTwoEntities {

    /**
     * typeId : 4
     * typeName : 手机数码
     */

    var typeId: String=""
    var typeName: String=""
    /**
     * thirdType : [{"typeId":"50012945","typeName":"智能设备","imgUrl":"/UploadFiles/gcat/2016100917064015802062.jpg"},{"typeId":"50008351","typeName":"台式整机","imgUrl":"/UploadFiles/gcat/2016101917441275990154.jpg"},{"typeId":"50012944","typeName":"笔记本","imgUrl":"/UploadFiles/gcat/2016101917425246339229.jpg"},{"typeId":"50012942","typeName":"手机","imgUrl":"/UploadFiles/gcat/2016101917433656517425.jpg"},{"typeId":"50012943","typeName":"平板","imgUrl":"/UploadFiles/gcat/2016101917432024319150.jpg"}]
     */

    var thirdType= ArrayList<ThirdTypeEntity>()

    class ThirdTypeEntity {
        /**
         * typeId : 50012945
         * typeName : 智能设备
         * imgUrl : /UploadFiles/gcat/2016100917064015802062.jpg
         */

        var typeId: String=""
        var typeName: String=""
        var imgUrl: String=""
    }
}

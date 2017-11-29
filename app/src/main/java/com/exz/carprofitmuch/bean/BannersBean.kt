package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/19.
 */

class BannersBean: AbsMark() {
    override fun getMarkId()=mark
    override fun getAdUri()= adUrl
    override fun getAdsID()=adsId
    /**
     *        "adsId":"1",
     *        "mark":"1",
     *        "id":"1",
     *        "imgUrl":"http://123.png",
     *        "adUrl":"网页地址"
     *
     */

    var adsId: String=""
    var mark: String=""
    var id: String=""
    var imgUrl: String="http://seopic.699pic.com/photo/50055/6756.jpg_wh1200.jpg"
    var adUrl: String=""

}

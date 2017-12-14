package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/10/19.
 */

class BannersBean(var imgUrl: String = "") :AbsBanner, AbsMark() {
    override fun getImgUri()=imgUrl
    override fun getMarkId()=id
    override fun getMarkType()=mark
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
    var adUrl: String=""

}

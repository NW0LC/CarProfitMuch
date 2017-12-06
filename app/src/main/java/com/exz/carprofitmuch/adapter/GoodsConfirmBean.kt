package com.exz.carprofitmuch.adapter

import com.exz.carprofitmuch.bean.AddressBean
import com.exz.carprofitmuch.bean.GoodsConfirmSubBean

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
class GoodsConfirmBean{
    var address:AddressBean?=null
    var scoreInfo:GoodsConfirmScoreBean?=null
    var shopInfo=ArrayList<GoodsConfirmSubBean>()
}
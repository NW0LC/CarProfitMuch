package com.exz.carprofitmuch.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by 史忠文
 * on 2017/11/16.
 */
open class GoodsCarBean : MultiItemEntity {
    var type=TYPE_1
    override fun getItemType(): Int = type
    companion object {
        val TYPE_1=1
        val TYPE_2=2
    }

    var shopName="Created by 史忠文"
    var shopId=""
    var isCheck=false
    var goods=ArrayList<GoodsBean>()
}
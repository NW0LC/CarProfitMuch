package com.exz.carprofitmuch.bean

import java.util.*

open class GoodsClassifyBean(
        /**
         * goodsClassifyName : 颜色
         * goodsClassifyRealmList : [{"goodsSubClassifyName":"黑色","goodsSubClassifyId":"1","goodsSubClassifyUrl":"","goodsSubClassifyNextId":"1,2,3,4"}]
         */
        var goodsClassifyName: String = "", var goodsSubClassify: ArrayList<GoodsSubClassifyBean> = ArrayList<GoodsSubClassifyBean>()) {
}
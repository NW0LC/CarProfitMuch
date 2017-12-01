package com.exz.carprofitmuch.bean

class GoodsSubClassifyBean(
        /**
         * goodsSubClassifyName : 黑色
         * goodsSubClassifyId : 1
         * goodsSubClassifyUrl :
         * goodsSubState : 1 on  2 off 3 pass
         */
        var rankId: String = "",var rankName: String = "") {
    var goodsSubClassifyUrl=""
    var goodsSubState = STATE_2
    companion object {
        const val STATE_1="1"
        const val STATE_2="2"
    }
}
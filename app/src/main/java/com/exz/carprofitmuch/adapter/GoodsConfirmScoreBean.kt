package com.exz.carprofitmuch.adapter

import android.content.Context
import com.exz.carprofitmuch.R

/**
 * Created by NW0LC
 * on 2017/11/19.
 */
class GoodsConfirmScoreBean{
    var isSelect =false
    var score="1500"
    var scorePrice ="15"
    fun toString(context: Context): String =
            String.format(context.getString(R.string.goods_confirm_score),score, scorePrice)
}


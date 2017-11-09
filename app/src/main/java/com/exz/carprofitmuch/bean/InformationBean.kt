package com.exz.carprofitmuch.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by 史忠文
 * on 2017/10/20.
 */

open class InformationBean(private var type: Int = 0) : MultiItemEntity{
    companion object {
        val TYPE_1=1
        val TYPE_2=2
    }
    override fun getItemType(): Int = type

    var id=""
    var title="十九大十九大，十九大力哥打打打哦呀啊啊啊"
    var time="2018-19-45"
    var img="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1508495839398&di=1bcae1455525f0fe69d50c724266d4b1&imgtype=0&src=http%3A%2F%2Fpic65.nipic.com%2Ffile%2F20150420%2F8684504_003238146482_2.jpg"

}

package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CommentBean
import java.util.*

class MyTreasureAdapter<T : CommentBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_my_treasure, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
    }

}
package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.MsgBean
import kotlinx.android.synthetic.main.item_msg.view.*

class MsgAdapter<T: MsgBean>: BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_msg, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.tv_content.text=item.content
        itemView.img.setImageResource(if (item.state=="1")R.mipmap.icon_mine_msg else R.mipmap.icon_mine_msg_red)
    }

}
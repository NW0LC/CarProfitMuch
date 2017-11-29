package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.MyTreasureListBean
import kotlinx.android.synthetic.main.item_my_treasure.view.*
import java.util.*

class MyTreasureAdapter<T : MyTreasureListBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_my_treasure, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.tv_title.text = item.title
        itemView.tv_date.text = String.format(mContext.getString(R.string.main_my_treasure_time), item.date)
        itemView.tv_address.text = String.format(mContext.getString(R.string.main_my_treasure_address), item.shopAddress)
    }

}
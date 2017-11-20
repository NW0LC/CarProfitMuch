package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.KeyAndValueBean
import kotlinx.android.synthetic.main.item_goods_confrim_pop.view.*
import java.util.*


class GoodsConfirmPopAdapter<T:KeyAndValueBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_goods_confrim_pop, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.tv_title.text = item.toString()
        itemView.tv_title.isChecked = item.isCheck
    }
}

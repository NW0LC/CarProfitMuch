package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.FootprintBean
import kotlinx.android.synthetic.main.item_item_footprint.view.*
import java.util.*

class ItemFootprintAdapter<T : FootprintBean.GoodsListBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_footprint, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(entity.imgUrl)
        itemView.tv_goodsName.text = entity.goodsName
        itemView.tv_goodsPrice.text = String.format("${mContext.getString(R.string.CNY)}%s", entity.goodsPrice)
    }
}
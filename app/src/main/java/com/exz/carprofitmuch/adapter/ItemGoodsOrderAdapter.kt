package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import kotlinx.android.synthetic.main.item_item_goods_order.view.*


class ItemGoodsOrderAdapter<T :GoodsBean>: BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_goods_order, null) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.imgUrl)
        itemView.tv_goodsName.text = item.goodsName
        itemView.img.setImageURI(item.imgUrl)
        itemView.tv_goodsType.text=String.format(mContext.getString(R.string.goods_order_goodsType),item.goodsType)
        itemView.tv_goodsCount.text=String.format(mContext.getString(R.string.goods_order_goodsCount),item.goodsCount)
        itemView.tv_goodsPrice.text=String.format(mContext.getString(R.string.CNY)+"%s",item.goodsPrice)
    }
}




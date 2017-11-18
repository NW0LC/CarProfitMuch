package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean

/**
 * Created by pc on 2017/11/15.
 */

class ItemGoodsOrderAdapter() : BaseQuickAdapter<GoodsBean, BaseViewHolder>(R.layout.item_item_goods_order, null) {

    override fun convert(helper: BaseViewHolder, item: GoodsBean) {
        var itemView = helper.itemView
//        itemView.img.setImageURI(item.img)
//        itemView.tv_goodsName.text=item.title
//        itemView.tv_goodsType.text=String.format(mContext.getString(R.string.goods_order_goodsType),item.goodsType)
//        itemView.tv_goodsCount.text=String.format(mContext.getString(R.string.goods_order_goodsCount),item.goodsCount)
//        itemView.tv_goodsPrice.text=String.format(mContext.getString(R.string.CNY),item.price)
    }
}

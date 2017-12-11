package com.exz.carprofitmuch.adapter

import android.view.View
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
        if (item.goodsPrice.isEmpty()) {
            itemView.tv_goodsPriceText.visibility= View.GONE
            itemView.tv_goodsPrice.visibility= View.GONE
        }else {
            itemView.tv_goodsPriceText.visibility= View.VISIBLE
            itemView.tv_goodsPrice.visibility= View.VISIBLE
        }
        if (item.payMark=="1"){
            itemView.tv_goodsPrice.text=String.format("%s${mContext.getString(R.string.SCORE)}",item.goodsPrice)
        }else{
            itemView.tv_goodsPrice.text=String.format(mContext.getString(R.string.CNY)+"%s",item.goodsPrice)
        }

    }
}




package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import kotlinx.android.synthetic.main.item_item_card_package.view.*


class ItemCardPackageAdapter<T : ServiceGoodsBean>: BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_card_package, null) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.imgUrl)
        itemView.tv_service_orderName.text=item.goodsName
        itemView.tv_service_orderCount.text=String.format(mContext.getString(R.string.card_package_list_count),item.count)
        if (item.payMark=="1"){
            itemView.tv_service_orderPrice.text=String.format("${mContext.getString(R.string.card_package_list_price)}%s${mContext.getString(R.string.SCORE)}",item.goodsPrice)
        }else{
            itemView.tv_service_orderPrice.text=String.format("${mContext.getString(R.string.card_package_list_price)}${mContext.getString(R.string.CNY)}%s",item.goodsPrice)
        }
    }
}




package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.utils.SZWUtils
import kotlinx.android.synthetic.main.item_item_footprint.view.*
import java.util.*

class ItemFootprintAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_footprint, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(entity.imgUrl)
        itemView.tv_goodsName.text = entity.goodsName
        if (entity.payMark=="1"){
            itemView.tv_goodsPrice.text=String.format("%s${mContext.getString(R.string.SCORE)}",entity.goodsPrice)
        }else{
            itemView.tv_goodsPrice.text=String.format("${mContext.getString(R.string.CNY)}%s",entity.goodsPrice)
        }
        itemView.setOnClickListener{
            if (SZWUtils.getMarkIntent(mContext, entity) != null)
                mContext.startActivity(SZWUtils.getMarkIntent(mContext,entity))
        }
    }
}
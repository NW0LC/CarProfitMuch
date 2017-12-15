package com.exz.carprofitmuch.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.PromotionsPersonalBean
import kotlinx.android.synthetic.main.item_promotions_personal.view.*
import java.util.*

class PromotionsPersonalAdapter<T : PromotionsPersonalBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_promotions_personal, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        helper.addOnClickListener(R.id.lay_content)
        helper.addOnClickListener(R.id.tv_shopName)
        helper.addOnClickListener(R.id.bt_phoneCall)
        itemView.tv_title.text=item.title
        itemView.tv_beginTime.text=item.startDate
        itemView.tv_speed.text=String.format("%s"+mContext.getString(R.string.DAY),item.dayCount)
        itemView.tv_shopName.text=item.shopName
        itemView.img_pass.visibility=if (item.state!="6") View.GONE else View.VISIBLE
    }

}
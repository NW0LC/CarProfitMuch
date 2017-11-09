package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.KeyAndValueBean
import kotlinx.android.synthetic.main.item_service_list_filter.view.*
import org.jetbrains.anko.textColor


class ServiceListFilterAdapter<T : KeyAndValueBean>(): BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_service_list_filter, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        helper.itemView.title.text=item.value
        helper.itemView.title.textColor=ContextCompat.getColor(mContext,if (item.isCheck)R.color.colorPrimary else R.color.MaterialGrey600)
        helper.itemView.title.setCompoundDrawablesRelativeWithIntrinsicBounds(if (item.isCheck)ContextCompat.getDrawable(mContext,R.mipmap.icon_service_list_classify_ok)else null,null,null,null)
        helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.White))

    }

}
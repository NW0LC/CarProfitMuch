package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.AddressBean
import kotlinx.android.synthetic.main.item_address_choose.view.*
import java.util.*


class AddressChooseAdapter<T : AddressBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_address_choose, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_userName.text=item.name
        itemView.tv_userPhone.text=item.phone
        if (item.isDefault()) {
            val default = mContext.getString(R.string.address_choose_default)
            val msp = SpannableString(default +item.toString())
            msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)), 0, default.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemView.tv_address.text = msp
        }else{
            itemView.tv_address.text = item.toString()
        }

    }
}



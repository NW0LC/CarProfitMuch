package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.AddressBean
import kotlinx.android.synthetic.main.item_address_manager.view.*
import org.jetbrains.anko.textColor
import java.util.*


class AddressManagerAdapter<T : AddressBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_address_manager, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        helper.addOnClickListener(R.id.bt_edit)
        helper.addOnClickListener(R.id.bt_delete)
        helper.addOnClickListener(R.id.radioButton)

        itemView.tv_userName.text = item.name
        itemView.tv_userPhone.text = item.phone
        itemView.tv_address.text = item.toString()

        if (item.isDefault()) {
            itemView.radioButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.icon_mine_open_shop_select), null, null, null)
            itemView.radioButton.text = mContext.getString(R.string.address_choose_default2)
            itemView.radioButton.textColor = ContextCompat.getColor(mContext, R.color.colorPrimary)
        } else {
            itemView.radioButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.icon_mine_open_shop_unselect), null, null, null)
            itemView.radioButton.text = mContext.getString(R.string.address_choose_setDefault)
            itemView.radioButton.textColor = ContextCompat.getColor(mContext, R.color.MaterialGrey600)
        }

    }
}



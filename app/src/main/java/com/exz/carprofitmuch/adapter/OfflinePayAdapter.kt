package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.OffLinePayBean
import com.szw.framelibrary.imageloder.GlideApp
import kotlinx.android.synthetic.main.item_offline_pay.view.*


class OfflinePayAdapter<T : OffLinePayBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_offline_pay, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        GlideApp.with(mContext).load(item.shopIcon).into(itemView.img)
        itemView.tv_shopName.text = item.shopName
        itemView.tv_time.text = item.payTime
        itemView.tv_remark.text = item.Remark
        itemView.tv_price.text = item.payMoney
        itemView.tv_pay_type.text = when (item.payType) {
            "1" -> {
                "支付宝"
            }
            "2" -> {
                "微信"
            }
            "3" -> {
                "余额"
            }
            else -> {
                ""
            }
        }

    }
}



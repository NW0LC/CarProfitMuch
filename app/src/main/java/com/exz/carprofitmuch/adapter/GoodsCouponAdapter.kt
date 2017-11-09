package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CouponBean
import kotlinx.android.synthetic.main.item_goods_coupon.view.*
import kotlinx.android.synthetic.main.item_red_packet.view.*
import java.util.*


class GoodsCouponAdapter<T : CouponBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_goods_coupon, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_couponPrice.text=item.couponPrice
        itemView.tv_redPacket_info.text = String.format(mContext.getString(R.string.goods_detail_coupon_info),item.couponInfo)
        itemView.tv_redPacket_time.text =String.format(mContext.getString(R.string.goods_detail_coupon_time),item.couponTime)

    }
}

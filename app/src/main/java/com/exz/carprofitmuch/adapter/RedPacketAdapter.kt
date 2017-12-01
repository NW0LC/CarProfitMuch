package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CouponBean
import kotlinx.android.synthetic.main.item_red_packet.view.*
import java.util.*


class RedPacketAdapter<T : CouponBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_red_packet, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView=helper.itemView
        itemView.tv_redPacket_price.text = entity.discount
        itemView.tv_redPacket_info.text = String.format(mContext.getString(R.string.goods_detail_redPacket_info),entity.limitMoney)
        itemView.tv_redPacket_time.text =String.format(mContext.getString(R.string.goods_detail_coupon_time),entity.beginDate+"-"+entity.invalidDate)
        itemView.had.text = String.format(mContext.getString(R.string.goods_detail_redPacket_get),entity.receivedCount)
        itemView.yet.text = String.format(mContext.getString(R.string.goods_detail_redPacket_get_yet),entity.surplusCount)
        if (entity.isGet == "0") {
//            itemView.had.visibility = View.VISIBLE
//            itemView.yet.visibility = View.VISIBLE
            itemView.button.text = "点击领取"
            itemView.type.visibility = View.GONE
            itemView.button.delegate.backgroundColor=ContextCompat.getColor(mContext,R.color.White)
        } else {
//            itemView.had.visibility = View.INVISIBLE
//            itemView.yet.visibility = View.INVISIBLE
            itemView.button.text = "去使用"
            itemView.type.visibility = View.VISIBLE
            itemView.button.delegate.backgroundColor=ContextCompat.getColor(mContext,R.color.MaterialYellow600)
        }
    }
}



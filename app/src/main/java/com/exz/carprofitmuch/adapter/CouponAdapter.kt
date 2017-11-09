package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CouponBean
import kotlinx.android.synthetic.main.item_coupon.view.*
import java.util.*

class CouponAdapter<T : CouponBean>(var couponState:Int) : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_coupon, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        when (couponState) {
            0 -> {//未使用
                itemView.ll_bg.setBackgroundResource(R.mipmap.icon_mine_coupon_bg_on)
                itemView.tv_store_name.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                itemView.tv_cny.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                itemView.tv_price.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                itemView.tv_validity_date.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey500))
                itemView.iv_coupon_state.visibility = View.INVISIBLE
            }
            1 -> { //已使用
                itemView.ll_bg.setBackgroundResource(R.mipmap.icon_mine_coupon_bg_on)
                itemView.tv_store_name.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                itemView.tv_cny.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                itemView.tv_price.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
                itemView.tv_validity_date.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey500))
                itemView.iv_coupon_state.visibility = View.VISIBLE
            }
            2 -> {  //已过期
                itemView.ll_bg.setBackgroundResource(R.mipmap.icon_mine_coupon_bg_off)
                itemView.tv_store_name.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.tv_cny.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.tv_price.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.tv_validity_date.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.iv_coupon_state.visibility = View.INVISIBLE
            }
        }
    }
}
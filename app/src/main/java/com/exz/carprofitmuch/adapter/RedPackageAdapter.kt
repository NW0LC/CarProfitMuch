package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CouponBean
import kotlinx.android.synthetic.main.item_red_package.view.*
import java.util.*

class RedPackageAdapter<T : CouponBean>(var couponState: Int) : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_red_package, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        when (couponState) {
            0 -> {//未使用
                itemView.rl_bg.setBackgroundResource(R.mipmap.icon_mine_red_package_bg_on)
                itemView.tv_store_name.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow600))
                itemView.tv_cny.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow100))
                itemView.tv_price.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow100))
                itemView.tv_all_use.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow700))
                itemView.tv_validity_date.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow700))
                itemView.iv_coupon_state.visibility = View.INVISIBLE
            }
            1 -> { //已使用
                itemView.rl_bg.setBackgroundResource(R.mipmap.icon_mine_red_package_bg_on)
                itemView.tv_store_name.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow600))
                itemView.tv_cny.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow100))
                itemView.tv_price.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow100))
                itemView.tv_all_use.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow700))
                itemView.tv_validity_date.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialYellow700))
                itemView.iv_coupon_state.visibility = View.VISIBLE
            }
            2 -> {  //已过期
                itemView.rl_bg.setBackgroundResource(R.mipmap.icon_mine_red_package_bg_off)
                itemView.tv_store_name.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.tv_cny.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.tv_price.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.tv_all_use.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.tv_validity_date.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                itemView.iv_coupon_state.visibility = View.GONE
            }
        }
    }
}
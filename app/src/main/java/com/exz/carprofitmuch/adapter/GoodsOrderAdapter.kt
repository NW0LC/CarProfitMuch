package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.MyOrderBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import kotlinx.android.synthetic.main.item_my_order.view.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.view.*

/**
 * Created by pc on 2017/11/15.
 *
 */

class GoodsOrderAdapter<T> : BaseQuickAdapter<MyOrderBean, BaseViewHolder>(R.layout.item_my_order, null) {

    override fun convert(helper: BaseViewHolder, item: MyOrderBean) {
        val mAdapter = ItemGoodsOrderAdapter<GoodsBean>()
        mAdapter.bindToRecyclerView(helper.itemView.mRecyclerView)
        mAdapter.setNewData(item.goodsInfo)
        helper.itemView.mRecyclerView.isFocusable = false
        helper.itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        helper.itemView.mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))
        helper.addOnClickListener(R.id.tv_mid)
        helper.addOnClickListener(R.id.tv_right)
        initStateBtn(item.orderState, helper.itemView.tv_my_order, TextView(mContext), helper.itemView.tv_mid, helper.itemView.tv_right)
        helper.itemView.tv_shop_name.text = item.shopName
        helper.itemView.tv_num_goods.text = String.format(mContext.getString(R.string.mine_my_order_num_goods), item.goodsInfo.size)
        helper.itemView.tv_refund.text = String.format(mContext.getString(R.string.mine_my_order_actual_payment), item.actualMoney)
    }

    companion object {
        //    1待付款 2待收货 3待评价 4已结束 5 取消订单
        public fun getState(state: String): String = when (state) {
            "1" -> "待付款"
            "2" -> "待发货"
            "3" -> "待收货"
            "4" -> "待评价"
            "5" -> "已完成"
            else -> "无此状态"

        }

        /**
         * [state] 订单状态id
         * [view] view[0] 订单状态view
         * [view] view[1] btLeft
         * [view] view[2] btMid
         * [view] view[3] btRight
         */
        fun initStateBtn(state: String, vararg view: TextView) {
            /**         btLeft    btMid     btRight
             * 1待付款     【联系商家   取消订单  支付订单】
             * 2待发货     【         联系商家  申请退款】
             * 3待收货     【联系商家   查看物流  确认收货】
             * 4待评价     【联系商家   申请退货  评价订单】
             * 5已结束     【联系商家   申请退货  删除订单】
             * 其他
             */
            view[0].text = getState(state)
            if (view[1].id == R.id.tv_left) view[1].visibility = View.VISIBLE else view[1].visibility = View.GONE
            val strLeft: String
            val strMid: String
            var strRight = ""
            when (state) {
                "1" -> {  // 【联系商家   取消订单  支付订单】
                    strLeft = "联系商家"
                    strMid = "取消订单"
                    strRight = "支付订单"
                }
                "1" -> {  // 【联系商家   联系商家  申请退款】
                    strLeft = ""
                    strMid = "联系商家"
                    strRight = "申请退款"
                }
                "3" -> { //【联系商家   查看物流  确认收货】
                    strLeft = "联系商家"
                    strMid = "查看物流"
                    strRight = "确认收货"
                }
                "4" -> {// 【联系商家   删除订单  评价订单】
                    strLeft = "联系商家"
                    strMid = "申请退货"
                    strRight = "评价订单"
                }
                "5" -> {// 【联系商家   删除订单  】
                    view[3].visibility = View.GONE
                    strLeft = "联系商家"
                    strMid = "申请退货"
                    strRight = "删除订单"
                }

                else -> {
                    view[1].visibility = View.GONE
                    view[2].visibility = View.GONE
                    strLeft = ""
                    strMid = ""
                    strRight = ""
                }

            }
            view[1].text = strLeft
            view[2].text = strMid
            view[3].text = strRight
        }
    }
}

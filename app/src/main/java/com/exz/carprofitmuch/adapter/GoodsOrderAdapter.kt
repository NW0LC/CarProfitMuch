package com.exz.carprofitmuch.adapter

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.MyOrderBean
import kotlinx.android.synthetic.main.item_my_order.view.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.view.*

/**
 * Created by pc on 2017/11/15.
 *
 */

class GoodsOrderAdapter<T:MyOrderBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_my_order, null) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val mAdapter = ItemGoodsOrderAdapter<GoodsBean>()
        mAdapter.bindToRecyclerView(helper.itemView.mRecyclerView)
        mAdapter.setNewData(item.goodsInfo)
        helper.itemView.mRecyclerView.isFocusable = false
        helper.itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        helper.addOnClickListener(R.id.tv_mid)
        helper.addOnClickListener(R.id.tv_right)
        initStateBtn(item.orderState, helper.itemView.tv_my_order, TextView(mContext), helper.itemView.tv_mid, helper.itemView.tv_right)
        helper.itemView.tv_shop_name.text = item.shopName
        helper.itemView.tv_num_goods.text = String.format(mContext.getString(R.string.mine_my_order_num_goods), item.goodsInfo.size)
        if (item.payMark=="1"){
            helper.itemView.tv_refund.text =String.format("${mContext.getString(R.string.mine_my_order_actual_payment)}%s${mContext.getString(R.string.SCORE)}",item.actualMoney)
        }else{
            helper.itemView.tv_refund.text = String.format("${mContext.getString(R.string.mine_my_order_actual_payment)}${mContext.getString(R.string.CNY)}%s", item.actualMoney)
        }
    }

    companion object {
        //    1待付款 2待收货 3待评价 4已结束 5 取消订单
        fun getState(state: String): String = when (state) {
            "1" -> "待付款"
            "2" -> "待发货"
            "3" -> "待收货"
            "4" -> "待评价"
            "5" -> "已完成"
            "6" -> "已取消"
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
            /**         btLeft        btMid     btRight
             * 1待付款 【联系商家   取消订单   支付订单】
             * 2待发货 【联系商家              申请退款】
             * 3待收货 【联系商家   查看物流   确认收货】
             * 4待评价 【联系商家   申请退货   评价订单】
             * 5已结束 【联系商家              删除订单】
             * 6已取消 【                      删除订单】
             * 其他
             */
            view[0].text = getState(state)
            view[1].visibility = View.VISIBLE
            view[2].visibility = View.VISIBLE
            view[3].visibility = View.VISIBLE
            val strLeft: String
            val strMid: String
            val strRight: String
            when (state) {
                "1" -> {  // 【联系商家   取消订单  支付订单】
                    strLeft = "联系商家"
                    strMid = "取消订单"
                    strRight = "支付订单"
                }
                "2" -> {  // 【联系商家   联系商家  申请退款】
                    view[2].visibility = View.GONE
                    strLeft = "联系商家"
                    strMid = ""
                    strRight = "申请退款"
                }
                "3" -> { //【联系商家   查看物流  确认收货】
                    strLeft = "联系商家"
                    strMid = "查看物流"
                    strRight = "确认收货"
                }
                "4" -> {// 【联系商家   申请退货  评价订单】
                    strLeft = "联系商家"
                    strMid = "申请退货"
                    strRight = "评价订单"
                }
                "5" -> {// 【联系商家   删除订单  】
                    view[2].visibility = View.GONE
                    strLeft = "联系商家"
                    strMid = ""
                    strRight = "删除订单"
                }
                "6" -> {// 【联系商家   删除订单  】
                    view[1].visibility = View.GONE
                    view[2].visibility = View.GONE
                    strLeft = ""
                    strMid = ""
                    strRight = "删除订单"
                }

                else -> {
                    view[1].visibility = View.GONE
                    view[2].visibility = View.GONE
                    view[3].visibility = View.GONE
                    strLeft = ""
                    strMid = ""
                    strRight = ""
                }

            }
            view[1].text = strLeft
            view[2].text = strMid
            view[3].text = strRight

            if (view[1].id != R.id.tv_left){
                if (view[2].visibility==View.VISIBLE) {
                    view[1].visibility=View.GONE
                }else{
                    view[1].visibility=View.VISIBLE
                }
            }
        }
    }
}

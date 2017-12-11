package com.exz.carprofitmuch.adapter

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.R.id.state
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.ReturnGoodsBean
import kotlinx.android.synthetic.main.item_return_goods.view.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.view.*
import kotlinx.android.synthetic.main.lay_return_goods_num.view.*

/**
 * Created by pc on 2017/11/15.
 *
 */

class ReturnGoodsAdapter<T> : BaseQuickAdapter<ReturnGoodsBean, BaseViewHolder>(R.layout.item_return_goods, null) {

    override fun convert(helper: BaseViewHolder, item: ReturnGoodsBean) {
        val mAdapter = ItemGoodsOrderAdapter<GoodsBean>()
        mAdapter.bindToRecyclerView(helper.itemView.mRecyclerView)
        mAdapter.setNewData(item.goodsInfo)
        helper.itemView.mRecyclerView.isFocusable=false
        helper.itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        helper.addOnClickListener(R.id.tv_mid)
        helper.addOnClickListener(R.id.tv_right)
        when (item.returnOrderType) {
            "1","3" -> {//退款
                helper.itemView.tv_refund_num.text= String.format(mContext.getString(R.string.return_priceOrderNum)+"%s",item.returnOrderId)
            }
            "2" -> {//退货
                helper.itemView.tv_refund_num.text=String.format(mContext.getString(R.string.return_goodsOrderNum)+"%s",item.returnOrderId)
            }
        }

        if (item.goodsInfo[0].payMark=="1")
        helper.itemView.tv_refund.text=String.format(mContext.getString(R.string.mine_return_price)+mContext.getString(R.string.SCORE),item.returnScore)
        else{
            helper.itemView.tv_refund.text=String.format(mContext.getString(R.string.mine_return_price),mContext.getString(R.string.CNY)+item.returnMoney)
        }
        initStateBtn(item.returnOrderState,item.returnOrderSubState, item.returnOrderType,helper.itemView.tv_my_order, helper.itemView.tv_mid, helper.itemView.tv_right)


    }

    companion object {

//        returnOrderSubState	"1{[1]仅退款,[2]卖家拒绝} 2{[1]仅退款,[2]卖家拒绝,[2]买家向平台申诉} 3{[2]退货退款,[1]卖家同意}
//        4{[2]退货退款,[1]卖家同意,[3]买家填写物流单号} 5{[2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货}
//        6{[2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货,[2]买家向平台申诉}
//        7{[2]退货退款,[2]卖家拒绝} 8{[2]退货退款,[2]卖家拒绝,[2]买家向平台申诉}


        //        （0全部 1待处理 2处理中 3已退款 4已删除 5已取消 6平台已拒绝）
        private fun getState(state: String): String = when (state) {
            "1" -> "待处理"
            "2" -> "处理中"
            "3" -> "已完成"
            else -> "无此状态"

        }

        /**
         *
         *  returnOrderState 0全部 1待处理 2处理中 3已退款 4已删除 5已取消 6平台已拒绝）
         * [state] 订单状态id
         * [view] view[0] 订单状态view
         * [view] view[1] btLeft
         * [view] view[2] btMid
         * [view] view[3] btRight
         *
         *  returnOrderSubState
         */
        fun initStateBtn(returnOrderState: String,returnOrderSubState :String,returnOrderType:String,vararg view: TextView) {
            /**             btLeft       btMid     btRight
             * 1待处理     【             联系商家   取消收货】
             * 2处理中     【                      联系商家】
             * 3已完成     【             删除订单   评价订单】
             * 其他
             */
            view[0].text = getState(returnOrderState)
            var strMid=""
            var strRight = ""
            when (returnOrderState) {
                "1" -> {
                    strMid = "联系商家"
                    strRight =if(returnOrderType == "2") "取消退货" else "取消退款"
                }
                "2" -> { //【联系商家   查看物流  确认收货】

                    when (returnOrderSubState) {
                        "1" -> { //{[1]仅退款,[2]卖家拒绝}
                            strMid = "平台申诉"
                            strRight="取消退款"
                        }
                        "2" -> {//{[1]仅退款,[2]卖家拒绝,[2]买家向平台申诉}
                            strMid = "联系商家"
                            strRight="取消退款"
                        }
                        "3" -> {// {[2]退货退款,[1]卖家同意}
                            strMid = "填写物流"
                            strRight="取消退款"
                        }
                        "4" -> {// {[2]退货退款,[1]卖家同意,[3]买家填写物流单号}
                            strMid = "联系商家"
                            strRight="取消退货"
                        }
                        "5" -> {// [2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货}
                            strMid = "平台申诉"
                            strRight="取消退货"
                        }
                        "6" -> {// [2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货,[2]买家向平台申诉}
                            strMid = "联系商家"
                            strRight="取消退货"
                        }

                        "7" -> {// [{[2]退货退款,[2]卖家拒绝} 8{[2]退货退款,[2]卖家拒绝,[2]买家向平台申诉}
                            strMid = "联系商家"
                            strRight="取消退货"
                        }

                    }

                }
                "3" -> {// 【联系商家   平台申诉  取消退货】
                    strMid = "联系商家"
                    strRight = "删除订单"
                }

                else -> {
                    view[1].visibility = View.GONE
                    view[2].visibility = View.GONE
                    strMid = ""
                    strRight = ""
                }

            }
            view[1].text = strMid
            view[2].text = strRight
        }
    }
}
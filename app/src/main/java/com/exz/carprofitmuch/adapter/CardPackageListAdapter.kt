package com.exz.carprofitmuch.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ServiceOrderBean
import kotlinx.android.synthetic.main.item_card_package_list.view.*
import java.util.*

class CardPackageListAdapter<T : ServiceOrderBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_card_package_list, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_service_shop_name.text=item.serviceShopName
        itemView.img.setImageURI(item.orderImg)
        itemView.tv_service_orderName.text=item.goods.goodsName
        itemView.tv_service_orderCount.text=String.format(mContext.getString(R.string.card_package_list_count),item.goods.count)
        itemView.tv_service_orderPrice.text=String.format(mContext.getString(R.string.card_package_list_price),item.goods.goodsPrice)
        itemView.tv_service_orderTime.text=String.format(mContext.getString(R.string.card_package_list_time),item.time)

        helper.addOnClickListener(R.id.bt_left)
        helper.addOnClickListener(R.id.bt_mid)
        helper.addOnClickListener(R.id.bt_right)
        initStateBtn(item.orderState,itemView.tv_service_orderState,itemView.bt_left,itemView.bt_mid,itemView.bt_right)
    }
    companion object {
        //    1未使用 2已使用 3已过期 4已退款
        private fun getState(state: String): String = when (state) {
            "1" -> "未使用"
            "2" -> "已使用"
            "3" -> "已过期"
            "4" -> "已退款"
            else -> "无此状态"

        }

        /**
         * [state] 订单状态id
         * [view] view[0] 订单状态view
         * [view] view[1] btLeft
         * [view] view[2] btMid
         * [view] view[3] btRight
         */
        fun initStateBtn(state:String,vararg view: TextView){
            /**         btLeft    btMid     btRight
             * 1未使用            联系商家  申请退款
             * 2已使用  删除订单  联系商家  评价服务
             * 3已过期            删除订单  联系商家
             * 4已退款                      删除订单
             * 其他
             */
            view[0].text = getState(state)
            view[1].visibility = View.VISIBLE
            view[2].visibility = View.VISIBLE
            view[3].visibility = View.VISIBLE
            val strLeft:String
            val strMid:String
            val strRight:String
            when (state) {
                "1" -> {
                    view[1].visibility = View.GONE
                    strLeft = ""
                    strMid = "联系商家"
                    strRight = "申请退款"
                }
                "2" -> {
                    strLeft = "删除订单"
                    strMid = "联系商家"
                    strRight = "评价服务"
                }
                "3" -> {
                    view[1].visibility = View.GONE
                    strLeft = ""
                    strMid = "删除订单"
                    strRight = "联系商家"
                }
                "4" -> {
                    view[1].visibility = View.GONE
                    view[2].visibility = View.GONE
                    strLeft = ""
                    strMid = ""
                    strRight = "删除订单"
                }

                else -> {
                    view[1].visibility = View.GONE
                    view[2].visibility = View.GONE
                    strLeft = ""
                    strMid = ""
                    strRight = "删除订单"
                }

            }
            view[1].text = strLeft
            view[2].text = strMid
            view[3].text = strRight
        }
    }
}
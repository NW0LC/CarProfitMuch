package com.exz.carprofitmuch.adapter

import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsConfirmSubBean
import kotlinx.android.synthetic.main.item_goods_confirm.view.*
import java.text.DecimalFormat
import java.util.*


class GoodsConfirmAdapter<T : GoodsConfirmSubBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_goods_confirm, ArrayList<T>()) {
    val format=DecimalFormat("0.00")
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_goodsShop_name.text=item.shopName
        itemView.count.text=item.goodsInfo[0].goodsCount
        itemView.tv_coupon.text=item.coupon
        itemView.tv_sendWay.text=item.sendWay
        itemView.ed_msg.setText(item.buyerMsg)
        itemView.tv_totalCount.text=String.format(mContext.getString(R.string.goods_confirm_totalCount),item.goodsCount)
        itemView.tv_totalPrice.text=String.format("${mContext.getString(R.string.CNY)}%s",item.totalPrice)
        itemView.bt_coupon.visibility=if (item.couponInfo.size==0) View.GONE else View.VISIBLE
        itemView.bt_sendWay.visibility=if (item.transportInfo.size==0) View.GONE else View.VISIBLE




        val mAdapter=ItemGoodsOrderAdapter<GoodsBean>()
        mAdapter.bindToRecyclerView(itemView.mRecyclerView)
        itemView.mRecyclerView.isFocusable=false
        itemView.mRecyclerView.layoutManager= LinearLayoutManager(mContext)
        mAdapter.setNewData(item.goodsInfo)

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                data[helper.adapterPosition].buyerMsg=s.toString()
            }
        }
        itemView.ed_msg.addTextChangedListener(watcher)


        helper.addOnClickListener(R.id.bt_sendWay)
        helper.addOnClickListener(R.id.bt_coupon)
        helper.addOnClickListener(R.id.add)
        helper.addOnClickListener(R.id.minus)
        helper.addOnClickListener(R.id.count)
    }
}
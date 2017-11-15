package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ScoreOrderBean
import kotlinx.android.synthetic.main.item_item_goods_order.view.*
import kotlinx.android.synthetic.main.item_order_score.view.*
import java.util.*


class ScoreExchangeRecordAdapter<T : ScoreOrderBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_order_score, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_scoreOrderNum.text = item.goodsNum
        itemView.img.setImageURI(item.goods.img)
        itemView.tv_goodsName.text=item.goods.title
        itemView.tv_goodsType.text=String.format(mContext.getString(R.string.goods_order_goodsType),item.goods.goodsType)
        itemView.tv_goodsCount.text=String.format(mContext.getString(R.string.goods_order_goodsCount),item.goods.goodsCount)
        itemView.tv_goodsPrice.text=String.format(mContext.getString(R.string.CNY),item.goods.scorePrice)
        itemView.tv_score_count.text=String.format(mContext.getString(R.string.order_score_count),item.goods.goodsCount,item.scoreCount)
        itemView.tv_scoreExchange_date.text=String.format(mContext.getString(R.string.order_score_exchangeDate),item.scoreExchangeDate)

        helper.addOnClickListener(R.id.bt_service)
    }
}



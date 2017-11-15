package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import kotlinx.android.synthetic.main.item_item_goods_order.view.*
import kotlinx.android.synthetic.main.item_order_score.view.*
import java.util.*


class ScoreExchangeRecordAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_order_score, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_scoreOrderNum.text = item.goodsNum
        itemView.img.setImageURI(item.img)
        itemView.tv_goodsName.text=item.title
        itemView.tv_goodsType.text=item.goodsType
        itemView.tv_goodsCount.text=String.format(mContext.getString(R.string.goods_order_goodsCount),item.goodsCount)
    }
}



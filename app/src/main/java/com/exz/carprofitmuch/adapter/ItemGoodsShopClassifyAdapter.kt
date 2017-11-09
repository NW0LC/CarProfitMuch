package com.exz.carprofitmuch.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsShopClassifyBean
import kotlinx.android.synthetic.main.item_item_goods_shop_classify.view.*

class ItemGoodsShopClassifyAdapter<T : GoodsShopClassifyBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_goods_shop_classify, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.title.text = item.value
        itemView.line_select.visibility = if (item.isCheck) View.VISIBLE else View.GONE
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        if (if (headerLayoutCount == 0) helper.adapterPosition % 2 == 1 else helper.adapterPosition % 2 == 0) {
            layoutParams.leftMargin = SizeUtils.dp2px(1f)
        } else {
            layoutParams.rightMargin = SizeUtils.dp2px(1f)
        }
        if (data.size % 2 == 0) {
            if (helper.adapterPosition != data.size - 1 && if (data.size > 2) helper.adapterPosition != data.size - 2 else false)
                layoutParams.bottomMargin = SizeUtils.dp2px(2f)
        } else {
            if (helper.adapterPosition != data.size - 1) {
                layoutParams.bottomMargin = SizeUtils.dp2px(2f)
            }
        }
        itemView.layoutParams = layoutParams
    }

}
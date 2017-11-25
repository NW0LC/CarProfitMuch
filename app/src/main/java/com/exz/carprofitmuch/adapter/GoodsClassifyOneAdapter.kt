package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsClassifyOneEntities
import kotlinx.android.synthetic.main.adapter_goods_classify_one.view.*

/**
 * Created by pc on 2017/4/21.
 */

class GoodsClassifyOneAdapter : BaseQuickAdapter<GoodsClassifyOneEntities, BaseViewHolder>(R.layout.adapter_goods_classify_one, null) {

    private var isSelect: Int = 0

    override fun convert(helper: BaseViewHolder, item: GoodsClassifyOneEntities) {
        val itemView = helper.itemView
        itemView.cell_tv.text = item.typeName
        if (isSelect == data.indexOf(item)) {
            itemView.cell_tv.setBackgroundResource(R.drawable.goods_classify_start_line_on)
            itemView.cell_tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
        } else {
            itemView.cell_tv.setBackgroundResource(R.drawable.goods_classify_start_line_off)
            itemView.cell_tv.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
        }

    }

    fun setSelect(select: Int) {
        this.isSelect = select
    }
}

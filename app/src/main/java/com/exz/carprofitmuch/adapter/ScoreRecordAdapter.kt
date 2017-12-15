package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ScoreRecordBean
import kotlinx.android.synthetic.main.item_mine_score.view.*
import java.util.*


class ScoreRecordAdapter<T : ScoreRecordBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_mine_score, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_title.text = item.title
        itemView.tv_date.text = item.date
        if (item.isAdd == "1") {
            itemView.tv_score.text = String.format("+%s", item.amount)
            itemView.img.setImageResource(R.mipmap.icon_mine_score_expend)
        } else {
            itemView.tv_score.text = String.format("-%s", item.amount)
            itemView.img.setImageResource(R.mipmap.icon_mine_score_income)
        }
    }
}



package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.BalanceRecordBean
import kotlinx.android.synthetic.main.item_balance_record.view.*
import java.util.*

class AccountBalanceRecordAdapter<T : BalanceRecordBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_balance_record, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView=helper.itemView
        if (entity.isIncrease == "1") { //0 减少  1 增加
            itemView.img_type.setBackgroundResource(R.mipmap.icon_mine_balance_income)
            itemView.tv_price.text = String.format("+%s", entity.money)
        } else if (entity.isIncrease == "0") {
            itemView.img_type.setBackgroundResource(R.mipmap.icon_mine_balance_expend)
            itemView.tv_price.text = String.format("-%s", entity.money)
        }
        itemView.tv_title.text = entity.title
        itemView.tv_date.text = entity.date
    }
}
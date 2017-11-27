package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.PromotionsBean
import com.flyco.roundview.RoundTextView
import kotlinx.android.synthetic.main.item_main_promotions.view.*
import java.util.*

class PromotionsAdapter<T : PromotionsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main_promotions, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.img.setImageURI(item.img)
        itemView.tv_title.text=item.title
        itemView.tv_speed.text=String.format("%s"+mContext.getString(R.string.DAY),item.day)
        itemView.tv_distance.text= String.format(item.distance + "km")
        setStateColorAndStr(item.state,itemView.tv_state)
    }
    companion object {
        fun setStateColorAndStr(state:String,vararg view:RoundTextView){
            when (state) {
                "1" -> {
                }
                else -> {
                }
            }
        }
    }
}
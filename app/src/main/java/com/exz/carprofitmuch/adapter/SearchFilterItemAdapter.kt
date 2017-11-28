package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.SearchFilterBean
import kotlinx.android.synthetic.main.pop_search_filter_list_item_tv.view.*
import java.util.*

/**
 * Created by 史忠文
 * on 2017/2/3.
 */

class SearchFilterItemAdapter<T : SearchFilterBean>(private val listener: (position: Int) -> Unit) : BaseQuickAdapter<T, BaseViewHolder>(R.layout.pop_search_filter_list_item_tv, ArrayList<T>()) {


    private var size: Int = 0

    fun setSize(size: Int) {
        this.size = size
    }

    override fun getItemCount(): Int = size

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView = helper.itemView
        itemView.tv.text = entity.value
        if (entity.isCheck) {
            itemView.tv.setBackgroundResource(R.drawable.search_filter_list_item_tv_bg_green_line)
            itemView.tv.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
        } else {
            itemView.tv.setBackgroundResource(R.drawable.search_filter_gold_gray)
            itemView.tv.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
        }
        itemView.tv.textSize = 14f
        itemView.tv.setPadding(SizeUtils.dp2px(5f), SizeUtils.dp2px(3f), SizeUtils.dp2px(5f), SizeUtils.dp2px(3f))
        helper.itemView.setOnClickListener { listener.invoke(helper.adapterPosition) }
    }
}

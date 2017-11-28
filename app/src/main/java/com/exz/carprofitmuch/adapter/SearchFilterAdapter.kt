package com.exz.carprofitmuch.adapter


import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.SearchFilterBean
import com.exz.carprofitmuch.bean.SearchFilterEntity
import kotlinx.android.synthetic.main.pop_search_filter_list_item.view.*
import java.util.*


/**
 * Created by 史忠文
 * on 2017/2/3.
 */

class SearchFilterAdapter<T : SearchFilterEntity> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.pop_search_filter_list_item, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView=helper.itemView
        itemView.title.text = entity.value
        var filterStr = ""
        entity.items
                .filter { it.isCheck }
                .forEach { filterStr += it.value + "," }
        itemView.filter.clearComposingText()
        itemView.filter.text = if (TextUtils.isEmpty(filterStr)) "全部" else filterStr.substring(0, filterStr.length - 1)
        if (entity.isOpen)
            itemView.filter.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.icon_search_triangle_gray_on), null)
        else
            itemView.filter.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.icon_search_triangle_gray_off), null)
        itemView.topLay.setOnClickListener {
            data[helper.adapterPosition - 1].isOpen = !data[helper.adapterPosition - 1].isOpen
            notifyItemChanged(helper.adapterPosition)
        }
        val itemAdapter = SearchFilterItemAdapter<SearchFilterBean>{
            data[helper.adapterPosition - 1].items[it].isCheck = !data[helper.adapterPosition - 1].items[it].isCheck
            notifyItemChanged(helper.adapterPosition)
        }
        itemView.mRecyclerView.isFocusable = false
        itemView.mRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        itemAdapter.setNewData(entity.items)
        itemView.mRecyclerView.adapter = itemAdapter
        itemAdapter.setSize(if (entity.isOpen) entity.items.size else if (entity.items.size <= 3) entity.items.size else 3)
    }
}

package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.FootprintBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import kotlinx.android.synthetic.main.item_footprint.view.*
import java.util.*


class FootprintAdapter<T : FootprintBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_footprint, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView=helper.itemView

        itemView.tv_date.text=entity.sectionName
        if(helper.adapterPosition==0){
            itemView.tv_date.visibility=View.VISIBLE
        }else if(data.get(helper.adapterPosition-1).sectionName.equals(entity.sectionName)){
            itemView.tv_date.visibility=View.GONE
        }

        var mAdapter=ItemFootprintAdapter< FootprintBean.GoodsListBean>()
        mAdapter.bindToRecyclerView(itemView.mRecyclerView)
        mAdapter.setNewData(entity.goodsList)
        itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        itemView.mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
    }
}



package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.ScoreStoreBean
import kotlinx.android.synthetic.main.item_score_store.view.*
import java.util.*
@Deprecated("deprecated")
class ScoreStoreAdapter<T : ScoreStoreBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_score_store, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_title.text = item.title
        itemView.tv_title.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(mContext,item.drawable),null,null,null)
        val mAdapter= ItemScoreStoreAdapter<GoodsBean>()
        mAdapter.bindToRecyclerView(itemView.mRecyclerView)
        itemView.mRecyclerView.isFocusable=false
        itemView.mRecyclerView.layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter.setNewData(item.scoreList)
    }
}
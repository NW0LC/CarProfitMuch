package com.exz.carprofitmuch.adapter

import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsCommentBean
import kotlinx.android.synthetic.main.item_goods_detail_comment.view.*
import java.util.*

class GoodsCommentAdapter<T : GoodsCommentBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_goods_detail_comment, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.img.setImageURI(item.img)
        itemView.tv_userName.text=item.uerName
        itemView.mRatingBar.rating=item.score.toFloat()
        itemView.tv_comment_content.text=item.content
        itemView.tv_comment_time.text=item.time
        itemView.tv_comment_type.text=item.type
        itemView.mRecyclerView.visibility=if (item.imgs.size>0) View.VISIBLE else View.GONE
        val mAdapter= ItemCommentImageAdapter()
        mAdapter.bindToRecyclerView(itemView.mRecyclerView)
        itemView.mRecyclerView.isFocusable=false
        itemView.mRecyclerView.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        mAdapter.setNewData(item.imgs)
    }

}
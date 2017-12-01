package com.exz.carprofitmuch.adapter

import android.support.v7.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CommentBean
import kotlinx.android.synthetic.main.item_service_comment.view.*
import java.util.*

class ServiceCommentAdapter<T : CommentBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_service_comment, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.img.setImageURI(item.userIcon)
        itemView.tv_userName.text=item.uerName
        itemView.tv_commentTime.text=item.commentDate
        itemView.mRatingBar.rating=item.score.toFloat()
        itemView.tv_comment_content.text=item.content
        val mAdapter= ItemCommentImageAdapter()
        mAdapter.bindToRecyclerView(itemView.mRecyclerView)
        itemView.mRecyclerView.isFocusable=false
        itemView.mRecyclerView.layoutManager= StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        mAdapter.setNewData(item.images)
    }

}
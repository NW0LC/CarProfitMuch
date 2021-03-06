package com.exz.carprofitmuch.adapter

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.MyCommentBean
import kotlinx.android.synthetic.main.item_goods_detail_comment.view.*
import java.util.*

class MyCommentAdapter<T : MyCommentBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_my_comment, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        helper.addOnClickListener(R.id.llLay)
        helper.addOnClickListener(R.id.img)
        itemView.img.setImageURI(item.imgUrl)
        itemView.tv_userName.text=item.goodsName
        itemView.mRatingBar.rating=item.score.toFloat()
        itemView.tv_comment_content.text=item.content
        itemView.tv_comment_time.text=item.date
        itemView.mRecyclerView.visibility = if (item.images.size > 0) View.VISIBLE else View.GONE
        val mAdapter = ItemMyCommentImageAdapter()
        mAdapter.bindToRecyclerView(itemView.mRecyclerView)
        itemView.mRecyclerView.isFocusable = false
        itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
        mAdapter.setNewData(item.images)
    }

}
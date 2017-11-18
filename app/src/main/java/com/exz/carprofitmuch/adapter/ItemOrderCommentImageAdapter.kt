package com.exz.carprofitmuch.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.szw.framelibrary.imageloder.GlideApp
import kotlinx.android.synthetic.main.item_comment_order_img.view.*

class ItemOrderCommentImageAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_comment_order_img, ArrayList<String>()) {
    override fun getItemCount(): Int = 5
    override fun convert(helper: BaseViewHolder, item: String) {
        val itemView = helper.itemView
        GlideApp.with(mContext).load(item).into(itemView.imgs)
        itemView.bt_close.visibility=if (helper.adapterPosition==data.size-1) View.GONE else View.VISIBLE
        helper.addOnClickListener(R.id.bt_close)
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30f)) / 5
        layoutParams.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(30f)) / 5
        itemView.layoutParams = layoutParams
    }

}
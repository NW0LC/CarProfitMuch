package com.exz.carprofitmuch.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.szw.framelibrary.imageloder.GlideApp
import com.szw.framelibrary.view.preview.PreviewActivity
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IMAGES
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_POSITION
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_SHOW_NUM
import kotlinx.android.synthetic.main.item_image.view.*

class ItemCommentImageAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_image, ArrayList<String>()) {
    override fun convert(helper: BaseViewHolder, item: String) {
        val itemView = helper.itemView
        GlideApp.with(mContext).load(item).centerCrop().transition(DrawableTransitionOptions.withCrossFade()).into(itemView.img)
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(94f)) / 3
        when {
            helper.adapterPosition % 3 == 0 -> layoutParams.rightMargin = SizeUtils.dp2px(2.5f)
            helper.adapterPosition % 3 == 1 -> {
                layoutParams.leftMargin = SizeUtils.dp2px(2.5f)
                layoutParams.rightMargin = SizeUtils.dp2px(2.5f)
            }
            helper.adapterPosition % 3 == 2 -> layoutParams.leftMargin = SizeUtils.dp2px(2.5f)
        }
        layoutParams.bottomMargin = SizeUtils.dp2px(5f)
        itemView.layoutParams = layoutParams
        itemView.img.setOnClickListener {
            val intent = Intent(mContext, PreviewActivity::class.java)
            intent.putExtra(PREVIEW_INTENT_IMAGES, data as ArrayList<String>)
            intent.putExtra(PREVIEW_INTENT_SHOW_NUM, true)
            intent.putExtra(PREVIEW_INTENT_POSITION, helper.adapterPosition)
            mContext.startActivity(intent)
            (mContext as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

}
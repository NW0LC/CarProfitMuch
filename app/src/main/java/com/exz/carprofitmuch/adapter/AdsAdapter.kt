package com.exz.carprofitmuch.adapter

import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.BannersBean
import kotlinx.android.synthetic.main.item_image.view.*

class AdsAdapter : BaseQuickAdapter<BannersBean, BaseViewHolder>(R.layout.item_image, ArrayList<BannersBean>()) {
    override fun convert(helper: BaseViewHolder, item: BannersBean) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.imgUrl)
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        layoutParams.height = ScreenUtils.getScreenWidth() / 2
        itemView.layoutParams = layoutParams
    }

}
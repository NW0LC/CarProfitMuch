package com.exz.carprofitmuch.adapter

import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import kotlinx.android.synthetic.main.item_image.view.*

class RedPacketTreasureAdapter(val type:Int) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_image, ArrayList<String>()) {
    override fun convert(helper: BaseViewHolder, item: String) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item)

        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        if(type==1){//宝藏
            layoutParams.width = (ScreenUtils.getScreenWidth()  - SizeUtils.dp2px(100f)) / 2
            layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(100f)) / 2
        }else{//红包
            layoutParams.width = (ScreenUtils.getScreenWidth() ) / 2
            layoutParams.height = (ScreenUtils.getScreenWidth()) / 2
        }

        itemView.layoutParams=layoutParams

    }

}
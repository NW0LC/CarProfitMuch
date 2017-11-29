package com.exz.carprofitmuch.adapter

import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.MapIdBean
import kotlinx.android.synthetic.main.item_image.view.*

class RedPacketTreasureAdapter(val type:Int) : BaseQuickAdapter<MapIdBean, BaseViewHolder>(R.layout.item_image, ArrayList<MapIdBean>()) {
    override fun convert(helper: BaseViewHolder, item: MapIdBean) {
        val itemView = helper.itemView


        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        if(type==1){//宝藏
            itemView.img.setImageURI("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
            layoutParams.width = (ScreenUtils.getScreenWidth()  - SizeUtils.dp2px(100f)) / 2
            layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(100f)) / 2
        }else{//红包
            layoutParams.width = (ScreenUtils.getScreenWidth() ) / 2
            layoutParams.height = (ScreenUtils.getScreenWidth()) / 2
            itemView.img.setImageURI( "res://com.exz.carprofitmuch/" + R.mipmap.icon_map_red_packet3)
        }
        itemView.layoutParams=layoutParams

    }

}
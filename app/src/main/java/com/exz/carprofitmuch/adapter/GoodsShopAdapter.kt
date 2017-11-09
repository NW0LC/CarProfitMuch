package com.exz.carprofitmuch.adapter

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import kotlinx.android.synthetic.main.item_goods_shop.view.*
import java.util.*

class GoodsShopAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_goods_shop, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.img.layoutParams.height=(ScreenUtils.getScreenWidth() - SizeUtils.dp2px(5f))/2
        itemView.img.setImageURI(item.img)
        itemView.tv_title.text=item.title
        itemView.tv_price.text=String.format("${mContext.getString(R.string.CNY)}%s",item.price)
        itemView.tv_oldPrice.text= String.format("${mContext.getString(R.string.CNY)}%s",item.oldPrice)
        itemView.tv_oldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        if (helper.adapterPosition%2==0) {
            layoutParams.rightMargin= SizeUtils.dp2px(2.5f)
        }else{
            layoutParams.leftMargin= SizeUtils.dp2px(2.5f)
        }
        layoutParams.bottomMargin= SizeUtils.dp2px(5f)
        itemView.layoutParams= layoutParams

    }
}
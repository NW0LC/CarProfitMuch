package com.exz.carprofitmuch.adapter

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import kotlinx.android.synthetic.main.item_main_store_goods.view.*
import java.util.*

class MainStoreAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main_store_goods, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.img.setImageURI(item.img)
        itemView.tv_title.text=item.title
        itemView.tv_old_price.text= String.format("${mContext.getString(R.string.CNY)}%s",item.oldPrice)
        itemView.tv_old_price.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
        itemView.tv_price.text=String.format("${mContext.getString(R.string.CNY)}%s",item.price)
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        headerLayoutCount
        if (if (headerLayoutCount==0)helper.adapterPosition%2==1 else helper.adapterPosition%2==0) {
            layoutParams.leftMargin=SizeUtils.dp2px(2.5f)
        }else{
            layoutParams.rightMargin=SizeUtils.dp2px(2.5f)
        }
        layoutParams.bottomMargin=SizeUtils.dp2px(5f)
        itemView.layoutParams= layoutParams

    }
}
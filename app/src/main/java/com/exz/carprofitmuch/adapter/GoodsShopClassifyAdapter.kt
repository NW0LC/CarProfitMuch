package com.exz.carprofitmuch.adapter


import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsShopClassifyBean
import kotlinx.android.synthetic.main.item_goods_shop_classify.view.*

class GoodsShopClassifyAdapter<T : GoodsShopClassifyBean>(private val itemClickListener:(position:Int,itemPosition:Int)->Unit) : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_goods_shop_classify, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.classifyName.text=item.value
        itemView.classifyName.setTextColor(if (item.isCheck) ContextCompat.getColor(mContext,R.color.colorPrimary) else ContextCompat.getColor(mContext,R.color.MaterialGrey700))
        itemView.line_select.visibility=if (item.isCheck) View.VISIBLE else View.INVISIBLE
        itemView.line_select_bottom.visibility=if (item.list.size>0) View.GONE else View.VISIBLE

        itemView.classifyNext.text=if (item.list.size>0) mContext.getString(R.string.main_see_all) else ""
        itemView.classifyNext.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,if (item.list.size<=0) ContextCompat.getDrawable(mContext,R.mipmap.icon_main_gray_next) else null,null)

        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin= SizeUtils.dp2px(if (helper.adapterPosition==0) 10f else 0f)
        layoutParams.bottomMargin= SizeUtils.dp2px(if (helper.adapterPosition==0) 10f else 0f)
        itemView.layoutParams= layoutParams


        val adapter= ItemGoodsShopClassifyAdapter<GoodsShopClassifyBean>()
        itemView.mRecyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        itemView.mRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        itemView.mRecyclerView.isFocusable=false
        adapter.bindToRecyclerView(itemView.mRecyclerView)
        adapter.setNewData(item.list)
        adapter.setOnItemClickListener { _, _, position ->
            itemClickListener.invoke(helper.adapterPosition,position)
        }
    }

}

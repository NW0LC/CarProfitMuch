package com.exz.carprofitmuch.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity.Companion.GoodsDetail_Intent_GoodsId
import com.exz.carprofitmuch.module.main.store.score.ScoreGoodsDetailActivity
import kotlinx.android.synthetic.main.item_item_score_store.view.*
import java.util.*

class ItemScoreStoreAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_score_store, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.img.layoutParams.height=(ScreenUtils.getScreenWidth()-SizeUtils.dp2px(5f))/2
        itemView.img.setImageURI(item.imgUrl)
        itemView.tv_title.text=item.title
        itemView.tv_info.text= item.subTitle
        itemView.tv_price.text=String.format("%s${mContext.getString(R.string.SCORE)}",item.price)
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        if (if (headerLayoutCount==0)helper.adapterPosition%2==1 else helper.adapterPosition%2==0) {
            layoutParams.leftMargin=SizeUtils.dp2px(2.5f)
        }else{
            layoutParams.rightMargin=SizeUtils.dp2px(2.5f)
        }
        layoutParams.bottomMargin=SizeUtils.dp2px(5f)
        itemView.layoutParams= layoutParams
        itemView.setOnClickListener{
            val intent = Intent(mContext, ScoreGoodsDetailActivity::class.java)
            intent.putExtra(GoodsDetail_Intent_GoodsId,item.id)
            mContext.startActivity(intent)
        }
    }
}
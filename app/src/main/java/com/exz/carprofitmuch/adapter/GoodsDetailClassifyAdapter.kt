package com.exz.carprofitmuch.adapter

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsClassifyBean
import com.exz.carprofitmuch.bean.GoodsSubClassifyBean
import com.hwangjr.rxbus.RxBus
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.item_goods_detail_classify.view.*
import java.util.*

class GoodsDetailClassifyAdapter<T : GoodsClassifyBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_goods_detail_classify, ArrayList<T>()) {



    override fun convert(baseViewHolder: BaseViewHolder, entity: T) {
        val itemView = baseViewHolder.itemView
        itemView.classifyName.text = entity.rankName
        val adapter = object : TagAdapter<GoodsSubClassifyBean>(entity.subRank) {

            override fun getView(parent: FlowLayout, position: Int, classifyBean: GoodsSubClassifyBean): View {
                val textView = mLayoutInflater.inflate(R.layout.tag_classify, parent, false) as TextView
                textView.text = classifyBean.rankName
                when (classifyBean.goodsSubState) {
                //1 on  2 off 3 pass
                    "1" -> {
                        textView.setBackgroundResource(R.drawable.tag_classify_can)
                        textView.setPadding(SizeUtils.dp2px(15f), SizeUtils.dp2px(3f), SizeUtils.dp2px(15f), SizeUtils.dp2px(3f))
                    }
                    "2" -> {
                        textView.setBackgroundResource(R.drawable.tag_classify_can)
                        textView.setPadding(SizeUtils.dp2px(15f), SizeUtils.dp2px(3f), SizeUtils.dp2px(15f), SizeUtils.dp2px(3f))
                    }
                }
                return textView
            }
        }
        itemView.tagFlow.adapter = adapter
        for (i in 0 until entity.subRank.size) {
            if ("1" == entity.subRank[i].goodsSubState) {
                adapter.setSelectedList(i)
                break
            }
        }
        itemView.tagFlow.setOnTagClickListener(TagFlowLayout.OnTagClickListener { _, position, _ ->
            if (itemView.tagFlow.selectedList.size != 0) {
                entity.subRank.filter { "1" == it.goodsSubState }.forEach { it.goodsSubState = "2" }
                entity.subRank[position].goodsSubState = "1"
            } else {
                notifyDataSetChanged()
                return@OnTagClickListener false
            }
            RxBus.get().post(GoodsDetailClassify, GoodsDetailClassify)
            false
        })
    }
    companion object {
        const val GoodsDetailClassify="GoodsDetailClassify"
    }
}
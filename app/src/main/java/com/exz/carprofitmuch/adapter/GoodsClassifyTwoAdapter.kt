package com.exz.carprofitmuch.adapter

import android.support.v7.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsClassifyTwoEntities
import kotlinx.android.synthetic.main.adapter_goods_classify_two.view.*

/**
 * Created by pc
 * on 2017/4/21.
 */

class GoodsClassifyTwoAdapter : BaseQuickAdapter<GoodsClassifyTwoEntities, BaseViewHolder>(R.layout.adapter_goods_classify_two, null) {

    override fun convert(helper: BaseViewHolder, item: GoodsClassifyTwoEntities) {
        val itemView = helper.itemView
        itemView.erjiname.text = item.typeName
        val adapter = GoodsClassifyTwoGridAdapter()
        itemView.mRecyclerView.layoutManager = GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false)
        adapter.setNewData(item.thirdType)
        itemView.mRecyclerView.adapter = adapter
    }

}

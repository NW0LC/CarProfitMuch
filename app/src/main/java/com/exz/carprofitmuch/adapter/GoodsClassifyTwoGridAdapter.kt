package com.exz.carprofitmuch.adapter

import android.content.Intent
import android.widget.LinearLayout
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsClassifyTwoEntities
import com.exz.carprofitmuch.module.main.store.search.SearchFilterActivity
import kotlinx.android.synthetic.main.adapter_goods_classify_two_grid.view.*

/**
 * Created by pc
 * on 2017/4/21.
 */

class GoodsClassifyTwoGridAdapter : BaseQuickAdapter<GoodsClassifyTwoEntities.ThirdTypeEntity, BaseViewHolder>(R.layout.adapter_goods_classify_two_grid, null) {

    override fun convert(helper: BaseViewHolder, item: GoodsClassifyTwoEntities.ThirdTypeEntity) {
        val itemView = helper.itemView
        val para = itemView.sanjiimage.layoutParams as LinearLayout.LayoutParams// 获取按钮的布局
        para.height = (ScreenUtils.getScreenWidth()-SizeUtils.dp2px(140f))/3
        itemView.sanjiimage.layoutParams = para
        itemView.sanjiimage.setImageURI(item.imgUrl)
        itemView.sanjiname.text = item.typeName
        itemView.sanjiimage.setOnClickListener{
            val intent = Intent(mContext, SearchFilterActivity::class.java)
            intent.putExtra("typeId", item.typeId)
            //                intent.putExtra("searchContent", item.getTypeName());
            mContext.startActivity(intent)
        }
    }

}

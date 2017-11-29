package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.OpenShopKeyValueBean
import kotlinx.android.synthetic.main.item_open_shop_list.view.*
import org.jetbrains.anko.textColor

/**
 * Created by pc on 2017/11/23.
 */

class OpenShopkeyValueAdapter() : BaseQuickAdapter<OpenShopKeyValueBean, BaseViewHolder>(R.layout.item_open_shop_list, null) {

    override fun convert(helper: BaseViewHolder, item: OpenShopKeyValueBean) {
        val v = helper.itemView
        v.tv_name.text=item.title //
        if(item.check){
            v.tv_name.textColor=ContextCompat.getColor(mContext,R.color.colorPrimary)
            v.tv_name.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mContext,R.mipmap.icon_mine_open_shop_select),null)
        }else{
            v.tv_name.textColor=ContextCompat.getColor(mContext,R.color.MaterialGrey800)

            v.tv_name.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mContext,R.mipmap.icon_mine_open_shop_noselect),null)
        }
    }
}

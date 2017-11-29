package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.OpenShopListBean
import kotlinx.android.synthetic.main.item_open_shop.view.*


/**
 * Created by pc on 2017/11/23.
 */

class OpenShopAdapter : BaseQuickAdapter<OpenShopListBean, BaseViewHolder>(R.layout.item_open_shop, null) {
    override fun convert(helper: BaseViewHolder, item: OpenShopListBean) {
        val v = helper.itemView
        v.tv_key.text = item.k
        v.tv_value.text = item.v
        when (item.state) {
            "1" -> {//未提交 未填写 灰色
                v.tv_value.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey400))
            }
            "2"->{//未提交 已填写 黑色
                v.tv_value.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey800))
            }
            "3" -> { //已提交 审核被拒 红色
                v.tv_value.setTextColor(ContextCompat.getColor(mContext, R.color.Red))
            }
            "4" ->{//已提交 已填写 已修改 黑色
                v.tv_value.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey800))
            }
        }
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        if(helper.adapterPosition==4||helper.adapterPosition==7){
            lp.setMargins(0, 15, 0, 0)
        }else{
            lp.setMargins(0, 0, 0, 0)
        }
        v.layoutParams = lp
    }
}

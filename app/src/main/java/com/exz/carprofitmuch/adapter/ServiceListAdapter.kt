package com.exz.carprofitmuch.adapter

import android.graphics.Paint
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ServiceShopBean
import com.exz.carprofitmuch.utils.SZWUtils
import kotlinx.android.synthetic.main.item_service_list.view.*
import kotlinx.android.synthetic.main.layout_service_goods.view.*
import java.util.*

class ServiceListAdapter<T : ServiceShopBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_service_list, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.shopIcon)
        itemView.tv_service_store_name.text = item.shopName
        itemView.mRatingBar.rating = item.score.toFloat()
        itemView.tv_service_store_score.text = String.format("%s${mContext.getString(R.string.UNIT_SCORE)}", item.score)
        itemView.tv_service_store_info.text = item.categoryName
        itemView.tv_service_store_address.text = String.format("%s"+mContext.getString(R.string.KM),item.district+item.distance)
        itemView.lay_serviceGoods.removeAllViews()
        for (serviceGood in item.goodsList) {
            val inflate = View.inflate(mContext, R.layout.layout_service_goods, null)
            inflate.tv_service_goods_price.text =String.format("${mContext.getString(R.string.CNY)}%s", serviceGood.goodsPrice)
            inflate.tv_service_goods_oldPrice.text=String.format("${mContext.getString(R.string.service_goods_oldPrice)}%s",serviceGood.oldPrice)
            inflate.tv_service_goods_oldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
            inflate.tv_service_goodsName.text = serviceGood.goodsName
            inflate.tv_service_goodsSoldCount.text = String.format("${mContext.getString(R.string.service_list_serviceGoods_soldCount)}%s", SZWUtils.getSoldCount(mContext,serviceGood.saleCount))
            itemView.lay_serviceGoods.addView(inflate)
            inflate.setOnClickListener{
                //跳转服务详情
            }
        }

    }


}
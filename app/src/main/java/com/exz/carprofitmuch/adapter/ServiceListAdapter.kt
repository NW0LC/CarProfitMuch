package com.exz.carprofitmuch.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ServiceStoreBean
import com.exz.carprofitmuch.utils.SZWUtils
import kotlinx.android.synthetic.main.item_service_list.view.*
import kotlinx.android.synthetic.main.layout_service_goods.view.*
import java.util.*

class ServiceListAdapter<T : ServiceStoreBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_service_list, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.img)
        itemView.tv_service_store_name.text = item.storeName
        itemView.mRatingBar.rating = item.score.toFloat()
        itemView.tv_service_store_score.text = String.format("${mContext.getString(R.string.SCORE)}%s", item.score)
        itemView.tv_service_store_info.text = item.info
        itemView.tv_service_store_address.text = item.address
        itemView.lay_serviceGoods.removeAllViews()
        for (serviceGood in item.serviceGoods) {
            val inflate = View.inflate(mContext, R.layout.layout_service_goods, null)
            inflate.tv_service_goods_price.text =String.format("${mContext.getString(R.string.CNY)}%s", serviceGood.price)
            inflate.tv_service_offerPrice.text = String.format(mContext.getString(R.string.service_list_serviceGoods_offerPrice), serviceGood.offerPrice)
            inflate.tv_service_goodsName.text = serviceGood.name
            inflate.tv_service_goodsSoldCount.text = String.format("${mContext.getString(R.string.service_list_serviceGoods_soldCount)}%s", SZWUtils.getSoldCount(mContext,serviceGood.soldCount))
            itemView.lay_serviceGoods.addView(inflate)
            inflate.setOnClickListener{
                //跳转服务详情
            }
        }

    }


}
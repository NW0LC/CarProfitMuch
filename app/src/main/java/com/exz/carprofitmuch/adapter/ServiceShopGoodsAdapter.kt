package com.exz.carprofitmuch.adapter

import android.graphics.Paint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.utils.SZWUtils.getSoldCount
import kotlinx.android.synthetic.main.item_service_shop_goods.view.*
import java.util.*

class ServiceShopGoodsAdapter<T : ServiceGoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_service_shop_goods, ArrayList<T>()) {
    var goodsCount=3
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
//        itemView.img.setImageURI(item.img)
        itemView.tv_service_goodsName.text=item.goodsName
        if (item.payMark=="1") {
            itemView.tv_service_goodsPrice.text=String.format("%s"+mContext.getString(R.string.SCORE), item.goodsPrice)
            itemView.tv_service_goodsOldPrice.text=String.format("%s"+mContext.getString(R.string.SCORE), item.oldPrice)
        }else{
            itemView.tv_service_goodsPrice.text=String.format("%s"+mContext.getString(R.string.CNY), item.goodsPrice)
            itemView.tv_service_goodsOldPrice.text=String.format("%s"+mContext.getString(R.string.service_goods_oldPrice), item.oldPrice)
        }
        itemView.tv_service_goodsOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
        itemView.tv_service_goodsSoldCount.text=String.format("${mContext.getString(R.string.service_list_serviceGoods_soldCount)}%s", getSoldCount(mContext,item.saleCount))

    }

    override fun getItemCount(): Int = if (data.size<goodsCount)data.size else goodsCount
}
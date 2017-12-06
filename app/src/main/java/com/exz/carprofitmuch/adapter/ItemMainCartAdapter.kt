package com.exz.carprofitmuch.adapter

import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.module.CartFragment
import com.exz.carprofitmuch.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_main_cart.*
import kotlinx.android.synthetic.main.item_item_main_cart.view.*
import kotlinx.android.synthetic.main.layout_main_cart.view.*
import java.util.*


class ItemMainCartAdapter<T : GoodsBean>(private var context: CartFragment, private var parentAdapter: MainCartAdapter<*>) : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_main_cart, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        helper.addOnClickListener(R.id.radioButton)
        helper.addOnClickListener(R.id.add)
        helper.addOnClickListener(R.id.minus)
        helper.addOnClickListener(R.id.count)
        helper.addOnClickListener(R.id.lay_main_cart)
        itemView.goodsName.text = item.goodsName
        itemView.count.text = if (TextUtils.isEmpty(item.goodsCount)) "1" else if (item.goodsCount.toDouble() == 0.toDouble()) "1" else item.goodsCount
        itemView.goodsChooseClassify.text = item.goodsType
        itemView.tv_price.text = item.goodsPrice
        itemView.img.setImageURI(item.imgUrl)
        itemView.radioButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, if (item.isCheck) R.mipmap.icon_mine_open_shop_select else R.mipmap.icon_mine_open_shop_unselect), null, null, null)
        itemView.btnDelete.setOnClickListener {
            DialogUtils.delete(mContext) {
                CartFragment.deleteCar(mContext, arrayListOf(item)) {
                    CartFragment.removeItem(context, parentAdapter, this, it)
                    CartFragment.checkSelectAll(parentAdapter, context.select_all)
                }
            }
        }
    }
}
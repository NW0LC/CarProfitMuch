package com.exz.carprofitmuch.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsShopBean
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Delete
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Edit
import com.exz.carprofitmuch.module.mine.favorite.FavoriteShopActivity.Companion.removeItem
import com.exz.carprofitmuch.utils.DialogUtils
import kotlinx.android.synthetic.main.item_favorite_shop.view.*
import kotlinx.android.synthetic.main.item_item_favorite_shop.view.*
import java.util.*

class FavoriteShopAdapter<T : GoodsShopBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_favorite_shop, ArrayList<T>()) {
    var animatorEnable = true
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.img)
        itemView.tv_goodsShop_name.text = item.shopName
        itemView.tv_goodsShop_type.text = item.shopType


        val animatorIn = ObjectAnimator.ofFloat(itemView.constraintLayout, "translationX", 0f, SizeUtils.dp2px(45f).toFloat())
        val animatorExit = ObjectAnimator.ofFloat(itemView.constraintLayout, "translationX", SizeUtils.dp2px(45f).toFloat(), 0f)
        animatorExit.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                itemView.radioButton.visibility = View.INVISIBLE
            }
        })
        if (animatorEnable) {
            animatorIn.duration = 200
            animatorExit.duration = 200
        } else {
            animatorIn.duration = 0
            animatorExit.duration = 0
        }
        if (Edit_Type == Edit_Type_Delete) {
            itemView.radioButton.visibility = View.VISIBLE
            animatorIn.start()
        } else if (Edit_Type == Edit_Type_Edit) {
            animatorExit.start()
        }
        itemView.radioButton.isChecked = item.isCheck
        itemView.btnDelete.setOnClickListener {
            DialogUtils.delete(mContext) {
                DataCtrlClass.favoriteShopIsCollection(mContext, "1", arrayOf(item)) {
                    removeItem(this@FavoriteShopAdapter, it)
                }
            }
        }
    }
}
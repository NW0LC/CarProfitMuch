package com.exz.carprofitmuch.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.support.v4.content.ContextCompat
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.module.CartFragment
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity
import com.exz.carprofitmuch.utils.DialogUtils
import kotlinx.android.synthetic.main.item_item_item_main_cart_lose.view.*
import kotlinx.android.synthetic.main.item_item_main_cart.view.*
import java.util.*

class ItemMainCartLoseAdapter<T : GoodsBean>(private var context: CartFragment, private var parentAdapter: MainCartAdapter<*>,private var animatorEnable :Boolean) : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_item_main_cart_lose, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        helper.addOnClickListener(R.id.radioButton)
        helper.addOnClickListener(R.id.lay_main_cart)
        itemView.tv_goodsName.text = item.title
        itemView.img.setImageURI(item.imgUrl)

        itemView.radioButton.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, if (item.isCheck) R.mipmap.icon_service_pay_select_on else R.mipmap.icon_service_pay_select_off), null, null, null)

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
        if (FavoriteGoodsActivity.Edit_Type == FavoriteGoodsActivity.Edit_Type_Delete) {
            itemView.radioButton.visibility = View.VISIBLE
            animatorIn.start()
        } else if (FavoriteGoodsActivity.Edit_Type == FavoriteGoodsActivity.Edit_Type_Edit) {
            animatorExit.start()
        }


        itemView.btnDelete.setOnClickListener {
            DialogUtils.delete(mContext) {
                //                CartFragment.deleteCar(mContext, arrayListOf(item)) {
                CartFragment.removeItem(context, parentAdapter, this, listOf(item))
//                }
            }
        }
    }
}
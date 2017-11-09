package com.exz.carprofitmuch.pop

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsShopClassifyAdapter
import com.exz.carprofitmuch.bean.GoodsShopClassifyBean
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopSearchResultActivity
import kotlinx.android.synthetic.main.action_bar_custom.view.*
import kotlinx.android.synthetic.main.pop_goods_shop_classify.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * Created by 史忠文
 * on 2017/11/1.
 */
class GoodsShopClassifyPop(context: Activity) : BasePopupWindow(context) {

    companion object {
        var shopClassifyId = ""
    }

    var data = ArrayList<GoodsShopClassifyBean>()
        set(value) {
            field = value
            for (goodsShopClassifyBean in value) {
                if (shopClassifyId == goodsShopClassifyBean.id) {
                    goodsShopClassifyBean.isCheck = true
                    break
                }
                for (classifyBean in goodsShopClassifyBean.list) {
                    if (shopClassifyId == classifyBean.id) {
                        classifyBean.isCheck = true
                        break
                    }
                }
            }
            mAdapter.setNewData(value)
        }
    private var mAdapter: GoodsShopClassifyAdapter<GoodsShopClassifyBean>

    init {
        initToolBar()
        isDismissWhenTouchOuside = true
        popupWindowView.mRecyclerView.layoutManager = LinearLayoutManager(context)
        popupWindowView.mRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mAdapter = GoodsShopClassifyAdapter { position, itemPosition ->
            selectItem(position, itemPosition)
        }
        mAdapter.bindToRecyclerView(popupWindowView.mRecyclerView)
        mAdapter.setOnItemClickListener({ _, _, position ->
            selectItem(position, null)
        })
    }

    private fun selectItem(position: Int, itemPosition: Int?) {
        data.forEach {
            it.isCheck = false
            it.list.forEach {
                it.isCheck = false
            }
        }
        if (itemPosition == null) {
            data[position].isCheck = true
            shopClassifyId = data[position].id
        } else {
            data[position].list[itemPosition].isCheck = true
            shopClassifyId = data[position].list[itemPosition].id
        }
        mAdapter.notifyDataSetChanged()

        if (context is GoodsShopSearchResultActivity)
            dismiss()
        else
            context.startActivity(Intent(context, GoodsShopSearchResultActivity::class.java))
    }

    private fun initToolBar() {
        popupWindowView.mTitle.text = context.getString(R.string.goods_shop_classifyName)
        popupWindowView.toolbar.setNavigationOnClickListener { dismiss() }
    }


    override fun initShowAnimation(): Animation {
        val set = AnimationSet(false)
        val showAnimation = TranslateAnimation(ScreenUtils.getScreenWidth().toFloat(), 0f, 0f, 0f)
//        shakeAnimation.interpolator = CycleInterpolator(5f)//循环几次
        showAnimation.duration = 400
//        set.addAnimation(defaultAlphaAnimation)
        set.addAnimation(showAnimation)
        return set
    }

    override fun initExitAnimation(): Animation {
        val set = AnimationSet(false)
        val showAnimation = TranslateAnimation(0f, ScreenUtils.getScreenWidth().toFloat(), 0f, 0f)
//        shakeAnimation.interpolator = CycleInterpolator(5f)//循环几次
        showAnimation.duration = 400
//        val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
//        alphaAnimation.duration = 300L
//        alphaAnimation.interpolator = AccelerateInterpolator()
//        set.addAnimation(alphaAnimation)
        set.addAnimation(showAnimation)
        return set
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View = createPopupById(R.layout.pop_goods_shop_classify)

    override fun initAnimaView(): View = findViewById(R.id.popup_animation)


}
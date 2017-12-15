package com.exz.carprofitmuch.pop

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.SearchFilterAdapter
import com.exz.carprofitmuch.bean.SearchFilterEntity
import com.exz.carprofitmuch.utils.RecycleViewDivider
import kotlinx.android.synthetic.main.header_search_goods_filter_price.view.*
import kotlinx.android.synthetic.main.pop_search_filter.view.*
import razerdp.basepopup.BasePopupWindow


/**
 * Created by 史忠文
 * on 2017/1/24.
 */

class SearchFilterPop(context: Activity) : BasePopupWindow(context), View.OnFocusChangeListener, View.OnClickListener {

    var lowPrice: String=""
    var heightPrice: String=""
    var  mAdapter: SearchFilterAdapter<SearchFilterEntity> = SearchFilterAdapter()
    private lateinit var inflate: View
    private var headerView: View = View.inflate(context, R.layout.header_search_goods_filter_price, null)

    init {
        popupWindow.isClippingEnabled = false
        setDismissWhenTouchOutside(true)
        mAdapter.setHeaderView(headerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(inflate.mRecyclerView)
        inflate.mRecyclerView.layoutManager = LinearLayoutManager(getContext())
        inflate.mRecyclerView.addItemDecoration(RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(getContext(), R.color.white)))
        headerView.heightPrice.onFocusChangeListener = this
        headerView.lowPrice.onFocusChangeListener = this
    }

    fun setData(list: List<SearchFilterEntity>) {
        mAdapter.setNewData(list)
    }

    override fun dismiss() {
        super.dismiss()
        lowPrice = if (TextUtils.isEmpty(headerView.lowPrice.text.toString().trim { it <= ' ' })) "" else headerView.lowPrice.text.toString().trim { it <= ' ' }
        heightPrice = if (TextUtils.isEmpty(headerView.heightPrice.text.toString().trim { it <= ' ' })) "" else headerView.heightPrice.text.toString().trim { it <= ' ' }
    }

    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View {
        inflate = View.inflate(context, R.layout.pop_search_filter, null)
        inflate.clear.setOnClickListener(this)
        inflate.confirm.setOnClickListener(this)
        return inflate
    }

    override fun initAnimaView(): View = findViewById(R.id.animationView)

    override fun initShowAnimation(): Animation {
        val shakeAnimate = TranslateAnimation((ScreenUtils.getScreenWidth() - SizeUtils.dp2px(60f)).toFloat(), 0f, 0f, 0f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    override fun initExitAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(60f)).toFloat(), 0f, 0f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (!hasFocus) {
            KeyboardUtils.hideSoftInput(v)
        }
    }
    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.clear -> {
                mAdapter.data.flatMap { it.items }.forEach { it.isCheck = false }
                mAdapter.notifyDataSetChanged()
            }
            R.id.confirm -> dismiss()
        }
    }

}

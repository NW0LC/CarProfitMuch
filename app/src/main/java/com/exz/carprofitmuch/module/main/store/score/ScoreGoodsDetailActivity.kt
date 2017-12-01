package com.exz.carprofitmuch.module.main.store.score

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v4.widget.NestedScrollView
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop.Companion.SCORE_STATE_NORMAL
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop.Companion.STATE_NORMAL
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_goods_detail.*
import kotlinx.android.synthetic.main.layout_banner.*


/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ScoreGoodsDetailActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {


    private var mScrollY = 0
    private lateinit var classifyPop:GoodsDetailClassifyPop

    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.score_goods_detail_goodsName)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbar.alpha = 1 - Math.min(percent, 1f)
                bottom_bar.visibility=View.VISIBLE
            }
        })
        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0
            private val h = DensityUtil.dp2px(170f)

            private var differY = 0f
            private var bottomBarH = DensityUtil.dp2px(45f)
            private var bottomBarY = (ScreenUtils.getScreenHeight() - bottomBarH).toFloat()
            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                var scrollNewY = scrollY
                if (lastScrollY < h) {
                    scrollNewY = Math.min(h, scrollNewY)
                    mScrollY = if (scrollNewY > h) h else scrollNewY
                    buttonBarLayout.alpha = 1f * mScrollY / h
                    blurView.alpha = 1f * mScrollY / h
                }
                lastScrollY = scrollNewY

                val differ = scrollY - oldScrollY
                differY = if ((differY + differ) in 0..bottomBarH) {
                    differY + differ
                } else {
                    when {differY + differ > bottomBarH -> bottomBarH.toFloat()
                        differY + differ < 0 -> 0f
                        else -> 0f
                    }
                }
                val nextY = bottomBarY + differY
                val y = ObjectAnimator.ofFloat(bottom_bar, "y", bottom_bar.y, nextY)
                val animatorSet = AnimatorSet()
                animatorSet.play(y)
                animatorSet.duration = 0
                animatorSet.start()
            }

        })
        buttonBarLayout.alpha = 0f
        blurView.alpha = 0f
        return false
    }
    override fun setInflateId(): Int= R.layout.activity_score_goods_detail

    override fun init() {
        classifyPop= GoodsDetailClassifyPop(this){
            tv_goodsType.text=it
        }
        STATE_NORMAL= SCORE_STATE_NORMAL

        initBanner()
        initEvent()
        mWebView.loadUrl("http://www.baidu.com")
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_choose_type.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
    }


    private fun initBanner() {
        val bannersBean = ArrayList<BannersBean>()
        bannersBean.add(BannersBean())
        bannersBean.add(BannersBean())
        bannersBean.add(BannersBean())
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(BannerImageLoader())
        //设置图片集合
        banner.setImages(bannersBean)
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //banner设置方法全部调用完毕时最后调用
        banner.start()

    }
    override fun onClick(p0: View?) {
        when (p0) {
            bt_choose_type -> {
                classifyPop.showPopupWindow()
            }
            bt_confirm -> {
//                val intent = Intent()
//                intent.putExtra(Intent_ClassName,ScoreConfirmActivity::class.java.name)
//                SZWUtils.checkLogin(this, intent)
                startActivity(Intent(this, ScoreConfirmActivity::class.java))
            }

        }
    }
    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreGoodsDetailData(this,"") {
            if (it != null) {
            }
        }
    }
    companion object {
        val ScoreGoods_Intent_ScoreGoodsId="ScoreGoods_Intent_ScoreGoodsId"
    }
}
package com.exz.carprofitmuch.module.main.store.score

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsCommentAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.CommentBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity.Companion.GoodsDetail_Intent_GoodsId
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop.Companion.SCORE_STATE_NORMAL
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop.Companion.STATE_NORMAL
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.view.preview.PreviewActivity
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IMAGES
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_POSITION
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_SHOW_NUM
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_goods_detail.*
import kotlinx.android.synthetic.main.layout_banner.*
import org.jetbrains.anko.backgroundColor


/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ScoreGoodsDetailActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {


    private var mScrollY = 0
    private lateinit var classifyPop:GoodsDetailClassifyPop
    private lateinit var mGoodsCommentAdapter: GoodsCommentAdapter<CommentBean>
    private var goodsBean: GoodsBean? = null
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
        refreshLayout.setOnRefreshListener(this)
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
        initRecycler()
        mWebView.loadUrl("${Urls.url}App/H5/GoodsInfo.aspx?id=${intent.getStringExtra(GoodsDetail_Intent_GoodsId)}")
        onRefresh(refreshLayout)
    }
    private fun initRecycler() {
        mGoodsCommentAdapter = GoodsCommentAdapter()
        mGoodsCommentAdapter.bindToRecyclerView(mCommentRecyclerView)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(this)
        mCommentRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
        mCommentRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mCommentRecyclerView.isNestedScrollingEnabled = false
        mCommentRecyclerView.isFocusable = false

    }
    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_choose_type.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
    }


    private fun initBanner() {
        banner.layoutParams.height=ScreenUtils.getScreenWidth()
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(BannerImageLoader())
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.setOnBannerListener {
            if (goodsBean != null) {
                val intent = Intent(mContext, PreviewActivity::class.java)
                intent.putExtra(PREVIEW_INTENT_IMAGES, goodsBean?.mainImgs)
                intent.putExtra(PREVIEW_INTENT_SHOW_NUM, true)
                intent.putExtra(PREVIEW_INTENT_POSITION, it)
                mContext.startActivity(intent)
            }
        }
    }
    override fun onClick(p0: View?) {
        when (p0) {
            bt_choose_type -> {
                classifyPop.showPopupWindow()
            }
            bt_confirm -> {
                classifyPop.showPopupWindow()
            }

        }
    }
    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.goodsDetailData(this, intent.getStringExtra(GoodsDetail_Intent_GoodsId) ?: "") {
            if (it != null) {
                goodsBean = it
                //设置图片集合
                val banners = ArrayList<BannersBean>()
                it.mainImgs.mapTo(banners) { BannersBean(it) }
                banner.setImages(banners)
                //banner设置方法全部调用完毕时最后调用
                banner.start()


                tv_goodsName.text = it.goodsName
                tv_score_count.text = String.format("%s"+getString(R.string.SCORE)  , it.goodsPrice)
                bt_choose_type.visibility = if (it.isCoupon == "1") View.VISIBLE else View.GONE
                tv_goodsType.visibility = if (it.isHaveRank == "1") View.VISIBLE else View.GONE
                bt_more_goodsComment.text = String.format(getString(R.string.goods_detail_comment), it.commentCount)
                mGoodsCommentAdapter.setNewData(it.commentList)

                if (it.isDelete == "1") {
                    bt_confirm.text = getString(R.string.goods_detail_pass)
                    bt_confirm.backgroundColor= ContextCompat.getColor(mContext,R.color.MaterialGrey600)
                    bt_confirm.isClickable=false
                }
                DataCtrlClass.goodsClassifyData(this, intent.getStringExtra(GoodsDetail_Intent_GoodsId) ?: "") { spec ->
                    classifyPop.setNewData(spec, it)
                }
            }
        }
    }
}
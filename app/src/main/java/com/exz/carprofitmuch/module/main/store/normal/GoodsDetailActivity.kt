package com.exz.carprofitmuch.module.main.store.normal

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
import com.exz.carprofitmuch.bean.*
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity
import com.exz.carprofitmuch.pop.CouponPop
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop
import com.exz.carprofitmuch.pop.RedPacketPop
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_goods_detail.*
import kotlinx.android.synthetic.main.layout_banner.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class GoodsDetailActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {


    private var mScrollY = 0
    private lateinit var classifyPop: GoodsDetailClassifyPop
    private lateinit var couponPop: CouponPop
    private lateinit var redPacketPop: RedPacketPop
    private lateinit var mGoodsCommentAdapter: GoodsCommentAdapter<CommentBean>
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.goods_detail_name)
        toolbar.setNavigationIcon(R.mipmap.icon_goods_detail_back)
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
                bottom_bar.visibility= View.VISIBLE
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
                    toolbar.setNavigationIcon(if (lastScrollY>70)R.mipmap.icon_arrow_white_back else R.mipmap.icon_goods_detail_back)
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
    override fun setInflateId(): Int= R.layout.activity_goods_detail

    override fun init() {
        classifyPop= GoodsDetailClassifyPop(this) {
            tv_goodsType.text = it
        }
        GoodsDetailClassifyPop.STATE_NORMAL = GoodsDetailClassifyPop.GOODS_STATE_NORMAL
        couponPop = CouponPop(mContext)
        redPacketPop = RedPacketPop(mContext)
        val coupons = ArrayList<CouponBean>()
        coupons.add(CouponBean())
        coupons.add(CouponBean())
        coupons.add(CouponBean())
        couponPop.data= coupons
        redPacketPop.data=coupons

        val data = GoodsBean()
        val goodsSubClassify = ArrayList<GoodsSubClassifyBean>()
        goodsSubClassify.add(GoodsSubClassifyBean("1", "大"))
        goodsSubClassify.add(GoodsSubClassifyBean("2", "小"))
        val goodsSubClassify2 = ArrayList<GoodsSubClassifyBean>()
        goodsSubClassify2.add(GoodsSubClassifyBean("3", "长"))
        goodsSubClassify2.add(GoodsSubClassifyBean("4", "短"))
        data.goodsClassify.add(GoodsClassifyBean("大小", goodsSubClassify))
        data.goodsClassify.add(GoodsClassifyBean("长短", goodsSubClassify2))
        data.goodsClassifyPool.add(GoodsClassifyPoolBean("1,3", "2.5", "2", "http"))
        data.goodsClassifyPool.add(GoodsClassifyPoolBean("1,4", "3.5", "3", "http"))
        data.goodsClassifyPool.add(GoodsClassifyPoolBean("2,3", "4.5", "4", "http"))
        data.goodsClassifyPool.add(GoodsClassifyPoolBean("2,4", "5.5", "0", "http"))

        classifyPop.setNewData(data)
        initBanner()
        initRecycler()
        initEvent()
        mWebView.loadUrl("http://www.baidu.com")
    }
    private val arrayList2=ArrayList<CommentBean>()
    private fun initRecycler() {
        mGoodsCommentAdapter = GoodsCommentAdapter()
        val imgs = ArrayList<String>()
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        arrayList2.add(CommentBean(imgs))
        arrayList2.add(CommentBean(imgs))
        arrayList2.add(CommentBean(imgs))

        mGoodsCommentAdapter.setNewData(arrayList2)

        mGoodsCommentAdapter.bindToRecyclerView(mCommentRecyclerView)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(this)
        mCommentRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
        mCommentRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mCommentRecyclerView.isNestedScrollingEnabled = false
        mCommentRecyclerView.isFocusable = false

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                header.visibility = View.VISIBLE
            }
            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset==0)
                    header.visibility = View.GONE
            }
        })
        refreshLayout.setOnRefreshListener(this)
    }
    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_choose_type.setOnClickListener(this)
        bt_more_goodsComment.setOnClickListener(this)
        bt_coupon.setOnClickListener(this)
        bt_redPacket.setOnClickListener(this)
        bt_addCard.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
        bt_goodsStore.setOnClickListener(this)
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
            bt_more_goodsComment -> {
                startActivity(Intent(this, GoodsCommentListActivity::class.java))
            }
            bt_choose_type -> {
                classifyPop.showPopupWindow()
            }
            bt_addCard -> {
                classifyPop.showPopupWindow()
            }
            bt_confirm -> {
                classifyPop.showPopupWindow()
            }
            bt_coupon -> {
                couponPop.showPopupWindow()
            }
            bt_redPacket -> {
                redPacketPop.showPopupWindow()
            }
            bt_goodsStore -> {
                startActivity(Intent(this, GoodsShopActivity::class.java))
            }

        }
    }
    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreGoodsDetailData(this) {
            if (it != null) {
            }
        }
    }
}
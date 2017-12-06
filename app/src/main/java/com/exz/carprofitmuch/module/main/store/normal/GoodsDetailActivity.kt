package com.exz.carprofitmuch.module.main.store.normal

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsCommentAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.CommentBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity.Companion.GoodsCommentList_Intent_Id
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity.Companion.GoodsCommentList_Intent_IdMark
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopActivity.Companion.GoodsShop_Intent_ShopId
import com.exz.carprofitmuch.pop.CouponPop
import com.exz.carprofitmuch.pop.GoodsDetailClassifyPop
import com.exz.carprofitmuch.pop.RedPacketPop
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.view.preview.PreviewActivity
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IMAGES
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_POSITION
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_SHOW_NUM
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_goods_detail.*
import kotlinx.android.synthetic.main.layout_banner.*
import org.jetbrains.anko.backgroundColor

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

    private var goodsBean: GoodsBean? = null
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
                bottom_bar.visibility = View.VISIBLE
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
                    toolbar.setNavigationIcon(if (lastScrollY > 70) R.mipmap.icon_arrow_white_back else R.mipmap.icon_goods_detail_back)
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

    override fun setInflateId(): Int = R.layout.activity_goods_detail

    override fun init() {
        initPop()
        initBanner()
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initRecycler()
        initEvent()
        onRefresh(refreshLayout)
        mWebView.loadUrl("http://www.baidu.com")
    }

    private fun initPop() {
        classifyPop = GoodsDetailClassifyPop(this) { tv_goodsType.text = it }
        GoodsDetailClassifyPop.STATE_NORMAL = GoodsDetailClassifyPop.GOODS_STATE_NORMAL
        couponPop = CouponPop(mContext)
        redPacketPop = RedPacketPop(mContext)
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
        bt_more_goodsComment.setOnClickListener(this)
        bt_coupon.setOnClickListener(this)
        bt_redPacket.setOnClickListener(this)
        bt_addCard.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
        bt_goodsStore.setOnClickListener(this)
        bt_favorite.setOnClickListener(this)
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
            bt_more_goodsComment -> {
                if (goodsBean!=null) {
                    val intent = Intent(this, GoodsCommentListActivity::class.java)
                    intent.putExtra(GoodsCommentList_Intent_Id,goodsBean?.goodsId)
                    intent.putExtra(GoodsCommentList_Intent_IdMark,"1")
                    startActivity(intent)
                }

            }
            bt_choose_type -> {
                if (goodsBean != null)
                classifyPop.showPopupWindow()
            }
            bt_addCard -> {
                if (goodsBean != null)
                classifyPop.showPopupWindow()

            }
            bt_confirm -> {
                if (goodsBean != null)
                classifyPop.showPopupWindow()
            }
            bt_coupon -> {  // 领取优惠券
                DataCtrlClass.couponListData(goodsBean?.shopId ?: "", goodsBean?.goodsId ?: "") {
                    if (it != null) {
                        couponPop.data = it
                        couponPop.showPopupWindow()
                    }
                }
            }
            bt_redPacket -> {
                redPacketPop.showPopupWindow()
            }
            bt_goodsStore -> {//跳转店铺
                if (goodsBean != null) {
                    val intent = Intent(this, GoodsShopActivity::class.java)
                    intent.putExtra(GoodsShop_Intent_ShopId, goodsBean?.shopId)
                    startActivity(intent)
                }
            }
            bt_favorite -> {//收藏 操作
                if (goodsBean != null) {
                    DataCtrlClass.editFavoriteData(this, goodsBean?.goodsId ?: "", "1",
                            if (goodsBean?.isCollected == "1") {
                                goodsBean?.isCollected = "0"
                                "0"
                            } else {
                                goodsBean?.isCollected = "1"
                                "1"
                            }) {
                        bt_favorite.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                                ContextCompat.getDrawable(this,
                                        if (goodsBean?.isCollected == "1")
                                            R.mipmap.icon_goods_detail_favorite_on
                                        else R.mipmap.icon_goods_detail_favorite_off), null, null)
                    }
                }
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
                tv_goodsPrice.text = String.format(getString(R.string.CNY) + "%s", it.goodsPrice)
                tv_goodsOldPrice.text = String.format(getString(R.string.goods_detail_oldPrice), it.goodsPrice)
                tv_goodsExpressPrice.text = String.format(getString(R.string.goods_detail_expressPrice), it.expressPrice)
                tv_goodsSoldCount.text = String.format(getString(R.string.goods_detail_soldCount), it.saleCount)
                tv_goodsAddress.text = it.address
                bt_more_goodsComment.text = String.format(getString(R.string.goods_detail_comment), it.commentCount)
                mGoodsCommentAdapter.setNewData(it.commentList)

                bt_favorite.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                        ContextCompat.getDrawable(this,
                                if (it.isCollected == "1")
                                    R.mipmap.icon_goods_detail_favorite_on
                                else R.mipmap.icon_goods_detail_favorite_off), null, null)
                bt_coupon.visibility = if (it.isCoupon == "1") View.VISIBLE else View.GONE
                bt_choose_type.visibility = if (it.isCoupon == "1") View.VISIBLE else View.GONE
                if (it.isDelete == "1") {
                    bt_addCard.visibility = View.GONE
                    val params = bt_confirm.layoutParams as LinearLayout.LayoutParams
                    params.weight = 6f
                    bt_confirm.layoutParams = params
                    bt_confirm.text = getString(R.string.goods_detail_pass)
                    bt_confirm.backgroundColor=ContextCompat.getColor(mContext,R.color.MaterialGrey600)
                    bt_confirm.isClickable=false
                }
                DataCtrlClass.goodsClassifyData(this, it.goodsId) { spec ->
                    if (spec != null)
                        classifyPop.setNewData(spec, it)
                }
            }
        }
    }

    companion object {
        val GoodsDetail_Intent_GoodsId = "GoodsDetail_Intent_GoodsId"
    }
}
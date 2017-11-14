package com.exz.carprofitmuch.module.main.store.normal

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsShopAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsShopClassifyBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.pop.GoodsShopClassifyPop
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.activity_goods_shop.*
import kotlinx.android.synthetic.main.layout_banner.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class GoodsShopActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private lateinit var mNewAdapter: GoodsShopAdapter<GoodsBean>
    private lateinit var mHotAdapter: GoodsShopAdapter<GoodsBean>
    private lateinit var goodsShopClassifyPop: GoodsShopClassifyPop

    override fun setInflateId(): Int = R.layout.activity_goods_shop

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                header.visibility = View.VISIBLE
            }

            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
            }
        })

        scrollView.setOnScrollChangeListener(SZWUtils.MyNestedScrollListener(bottom_bar, SizeUtils.dp2px(45f)))
        return false
    }


    override fun init() {
        goodsShopClassifyPop = GoodsShopClassifyPop(this)
        val arrayList = ArrayList<GoodsShopClassifyBean>()
        val classifyBean = GoodsShopClassifyBean("1")
        classifyBean.name = "分类1"
        classifyBean.list.add(GoodsShopClassifyBean("2","子类1"))
        classifyBean.list.add(GoodsShopClassifyBean("3","子类2"))
        classifyBean.list.add(GoodsShopClassifyBean("4","子类3"))
        val classifyBean2 = GoodsShopClassifyBean("5")
        classifyBean2.name = "分类2"
        classifyBean2.list.add(GoodsShopClassifyBean("6","子类1"))
        classifyBean2.list.add(GoodsShopClassifyBean("7","子类2"))
        classifyBean2.list.add(GoodsShopClassifyBean("8","子类3"))
        arrayList.add(GoodsShopClassifyBean("9","全部宝贝"))
        arrayList.add(GoodsShopClassifyBean("10","新品首发"))
        arrayList.add(classifyBean)
        arrayList.add(classifyBean)
        arrayList.add(classifyBean)
        arrayList.add(classifyBean)
        arrayList.add(GoodsShopClassifyBean("11","新品首发"))
        arrayList.add(classifyBean2)
        goodsShopClassifyPop.data = arrayList

        val mNsp = SpannableString(getString(R.string.goods_shop_newGoodsList))
        mNsp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.MaterialRed600)), mNsp.length - 1, mNsp.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_newGoodsList.text = mNsp
        val mHsp = SpannableString(getString(R.string.goods_shop_newGoodsList))
        mHsp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.MaterialRed600)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_hotGoodsList.text = mHsp

        initBanner()
        initRecycler()
        initEvent()
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

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        mRightImg.setOnClickListener(this)
        mLeftImg.setOnClickListener(this)
        mTitle.setOnClickListener(this)
    }

    private fun initRecycler() {
        mNewAdapter = GoodsShopAdapter()
        mHotAdapter = GoodsShopAdapter()
        val scoreList = ArrayList<GoodsBean>()
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())

        mNewAdapter.setNewData(scoreList)
        mNewAdapter.bindToRecyclerView(mNewRecyclerView)
        mNewRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mNewRecyclerView.isNestedScrollingEnabled = false
        mNewRecyclerView.isFocusable = false

        mHotAdapter.setNewData(scoreList)
        mHotAdapter.bindToRecyclerView(mHotRecyclerView)
        mHotRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mHotRecyclerView.isNestedScrollingEnabled = false
        mHotRecyclerView.isFocusable = false

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                header.visibility = View.VISIBLE
            }

            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
            }
        })
        refreshLayout.setOnRefreshListener(this)

        mNewAdapter.onItemClickListener = this
        mHotAdapter.onItemClickListener = this
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        startActivity(Intent(mContext, GoodsDetailActivity::class.java))
    }

    override fun onClick(p0: View?) {
        when (p0) {
            mLeftImg -> {
                onBackPressed()
            }
            mTitle -> {
                val intent = Intent(this, GoodsShopSearchResultActivity::class.java)
                    intent.putExtra("isShowSoft",true)
                startActivity(intent)
            }
            mRightImg -> {
//                startActivity(Intent(this, GoodsShopSearchResultActivity::class.java))
                goodsShopClassifyPop.showPopupWindow()
            }
            else -> {
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreStoreData(this) {
            if (it != null) {
//                mNewAdapter.setNewData(it)
            }
        }
    }
    companion object {
        val GoodsShop_Intent_ShopId="GoodsShop_Intent_ShopId"
    }
}
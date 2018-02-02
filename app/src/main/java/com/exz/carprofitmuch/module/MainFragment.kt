package com.exz.carprofitmuch.module

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.MainAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.InformationBean
import com.exz.carprofitmuch.bean.InformationBean.Companion.TYPE_1
import com.exz.carprofitmuch.bean.MainRecommendBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.HotNewsActivity
import com.exz.carprofitmuch.module.main.ads.AdsActivity
import com.exz.carprofitmuch.module.main.insurance.InsuranceActivity
import com.exz.carprofitmuch.module.main.map.MapPinActivity
import com.exz.carprofitmuch.module.main.promotion.PromotionsActivity
import com.exz.carprofitmuch.module.main.store.score.ScoreStoreActivity
import com.exz.carprofitmuch.scanner.ScannerActivity
import com.exz.carprofitmuch.service.LocationService
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.facebook.drawee.view.SimpleDraweeView
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.header_main.view.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.textColor

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MainFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, OnBannerListener {


    private var mScrollY = 0
    private lateinit var mAdapter: MainAdapter<InformationBean>
    private lateinit var headerView: View
    private lateinit var footerView: View
    private lateinit var hotRecommendViews: MutableList<Any>
    private var banners = ArrayList<BannersBean>()
    private var recommends = ArrayList<MainRecommendBean>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun initView() {
        initBar()
        initRecycler()
        initHeaderAndFooter()
        refreshLayout.autoRefresh()
    }

    override fun initEvent() {
        headerView.banner.setOnBannerListener(this)
        headerView.bt_tab_1.setOnClickListener(this)
        headerView.bt_tab_2.setOnClickListener(this)
        headerView.bt_tab_3.setOnClickListener(this)
        headerView.bt_tab_4.setOnClickListener(this)
        headerView.bt_tab_5.setOnClickListener(this)
        headerView.bt_tab_6.setOnClickListener(this)
        headerView.bt_more_hot_recommend.setOnClickListener(this)
        headerView.bt_hot_lay_0.setOnClickListener(this)
        headerView.bt_hot_lay_1.setOnClickListener(this)
        headerView.bt_hot_lay_2.setOnClickListener(this)
        headerView.bt_hot_lay_3.setOnClickListener(this)
        headerView.bt_hot_lay_4.setOnClickListener(this)
        headerView.bt_more_hot_info.setOnClickListener(this)
    }


    private fun initRecycler() {
        mAdapter = MainAdapter()
        headerView = View.inflate(context, R.layout.header_main, null)
        footerView = View.inflate(context, R.layout.footer_main, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.addFooterView(footerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)


        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }
        })
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val h = DensityUtil.dp2px(170f)
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollY += dy
                if (mScrollY < h) {
                    buttonBarLayout.alpha = 1f * mScrollY / h
//                    blurView.alpha = 1f * mScrollY / h
                    blurView.setBlurRadius((1f * mScrollY / h) * 10)
                    blurView.setOverlayColor(Color.argb(((1f * mScrollY / h) * 204).toInt(), 252, 133, 23))
                }

            }
        })
        refreshLayout.setOnRefreshListener(this)

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val intent = Intent(context, MyWebActivity::class.java)
                intent.putExtra(Intent_Url, mAdapter.data[position].url)
                intent.putExtra(Intent_Title, "")
                startActivity(intent)
            }
        })
    }

    private fun initBar() {
        toolbar.navigationIcon = null
        blurView.setBlurRadius(0f)
        blurView.setOverlayColor(Color.argb(0, 252, 133, 23))
        buttonBarLayout.alpha = 0f
        mTitle.text = getString(R.string.app_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(activity)
        StatusBarUtil.setPaddingSmart(activity, toolbar)
        StatusBarUtil.setPaddingSmart(activity, blurView)

        toolbar.inflateMenu(R.menu.menu_main)
        val actionView = toolbar.menu.getItem(0).actionView
        actionView.setOnClickListener {
            (activity as BaseActivity).PermissionCameraWithCheck(Intent(context, ScannerActivity::class.java), false)
        }
    }
    private fun initHeaderAndFooter() {
        headerView.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        headerView.banner.setImageLoader(BannerImageLoader())
        //设置自动轮播，默认为true
        headerView.banner.isAutoPlay(true)
        //设置轮播时间
        headerView.banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        headerView.banner.setIndicatorGravity(BannerConfig.CENTER)


        headerView.bt_hot_lay_0.layoutParams.height = ScreenUtils.getScreenWidth() / 2
        hotRecommendViews = ArrayList()
        hotRecommendViews.add(headerView.tv_hot_title_0)
        hotRecommendViews.add(headerView.tv_hot_info_0)
        hotRecommendViews.add(headerView.img_hot_bg_0)
        hotRecommendViews.add(headerView.tv_hot_title_1)
        hotRecommendViews.add(headerView.tv_hot_info_1)
        hotRecommendViews.add(headerView.img_hot_bg_1)
        hotRecommendViews.add(headerView.tv_hot_title_2)
        hotRecommendViews.add(headerView.tv_hot_info_2)
        hotRecommendViews.add(headerView.img_hot_bg_2)
        hotRecommendViews.add(headerView.tv_hot_title_3)
        hotRecommendViews.add(headerView.tv_hot_info_3)
        hotRecommendViews.add(headerView.img_hot_bg_3)
        hotRecommendViews.add(headerView.tv_hot_title_4)
        hotRecommendViews.add(headerView.tv_hot_info_4)
        hotRecommendViews.add(headerView.img_hot_bg_4)

    }

    override fun OnBannerClick(position: Int) {
        if (SZWUtils.checkLogin(this@MainFragment) && SZWUtils.getIntent(context, banners[position]) != null)
            startActivity(SZWUtils.getIntent(context, banners[position]))
    }

    override fun onClick(p0: View?) {
        when (p0) {
            headerView.bt_tab_1 -> {
                val intent = Intent(context, InsuranceActivity::class.java)
                SZWUtils.checkLogin(this, intent, InsuranceActivity::class.java.name)
            }
            headerView.bt_tab_2 -> {//积分兑换
                startActivity(Intent(context, ScoreStoreActivity::class.java))
            }
            headerView.bt_tab_3 -> {
                startActivity(Intent(context, AdsActivity::class.java))
            }
            headerView.bt_tab_4 -> {//宝藏领取
                val intent = Intent(context, MapPinActivity::class.java).putExtra("className", context.getString(R.string.main_treasure_get))
                SZWUtils.checkLogin(this, intent, MapPinActivity::class.java.name)
            }
            headerView.bt_tab_5 -> {//红包领取
                val intent = Intent(context, MapPinActivity::class.java).putExtra("className", context.getString(R.string.main_redpacket_get))
                SZWUtils.checkLogin(this, intent, MapPinActivity::class.java.name)
            }
            headerView.bt_tab_6 -> {//商家活动
                if (MainActivity.locationEntity == null) {
                    toast("定位中，请允许获取定位权限")
                    (activity as BaseActivity).PermissionLocationWithCheck(Intent(context, LocationService::class.java), true)
                } else {
                    val intent = Intent(context, PromotionsActivity::class.java)
                    SZWUtils.checkLogin(this, intent, clazzName = PromotionsActivity::class.java.name)
                }
            }
            headerView.bt_more_hot_recommend -> {
                (activity as MainActivity).mainTabBar.currentTab = 1
            }
            headerView.bt_hot_lay_0 -> {
                if (recommends.isNotEmpty() && SZWUtils.getIntent(context, recommends[0]) != null)
                    startActivity(SZWUtils.getIntent(context, recommends[0]))
            }
            headerView.bt_hot_lay_1 -> {
                if (recommends.isNotEmpty() && SZWUtils.getIntent(context, recommends[1]) != null)
                    startActivity(SZWUtils.getIntent(context, recommends[1]))
            }
            headerView.bt_hot_lay_2 -> {
                if (recommends.isNotEmpty() && SZWUtils.getIntent(context, recommends[2]) != null)
                    startActivity(SZWUtils.getIntent(context, recommends[2]))
            }
            headerView.bt_hot_lay_3 -> {
                if (recommends.isNotEmpty() && SZWUtils.getIntent(context, recommends[3]) != null)
                    startActivity(SZWUtils.getIntent(context, recommends[3]))
            }
            headerView.bt_hot_lay_4 -> {
                if (recommends.isNotEmpty() && SZWUtils.getIntent(context, recommends[4]) != null)
                    startActivity(SZWUtils.getIntent(context, recommends[4]))
            }
            headerView.bt_more_hot_info -> {
                startActivity(Intent(context, HotNewsActivity::class.java))
            }
        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.bannerData(context, "1") {
            refreshLayout?.finishRefresh()
            if (it != null) {
                banners = it
                //设置图片集合
                headerView.banner.setImages(it)
                //banner设置方法全部调用完毕时最后调用
                headerView.banner.start()
            }
        }
        DataCtrlClass.mainRecommendData(context) {
            if (it != null) {
                recommends = it
                for (i in 0 until it.size) {
                    (hotRecommendViews[i * 3] as TextView).text = it[i].title
                    (hotRecommendViews[i * 3] as TextView).textColor = Color.parseColor(it[i].titleColor)
                    (hotRecommendViews[i * 3 + 1] as TextView).text = it[i].info
                    (hotRecommendViews[i * 3 + 2] as SimpleDraweeView).setImageURI(it[i].imgUrl)
                }
            }
        }
        DataCtrlClass.mainNewsData(context) {
            if (it != null) {
                (0..it.size).filter { it % 4 == 0 }.forEach { i -> it[i].type = TYPE_1 }
                mAdapter.setNewData(it)
            }
        }
    }


    companion object {
        fun newInstance(): MainFragment {
            val bundle = Bundle()
            val fragment = MainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
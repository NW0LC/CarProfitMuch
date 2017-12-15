package com.exz.carprofitmuch.module.main.store.score

import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ItemScoreStoreAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_store.*
import kotlinx.android.synthetic.main.header_store.view.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ScoreStoreActivity : BaseActivity(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{
    private var banners = ArrayList<BannersBean>()
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: ItemScoreStoreAdapter<GoodsBean>
    private lateinit var headerView: View
    private lateinit var footerView: View
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.main_score_store_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        return false
    }

    override fun setInflateId(): Int=R.layout.activity_score_store

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initHeaderAndFooter()
        initEvent()
        refreshLayout.autoRefresh()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {
        mAdapter = ItemScoreStoreAdapter()
        headerView = View.inflate(this, R.layout.layout_banner, null)
        footerView = View.inflate(this, R.layout.footer_score_store, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.addFooterView(footerView)
//        mAdapter.emptyView=View(mContext)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this,mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

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
        headerView.banner .setOnBannerListener {
            if (SZWUtils.checkLogin(this@ScoreStoreActivity) && SZWUtils.getIntent(this@ScoreStoreActivity, banners[it]) != null)
                startActivity(SZWUtils.getIntent(this@ScoreStoreActivity, banners[it]))
        }
    }
    override fun onRefresh(refreshLayout: RefreshLayout?) {
        currentPage = 1
        refreshState = Constants.RefreshState.STATE_REFRESH
        iniData()

    }



    override fun onLoadMoreRequested() {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        iniData()
    }
    private fun iniData() {
        DataCtrlClass.bannerData(this, "3") {
            refreshLayout?.finishRefresh()
            if (it != null) {
                banners = it
                //设置图片集合
                headerView.banner.setImages(it)
                //banner设置方法全部调用完毕时最后调用
                headerView.banner.start()
            }
        }
        DataCtrlClass.scoreStoreData(this,currentPage) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                    mAdapter.setNewData(it)
                } else {
                    mAdapter.addData(it)

                }
                if (it.isNotEmpty()) {
                    mAdapter.loadMoreComplete()
                    currentPage++
                } else {
                    mAdapter.loadMoreEnd()
                }
            } else {
                mAdapter.loadMoreFail()
            }
        }
    }
}
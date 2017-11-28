package com.exz.carprofitmuch.module.main

import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.AdsAdapter
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_ads.*
import kotlinx.android.synthetic.main.layout_ads_tab.view.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class AdsActivity : BaseActivity(), OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener{

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: AdsAdapter
    private val mTitles = arrayOf("首页", "消息")
    override fun initToolbar(): Boolean {
//        mTitle.text = getString(R.string.ads_name)
        val tabsLay = View.inflate(mContext, R.layout.layout_ads_tab, null)
        val params= ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,SizeUtils.dp2px(32f))
        tabsLay.tab.layoutParams=params
        tabsLay.tab.setTabData(mTitles)
        buttonBarLayout.addView( tabsLay,0)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView,10f)
        return false
    }

    override fun setInflateId(): Int= R.layout.activity_ads

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {

        mAdapter = AdsAdapter()
        val arrayList = ArrayList<String>()
        arrayList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg")
        arrayList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg")
        arrayList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg")
        arrayList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg")
        arrayList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg")
        arrayList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3337482453,2397318421&fm=27&gp=0.jpg")

        mAdapter.setNewData(arrayList)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this,mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))
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
        DataCtrlClass.mainAdsData(this,"0",currentPage) {
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
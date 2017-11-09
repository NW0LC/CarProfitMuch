package com.exz.carprofitmuch.module.main

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.AdsAdapter
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_ads.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class AdsActivity : BaseActivity(), OnRefreshListener , BaseQuickAdapter.RequestLoadMoreListener{

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: AdsAdapter
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.ads_name)
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
        DataCtrlClass.mainAdsData(this,currentPage) {
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
package com.exz.carprofitmuch.module.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.FootprintAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_red_packet.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class FootprintActivity : BaseActivity(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: FootprintAdapter<GoodsBean>
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.footprint_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView,10f)


        toolbar.inflateMenu(R.menu.menu_footprint)
        val actionView = toolbar.menu.getItem(0).actionView
        (actionView as TextView).text=getString(R.string.footprint_clear)
        actionView.setOnClickListener{
            //Todo 清空历史记录
            DialogUtils.delete(mContext){

            }
        }
        return false
    }

    override fun setInflateId(): Int= R.layout.activity_red_packet

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {
        mAdapter = FootprintAdapter()
        val coupons = ArrayList<GoodsBean>()
        coupons.add(GoodsBean(date = "12.12"))
        coupons.add(GoodsBean(date = "12.12"))
        coupons.add(GoodsBean(date = "12.12"))
        coupons.add(GoodsBean(date = "12.12"))
        coupons.add(GoodsBean(date = "12.13"))
        coupons.add(GoodsBean(date = "12.12"))
        coupons.add(GoodsBean(date = "12.12"))
        coupons.add(GoodsBean(date = "12.12"))

        mAdapter.setNewData(coupons)
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
        DataCtrlClass.footprintData(this, currentPage) {
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
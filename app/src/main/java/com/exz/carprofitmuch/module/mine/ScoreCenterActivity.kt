package com.exz.carprofitmuch.module.mine

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ScoreRecordAdapter
import com.exz.carprofitmuch.bean.ScoreRecordBean
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
class ScoreCenterActivity : BaseActivity(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: ScoreRecordAdapter<ScoreRecordBean>
    lateinit var header: View
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.mine_score_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 10f)

        toolbar.inflateMenu(R.menu.menu_mine_score)
        val actionView = toolbar.menu.getItem(0).actionView
        (actionView as TextView).text=getString(R.string.mine_score_exchangeRecord)
        actionView.setOnClickListener{

        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_score_center

    override fun init() {
        initRecycler()
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {
        mAdapter = ScoreRecordAdapter()
        val coupons = ArrayList<ScoreRecordBean>()
        coupons.add(ScoreRecordBean())
        coupons.add(ScoreRecordBean())
        coupons.add(ScoreRecordBean())
        coupons.add(ScoreRecordBean())
        coupons.add(ScoreRecordBean())
        coupons.add(ScoreRecordBean())
        coupons.add(ScoreRecordBean())
        coupons.add(ScoreRecordBean())

        mAdapter.setNewData(coupons)

        header = View.inflate(mContext, R.layout.header_score_center, null)
        mAdapter.addHeaderView(header)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))

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
        DataCtrlClass.mineScoreRecordData(this, currentPage) {
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
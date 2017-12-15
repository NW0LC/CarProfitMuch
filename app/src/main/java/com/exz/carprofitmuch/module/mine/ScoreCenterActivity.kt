package com.exz.carprofitmuch.module.mine

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ScoreRecordAdapter
import com.exz.carprofitmuch.bean.ScoreRecordBean
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_center.*
import kotlinx.android.synthetic.main.header_score_center.view.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class ScoreCenterActivity : BaseActivity(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {


    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: ScoreRecordAdapter<ScoreRecordBean>
    lateinit var headerView: View
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.mine_score_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        toolbar.inflateMenu(R.menu.menu_mine_score)
//        val actionView = toolbar.menu.getItem(0).actionView
//        (actionView as TextView).text = getString(R.string.mine_score_exchangeRecord)
//        actionView.setOnClickListener {
//            startActivity(Intent(this, ScoreOrderListActivity::class.java))
//        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_score_center

    override fun init() {
        initRecycler()
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initEvent()
        initMyScore()
    }

    private fun initMyScore() {
        DataCtrlClassXZW.myScoreData(mContext, {
            if (it != null) {
                headerView.tv_scoreCount.text = it.score//可用积分
                headerView.tv_pendScoreCount.text = it.scoreL//待返还积分
                headerView.tv_frizzScoreCount.text = it.scoreT//冻结积分
                headerView.tv_score_date.text =String.format(getString(R.string.mine_score_date),it.scoreT,it.invalidDate)
                headerView.tv_score_date.visibility=if ((it.scoreT.toDoubleOrNull()?:0)==0) View.GONE else View.VISIBLE
            }

        })
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        headerView.bt_rule.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        val intent = Intent(this, MyWebActivity::class.java)
        intent.putExtra(Intent_Url, Urls.PointRule)
        intent.putExtra(Intent_Title, getString(R.string.mine_score_rule))
        startActivity(intent)
    }
    private fun initRecycler() {
        mAdapter = ScoreRecordAdapter()

        headerView = View.inflate(mContext, R.layout.header_score_center, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))
        onRefresh(refreshLayout)
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
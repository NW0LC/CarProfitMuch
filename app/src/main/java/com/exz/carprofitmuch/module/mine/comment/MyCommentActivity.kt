package com.exz.carprofitmuch.module.mine.comment

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.MyCommentAdapter
import com.exz.carprofitmuch.bean.MyCommentBean
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity.Companion.GoodsDetail_Intent_GoodsId
import com.exz.carprofitmuch.module.main.store.service.ServiceDetailActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceDetailActivity.Companion.Service_Intent_ServieceId
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_my_comment_list.*

/**
 * Created by pc on 2017/11/9.
 * 我的评价
 */

class MyCommentActivity : BaseActivity(), BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {

    private var refreshState = com.szw.framelibrary.config.Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: MyCommentAdapter<MyCommentBean>
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.mine_my_comment)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 10f)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_my_comment_list

    override fun init() {
        initRecycler()
        initEvent()
    }


    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun initRecycler() {
        mAdapter = MyCommentAdapter()
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (mAdapter.data.get(position).classMark.equals("2")) {

                    startActivity(Intent(mContext, ServiceDetailActivity::class.java).putExtra(Service_Intent_ServieceId, mAdapter.data.get(position).goodsId))
                } else if (mAdapter.data.get(position).classMark.equals("1")) {
                    startActivity(Intent(mContext, GoodsDetailActivity::class.java).putExtra(GoodsDetail_Intent_GoodsId, mAdapter.data.get(position).goodsId))
                }
            }
        })
        onRefresh(refreshLayout)
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {
        currentPage = 1
        refreshState = com.szw.framelibrary.config.Constants.RefreshState.STATE_REFRESH
        iniData()

    }

    override fun onLoadMoreRequested() {
        refreshState = com.szw.framelibrary.config.Constants.RefreshState.STATE_LOAD_MORE
        iniData()
    }

    private fun iniData() {
        DataCtrlClassXZW.MyCommentListData(mContext, currentPage) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                if (refreshState == com.szw.framelibrary.config.Constants.RefreshState.STATE_REFRESH) {
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
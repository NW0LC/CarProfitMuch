package com.exz.carprofitmuch.module.mine.returngoods

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ReturnGoodsAdapter
import com.exz.carprofitmuch.bean.ReturnGoodsBean
import com.exz.carprofitmuch.module.mine.returngoods.ReturnGoodsDetailActivity.Companion.ReturnGoodsDetail_Intent_orderData
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_comment_list.*

/**
 * on 2017/10/17.
 * 退货退款
 */

class ReturnGoodsFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: ReturnGoodsAdapter<ReturnGoodsBean>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false)
        return rootView
    }

    override fun initView() {
        initToolbar()
        initRecycler()
        onRefresh(refreshLayout)
    }

    override fun initEvent() {

    }

    fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.setPaddingSmart(activity, mRecyclerView)
        StatusBarUtil.setMargin(activity, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 55f)
        SZWUtils.setMargin(header, 55f)
//        SZWUtils.setMargin(header, 55f)
        return false
    }

    private fun initRecycler() {
        mAdapter = ReturnGoodsAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(context, R.color.app_bg)))

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

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val intent = Intent(context, ReturnGoodsDetailActivity::class.java)
                intent.putExtra(ReturnGoodsDetail_Intent_orderData,mAdapter.data[position])
                startActivityForResult(intent,100)
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                val entity = mAdapter.data[position]
                ReturnGoodsAdapter.setOnClick(activity, view.id ,entity){
                    onRefresh(refreshLayout)
                }
            }
        })

    }

    override fun onClick(p0: View?) {
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
        DataCtrlClassXZW.returnOrderOrderData(context,arguments.getInt(COMMENT_TYPE).toString(), currentPage) {
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

    override fun onResume() {
        super.onResume()
        onRefresh(refreshLayout)
    }
    companion object {
        private const val COMMENT_TYPE = "type"
        fun newInstance(position: Int): ReturnGoodsFragment {
            val bundle = Bundle()
            val fragment = ReturnGoodsFragment()
            bundle.putInt(COMMENT_TYPE, position)
            fragment.arguments = bundle
            return fragment
        }
    }
}
package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ServiceListAdapter
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.bean.ServiceStoreBean
import com.exz.carprofitmuch.pop.ServiceListClassifyPop
import com.exz.carprofitmuch.pop.ServiceListSortPop
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_list.*
import razerdp.basepopup.BasePopupWindow
import java.util.*


/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceListActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {


    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: ServiceListAdapter<ServiceStoreBean>
    private lateinit var classifyPop: ServiceListClassifyPop
    private lateinit var sortPop: ServiceListSortPop
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.service_list_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 45f)
        SZWUtils.setMargin(header, 45f)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_service_list

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initFilter()
        initEvent()
    }

    private fun initFilter() {
        classifyPop = ServiceListClassifyPop(this) { title, position ->
            radioButton1.text = title
            SZWUtils.setGreyOrGreen(this, radioButton1, position == 0)
        }
        sortPop = ServiceListSortPop(this) { title, _,position ->
            radioButton2.text = title
            SZWUtils.setGreyOrGreen(this, radioButton2, position == 0)
        }
        classifyPop.onDismissListener = popDismissListener
        sortPop.onDismissListener = popDismissListener
        classifyPop.data = SZWUtils.getServiceListClassifyData()
        sortPop.data = SZWUtils.getServiceListSortData()


    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        radioButton1.setOnClickListener(this)
        radioButton2.setOnClickListener(this)
    }

    private fun initRecycler() {
        mAdapter = ServiceListAdapter()
        val arrayList = ArrayList<ServiceStoreBean>()
        val scoreList = ArrayList<ServiceGoodsBean>()
        scoreList.add(ServiceGoodsBean())
        scoreList.add(ServiceGoodsBean())
        scoreList.add(ServiceGoodsBean())
        scoreList.add(ServiceGoodsBean())
        scoreList.add(ServiceGoodsBean())
        scoreList.add(ServiceGoodsBean())
        arrayList.add(ServiceStoreBean(scoreList))
        arrayList.add(ServiceStoreBean(scoreList))

        mAdapter.setNewData(arrayList)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))


        mRecyclerView.addOnItemTouchListener(object :OnItemClickListener(){
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            startActivity(Intent(this@ServiceListActivity, ServiceShopActivity::class.java))
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            radioButton1 -> {
                classifyPop.showPopupWindow(radioGroup)
            }
            radioButton2 -> {
                sortPop.showPopupWindow(radioGroup)
            }
            else -> {
            }
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

    private fun iniData(){
        DataCtrlClass.serviceListData(this, currentPage) {
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
    private val popDismissListener: BasePopupWindow.OnDismissListener = object : BasePopupWindow.OnDismissListener() {
        override fun onDismiss() {
            refreshLayout.autoRefresh()
            radioGroup.clearCheck()
        }
    }

    override fun onDestroy() {
        ServiceListSortPop.sortId = ""
        ServiceListClassifyPop.serviceClassifyId = ""
        super.onDestroy()
    }
}
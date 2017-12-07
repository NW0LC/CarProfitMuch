package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ServiceListAdapter
import com.exz.carprofitmuch.bean.ServiceShopBean
import com.exz.carprofitmuch.module.MainActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity.Companion.ServiceShop_Intent_ServiceShopId
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


/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceListActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {


    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: ServiceListAdapter<ServiceShopBean>
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
        refreshLayout.autoRefresh()
    }

    private fun initFilter() {
        classifyPop = ServiceListClassifyPop(this) { title,_, position ->
            radioButton1.text = title
            SZWUtils.setGreyOrGreen(this, radioButton1, position == 0)
            onRefresh(refreshLayout)
        }
        sortPop = ServiceListSortPop(this) { title, _,position ->
            radioButton2.text = title
            SZWUtils.setGreyOrGreen(this, radioButton2, position == 0)
            onRefresh(refreshLayout)
        }
        classifyPop.onDismissListener = popDismissListener
        sortPop.onDismissListener = popDismissListener
        sortPop.data = SZWUtils.getServiceListSortData()


    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        radioButton1.setOnClickListener(this)
        radioButton2.setOnClickListener(this)
    }

    private fun initRecycler() {
        mAdapter = ServiceListAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))


        mRecyclerView.addOnItemTouchListener(object :OnItemChildClickListener(){
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val intent = Intent(this@ServiceListActivity, ServiceShopActivity::class.java)
                intent.putExtra(ServiceShop_Intent_ServiceShopId,mAdapter.data[position].shopId)
                SZWUtils.checkLogin(this@ServiceListActivity, intent, ServiceShopActivity::class.java.name)
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            radioButton1 -> {
                if (classifyPop.data.isNotEmpty())
                classifyPop.showPopupWindow(radioGroup)
                else
                    radioGroup.clearCheck()
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
        DataCtrlClass.serviceClassifyData(this,"2"){
            if (it!=null)
            classifyPop.data =it
        }
    }

    override fun onLoadMoreRequested() {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        iniData()
    }

    private fun iniData(){
        DataCtrlClass.serviceListData(this, currentPage,classifyPop.serviceClassifyId,sortPop.sortId,MainActivity.locationEntity?.longitude?:"0",MainActivity.locationEntity?.latitude?:"0") {
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
            radioGroup.clearCheck()
        }
    }
}
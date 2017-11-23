package com.exz.carprofitmuch.module.mine.address

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.AddressManagerAdapter
import com.exz.carprofitmuch.bean.AddressBean
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_address_manager.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class AddressManagerActivity : BaseActivity(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: AddressManagerAdapter<AddressBean>
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.address_manager_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView,10f)
        return false
    }

    override fun setInflateId(): Int= R.layout.activity_address_manager

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_submit.setOnClickListener{
            val intent = Intent(this@AddressManagerActivity, AddressAddOrUpdateActivity::class.java)
            intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressType, AddressAddOrUpdateActivity.address_type_3)
            startActivityForResult(intent,100)
        }
    }

    private fun initRecycler() {
        mAdapter = AddressManagerAdapter()
        val coupons = ArrayList<AddressBean>()
        coupons.add(AddressBean("1"))
        coupons.add(AddressBean("0"))
        coupons.add(AddressBean("0"))
        coupons.add(AddressBean("0"))
        coupons.add(AddressBean("0"))
        coupons.add(AddressBean("0"))
        coupons.add(AddressBean("0"))
        coupons.add(AddressBean("0"))

        mAdapter.setNewData(coupons)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this,mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))
        mRecyclerView.addOnItemTouchListener(object :OnItemChildClickListener(){
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                when (view.id) {
                    R.id.bt_edit-> {
                        val intent = Intent(this@AddressManagerActivity, AddressAddOrUpdateActivity::class.java)
                        intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressData,mAdapter.data[position])
                        intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressType,if (mAdapter.data[position].isDefault()) AddressAddOrUpdateActivity.address_type_2 else AddressAddOrUpdateActivity.address_type_1)
                        startActivityForResult(intent,100)
                    }
                    R.id.bt_delete-> {
                        DialogUtils.delete(mContext){
                            DataCtrlClass.editAddressState(mContext,""){
                                onRefresh(refreshLayout)
                            }
                        }
                    }
                    R.id.radioButton-> {
                        DataCtrlClass.editAddressState(mContext,""){
                            onRefresh(refreshLayout)
                        }

                    }
                }
            }
        })
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
        DataCtrlClass.addressChooseData(this, currentPage) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK==resultCode)
            onRefresh(refreshLayout)
    }
}
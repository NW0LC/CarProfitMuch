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
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_address_manager.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class AddressManagerActivity : BaseActivity() {

    private lateinit var mAdapter: AddressManagerAdapter<AddressBean>
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.address_manager_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 10f)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_address_manager

    override fun init() {
        initRecycler()
        initEvent()
        initData()
    }

    private fun initData() {
        DataCtrlClass.addressChooseData(this) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                mAdapter.setNewData(it)
                mAdapter.loadMoreEnd()
            }
        }
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_submit.setOnClickListener {
            val intent = Intent(this@AddressManagerActivity, AddressAddOrUpdateActivity::class.java)
            intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressType, AddressAddOrUpdateActivity.address_type_3)
            startActivityForResult(intent, 100)
        }
    }

    private fun initRecycler() {
        mAdapter = AddressManagerAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
//        mAdapter.setOnLoadMoreListener(this,mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                val entity= mAdapter.data[position]
                when (view.id) {
                    R.id.bt_edit -> {
                        val intent = Intent(this@AddressManagerActivity, AddressAddOrUpdateActivity::class.java)
                        intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressData, mAdapter.data[position])
                        intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressType, if (mAdapter.data[position].isDefault()) AddressAddOrUpdateActivity.address_type_2 else AddressAddOrUpdateActivity.address_type_1)
                        startActivityForResult(intent, 100)
                    }
                    R.id.bt_delete -> {
                        DataCtrlClass.EditAddressData(mContext, entity.id, Urls.AddressDelete) {
                                initData()
                        }
                    }
                    R.id.radioButton -> {
                        DataCtrlClass.EditAddressData(mContext, entity.id, Urls.AddressDefault) {
                                initData()

                        }

                    }
                }
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode)
            initData()
    }
}
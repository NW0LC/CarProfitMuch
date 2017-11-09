package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ServiceShopGoodsAdapter
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_pay_service_result.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServicePayResultActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {
    private lateinit var mServiceShopGoodsAdapter: ServiceShopGoodsAdapter<ServiceGoodsBean>
    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.text = getString(R.string.pay_service_result_name)
        mRight.text =getString(R.string.pay_service_result_confirm)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, lay_title)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_pay_service_result

    override fun init() {
        initEvent()
        initRecycler()
    }

    private fun initEvent() {
        mLeftImg.setOnClickListener{ finish() }
        mRight.setOnClickListener{ finish() }

    }
    private val arrayList=ArrayList<ServiceGoodsBean>()
    private fun initRecycler() {
        mServiceShopGoodsAdapter = ServiceShopGoodsAdapter()

        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())

        mServiceShopGoodsAdapter.goodsCount=arrayList.size
        mServiceShopGoodsAdapter.setNewData(arrayList)


        mServiceShopGoodsAdapter.bindToRecyclerView(mServiceRecyclerView)
        mServiceRecyclerView.layoutManager = LinearLayoutManager(this)
        mServiceRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
        mServiceRecyclerView.isNestedScrollingEnabled = false
        mServiceRecyclerView.isFocusable = false

        mServiceRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                startActivity(Intent(this@ServicePayResultActivity,ServiceDetailActivity::class.java))
            }
        })
    }
    override fun onClick(p0: View?) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreStoreData(this) {
            if (it != null) {

            }
        }
    }
}
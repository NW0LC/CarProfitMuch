package com.exz.carprofitmuch.module.main.promotion

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.PromotionsPersonalAdapter
import com.exz.carprofitmuch.bean.PromotionsPersonalBean
import com.exz.carprofitmuch.module.main.promotion.PromotionsDetailActivity.Companion.PromotionsDetail_Intent_PromotionId
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity.Companion.ServiceShop_Intent_ServiceShopId
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_comment_list.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class PromotionsPersonalFragment : MyBaseFragment(), OnRefreshListener,  BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: PromotionsPersonalAdapter<PromotionsPersonalBean>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false)
        return rootView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            onRefresh(refreshLayout)
        }
    }
    override fun initView() {
        initToolbar()
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
    }

    override fun initEvent() {

    }
    fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.setPaddingSmart(activity, mRecyclerView)
        StatusBarUtil.setMargin(activity, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 55f)
        SZWUtils.setMargin(header, 55f)
        return false
    }
    private fun initRecycler() {
        mAdapter = PromotionsPersonalAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(context!!, R.color.app_bg)))
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener(){
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (view?.id) {
                    R.id.lay_content -> {
                        val intent = Intent(context, PromotionsDetailActivity::class.java)
                        intent.putExtra(PromotionsDetail_Intent_PromotionId,mAdapter.data[position].id)
                        startActivity(intent)
                    }
                    R.id.tv_shopName -> {
                        val intent = Intent(context, ServiceShopActivity::class.java)
                        intent.putExtra(ServiceShop_Intent_ServiceShopId,mAdapter.data[position].shopId)
                        startActivity(intent)
                    }
                    R.id.bt_phoneCall -> {DialogUtils.Call(activity as BaseActivity,mAdapter.data[position].shopPhone) }
                    else -> {
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

    private fun iniData(){
        DataCtrlClass.promotionsPersonalData(context, currentPage,arguments?.getString(COMMENT_TYPE).toString()) {
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
    companion object {
        private const val COMMENT_TYPE="type"
        fun newInstance(position:String): PromotionsPersonalFragment {
            val bundle = Bundle()
            val fragment = PromotionsPersonalFragment()
            bundle.putString(COMMENT_TYPE,position)
            fragment.arguments = bundle
            return fragment
        }
    }
}
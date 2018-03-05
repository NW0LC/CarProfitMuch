package com.exz.carprofitmuch.module.mine

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.CardPackageListAdapter
import com.exz.carprofitmuch.bean.ServiceOrderBean
import com.exz.carprofitmuch.module.mine.CardPackageDetailActivity.Companion.CardPackageDetail_Intent_OrderId
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

class CardPackageListFragment : MyBaseFragment(), OnRefreshListener,  BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: CardPackageListAdapter<ServiceOrderBean>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_card_package_list, container, false)
        return rootView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            onRefresh(refreshLayout)
    }
    override fun initView() {
        initToolbar()
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
    }
    fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.setPaddingSmart(activity, mRecyclerView)
        StatusBarUtil.setMargin(activity, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 55f)
        SZWUtils.setMargin(header, 45f)
        return false
    }
    private fun initRecycler() {
        mAdapter = CardPackageListAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(context!!, R.color.app_bg)))

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener(){
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val intent = Intent(context!!, CardPackageDetailActivity::class.java)
                intent.putExtra(CardPackageDetail_Intent_OrderId,mAdapter.data[position].orderId)
                startActivityForResult(intent,100)
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                /**         btLeft    btMid     btRight
                 * 1未使用            联系商家 申请退款
                 * 2已使用  删除订单  联系商家  评价服务
                 * 3已过期  删除订单  联系商家
                 * 4已退款  删除订单
                 * 其他
                 */
                when (view.id) {
                     R.id.bt_mid-> {
                         DialogUtils.Call(activity as BaseActivity,mAdapter.data[position].shopPhone)
                    }
                     R.id.bt_right-> {
                         when (mAdapter.data[position].orderState) {
                             "1" -> {
                                  //申请退款
                                  DataCtrlClass.editServiceState(context,mAdapter.data[position].orderId,"2"){
                                      if (it!=null)
                                          onRefresh(refreshLayout)
                                  }
                             }
                             "2" -> {
                                 //评价订单
                                 startActivity(Intent(context,ServiceOrderCommentActivity::class.java))
                             }
                             else -> {
                             }
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

    private fun iniData(){
        DataCtrlClass.cardPackageListData(context, currentPage,arguments?.get(COMMENT_TYPE).toString()) {
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
        if (resultCode==RESULT_OK)
            onRefresh(refreshLayout)
    }
    companion object {
        private const val COMMENT_TYPE="type"
        fun newInstance(position:Int): CardPackageListFragment {
            val bundle = Bundle()
            val fragment = CardPackageListFragment()
            bundle.putInt(COMMENT_TYPE,position)
            fragment.arguments = bundle
            return fragment
        }
    }
}
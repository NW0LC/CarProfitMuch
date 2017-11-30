package com.exz.carprofitmuch.module.mine.goodsorder

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsOrderAdapter
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Intent_Finish_Type_2
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_Finish_Type
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_OrderId
import com.exz.carprofitmuch.module.mine.GoodsOrderCommentActivity
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_comment_list.*

/**
 * on 2017/10/17.
 * 评价
 */

class GoodsOrderFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    var orderState = "3"
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: GoodsOrderAdapter<Any?>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false)
        return rootView
    }

    override fun initView() {
        initToolbar()
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initRecycler()
    }

    override fun initEvent() {

    }

    override fun onResume() {
        super.onResume()
        onRefresh(refreshLayout)
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
        mAdapter = GoodsOrderAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 15, ContextCompat.getColor(context, R.color.app_bg)))


        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                GoodsOrderDetailActivity.orderId = mAdapter.data.get(position).orderId
                startActivity(Intent(context, GoodsOrderDetailActivity::class.java))
            }

        })
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(a: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                /**         btLeft    btMid     btRight
                 * 1待付款     【联系商家   取消订单  支付订单】
                 * 2待发货     【         联系商家  申请退款】
                 * 3待收货     【联系商家   查看物流  确认收货】
                 * 4待评价     【联系商家   删除订单  评价订单】
                 * 5已结束     【联系商家   删除订单  】
                 * 其他
                 */
                when (view.id) {
                    R.id.tv_mid -> {
                        when (orderState) {
                            "1" -> {//取消订单
                                DataCtrlClassXZW.EditOrderData(context, mAdapter.data.get(position).orderId, "0", {
                                    if (it != null) {
                                        onRefresh(refreshLayout)
                                    }
                                })
                            }
                            "2" -> {//联系卖家
                                DialogUtils.Call(context as BaseActivity, mAdapter.data.get(position).shopPhone)
                            }
                            "3" -> {//查看物流
                                startActivity(Intent(context, MyWebActivity::class.java).putExtra(MyWebActivity.Intent_Url, "http://m.kuaidi100.com/result.jsp?nu=" + mAdapter.data.get(position).logisticsNum).putExtra(MyWebActivity.Intent_Title, "查看物流"))
                            }
                            "4", "5" -> {    //删除订单
                                DataCtrlClassXZW.EditOrderData(context, mAdapter.data.get(position).orderId, "1", {
                                    if (it != null) {
                                        onRefresh(refreshLayout)
                                    }
                                })
                            }

                        }


                    }
                    R.id.tv_right -> {
                        when (orderState) {
                            "1" -> {//支付订单
                                startActivity(Intent(context, PayMethodsActivity::class.java).putExtra(Pay_Intent_OrderId, mAdapter.data.get(position).orderId).putExtra(Pay_Intent_Finish_Type, Intent_Finish_Type_2))
                            }
                            "2" -> {//申请退款

                            }
                            "3" -> {//确认收货
                                DataCtrlClassXZW.ConfirmOrderDetailData(context, "2", {
                                    if (it != null) {
                                        onRefresh(refreshLayout)
                                    }
                                })
                            }
                            "4", "5" -> {    //评价订单
                                GoodsOrderCommentActivity.shopId = mAdapter.data.get(position).shopId
                                GoodsOrderCommentActivity.orderId = mAdapter.data.get(position).orderId
                                startActivity(Intent(context, GoodsOrderCommentActivity::class.java))

                            }

                        }
                    }
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
        DataCtrlClassXZW.MyOrderData(context, arguments.get(COMMENT_TYPE).toString(), currentPage) {
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
        private const val COMMENT_TYPE = "type"
        fun newInstance(position: Int): GoodsOrderFragment {
            val bundle = Bundle()
            val fragment = GoodsOrderFragment()
            bundle.putInt(COMMENT_TYPE, position)
            fragment.arguments = bundle
            return fragment
        }
    }
}
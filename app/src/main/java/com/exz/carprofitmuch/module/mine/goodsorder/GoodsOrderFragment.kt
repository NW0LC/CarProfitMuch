package com.exz.carprofitmuch.module.mine.goodsorder

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
import com.exz.carprofitmuch.adapter.GoodsOrderAdapter
import com.exz.carprofitmuch.bean.MyOrderBean
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Intent_Finish_Type_2
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_Finish_Type
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_OrderId
import com.exz.carprofitmuch.module.mine.GoodsOrderCommentActivity
import com.exz.carprofitmuch.module.mine.goodsorder.RefundActivity.Companion.Refund_Intent_OrderId
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_comment_list.*

/**
 * on 2017/10/17.
 * 评价
 */

class GoodsOrderFragment : MyBaseFragment(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: GoodsOrderAdapter<MyOrderBean>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false)
        return rootView
    }

    override fun initView() {
        initToolbar()
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initRecycler()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onRefresh(refreshLayout)
        }
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
                GoodsOrderDetailActivity.orderId = mAdapter.data[position].orderId
                startActivity(Intent(context, GoodsOrderDetailActivity::class.java))
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                /**         btLeft        btMid     btRight
                 * 1待付款 【联系商家   取消订单   支付订单】
                 * 2待发货 【联系商家              申请退款】
                 * 3待收货 【联系商家   查看物流   确认收货】
                 * 4待评价 【联系商家   申请退货   评价订单】
                 * 5已结束 【联系商家              删除订单】
                 * 6已取消 【                      删除订单】
                 * 其他
                 */
                when (view.id) {
                    R.id.tv_mid -> {
                        when (mAdapter.data[position].orderState) {
                            "1" -> {//取消订单
                                DialogUtils.cancel(context){
                                    DataCtrlClassXZW.editOrderData(context, mAdapter.data[position].orderId, "0", {
                                        if (it != null) {
                                            onRefresh(refreshLayout)
                                        }
                                    })
                                }

                            }
                            "3" -> {//查看物流
                                startActivity(Intent(context, MyWebActivity::class.java).putExtra(MyWebActivity.Intent_Url, "http://m.kuaidi100.com/result.jsp?nu=" + mAdapter.data[position].logisticsNum).putExtra(MyWebActivity.Intent_Title, "查看物流"))
                            }
                            "4" -> {    //申请退货
                                startActivityForResult(Intent(context,RefundActivity::class.java).putExtra(Refund_Intent_OrderId,mAdapter.data[position].orderId),100)
                            }

                        }


                    }
                    R.id.tv_right -> {
                        when (mAdapter.data[position].orderState) {
                            "1" -> {//支付订单
                                startActivityForResult(Intent(context, PayMethodsActivity::class.java).putExtra(Pay_Intent_OrderId, mAdapter.data[position].orderId).putExtra(Pay_Intent_Finish_Type, Intent_Finish_Type_2),100)
                            }
                            "2" -> {//申请退款
                                com.exz.carprofitmuch.utils.DialogUtils.refund(context, mAdapter.data[position].orderId, {
                                    DataCtrlClassXZW.ApplyReturnMoney(context, mAdapter.data[position].orderId, it, {
                                        if (it != null) {
                                            onRefresh(refreshLayout)
                                        }
                                    })

                                })

                            }
                            "3" -> {//确认收货

                                DataCtrlClassXZW.editOrderData(context, mAdapter.data[position].orderId, "2", {
                                    if (it != null) {
                                        onRefresh(refreshLayout)
                                    }
                                })
                            }
                            "4" -> {    //评价订单
                                GoodsOrderCommentActivity.shopId = mAdapter.data[position].shopId
                                GoodsOrderCommentActivity.orderId = mAdapter.data[position].orderId
                                GoodsOrderCommentActivity.json = Gson().toJson(mAdapter.data[position].goodsInfo)
                                startActivityForResult(Intent(context, GoodsOrderCommentActivity::class.java),100)

                            }
                            "5","6" -> {    //删除订单
                                DialogUtils.delete(context){
                                    DataCtrlClassXZW.editOrderData(context, mAdapter.data[position].orderId, "1", {
                                        if (it != null) {
                                            onRefresh(refreshLayout)
                                        }
                                    })
                                }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100)
            onRefresh(refreshLayout)
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
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
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.MyOrderBean
import com.exz.carprofitmuch.module.mine.GoodsOrderCommentActivity
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_comment_list.*
import java.util.*

/**
 * on 2017/10/17.
 * 评价
 */

class GoodsOrderFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    var orderState = "3"
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: GoodsOrderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false)
        return rootView
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

    private val arrayList2 = ArrayList<MyOrderBean>()
    private val arrayList2Item = ArrayList<GoodsBean>()
    private fun initRecycler() {
        arrayList2Item.add(GoodsBean())
        arrayList2.add(MyOrderBean("1", arrayList2Item))
        arrayList2.add(MyOrderBean("2", arrayList2Item))
        arrayList2.add(MyOrderBean("3", arrayList2Item))
        arrayList2.add(MyOrderBean("4", arrayList2Item))
        mAdapter = GoodsOrderAdapter()
        mAdapter.setNewData(arrayList2)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 15, ContextCompat.getColor(context, R.color.app_bg)))


        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                startActivity(Intent(context, GoodsOrderDetailActivity::class.java))
            }

        })
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                /**         btLeft    btMid     btRight
                 * 1待付款     【联系商家   取消订单  支付订单】
                 * 2待收货     【联系商家   查看物流  确认收货】
                 * 3待评价     【联系商家   删除订单  评价订单】
                 * 4已结束     【联系商家   删除订单  】
                 * 其他
                 */
                when (view.id) {
                    R.id.tv_mid -> {
                        when (orderState) {
                            "1" -> {//取消订单
                                DataCtrlClassXZW.CancelOrderDetailData(context, "", {
                                    if (it != null) {
                                        iniData()
                                    }
                                })
                            }
                            "2" -> {//查看物流
                                startActivity(Intent(context, MyWebActivity::class.java).putExtra(MyWebActivity.Intent_Url, "http://www.baidu.com").putExtra(MyWebActivity.Intent_Title, "http://www.baidu.com"))
                            }
                            "3", "4" -> {    //删除订单
                                DataCtrlClassXZW.DeleteOrderDetailData(context, "", {
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
//                        startActivity(Intent(mContext,MyWebActivity::class.java).putExtra("orderId","1"))
                            }
                            "2" -> {//确认收货
                                DataCtrlClassXZW.ConfirmOrderDetailData(context, "", {
                                    if (it != null) {
                                        onRefresh(refreshLayout)
                                    }
                                })
                            }
                            "3", "4" -> {    //评价订单
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
        DataCtrlClassXZW.MyOrderData(context, currentPage) {
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
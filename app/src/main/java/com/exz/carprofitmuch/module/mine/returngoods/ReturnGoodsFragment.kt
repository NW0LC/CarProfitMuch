package com.exz.carprofitmuch.module.mine.returngoods

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
import com.exz.carprofitmuch.adapter.ReturnGoodsAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.MyOrderBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_comment_list.*
import java.util.*

/**
 * on 2017/10/17.
 * 退货退款
 */

class ReturnGoodsFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    var orderState = "3"
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: ReturnGoodsAdapter<MyOrderBean>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false)
        return rootView
    }

    override fun initView() {
        initToolbar()
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
//        SZWUtils.setMargin(header, 55f)
        return false
    }

    private val arrayList2 = ArrayList<MyOrderBean>()
    private fun initRecycler() {
        mAdapter = ReturnGoodsAdapter()
        val imgs = ArrayList<GoodsBean>()
        imgs.add(GoodsBean())
        arrayList2.add(MyOrderBean("1", imgs))
        arrayList2.add(MyOrderBean("2", imgs))
        arrayList2.add(MyOrderBean("3", imgs))
        mAdapter.setNewData(arrayList2)

        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(context, R.color.app_bg)))

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                header.visibility = View.VISIBLE
            }

            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
            }
        })
        refreshLayout.setOnRefreshListener(this)

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                startActivity(Intent(context, ReturnGoodsDetailActivity::class.java).putExtra("orderType",(adapter!!.data.get(position) as MyOrderBean).orderSate))
            }
        })

        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                /**             btLeft       btMid     btRight
                 * 1待处理     【             联系商家   取消收货】
                 * 2处理中     【                       联系商家】
                 * 3已完成     【                       联系商家】
                 * 其他
                 */
                when (view.id) {
                    R.id.tv_mid -> {
                        when (orderState) {
                            "1" -> {//联系商家
                                DialogUtils.Call(context as BaseActivity, "110")
                            }
                        }


                    }
                    R.id.tv_right -> {
                        when (orderState) {
                            "1" -> {//取消收货
                                DataCtrlClassXZW.CancelOrderDetailData(context, "", {
                                    if (it != null) {
                                        iniData()
                                    }
                                })
                            }
                            "2" -> {//联系商家
                                DialogUtils.Call(context as BaseActivity, "110")
                            }
                            "3"-> {    //联系商家
                                DialogUtils.Call(context as BaseActivity, "110")

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
        fun newInstance(position: Int): ReturnGoodsFragment {
            val bundle = Bundle()
            val fragment = ReturnGoodsFragment()
            bundle.putInt(COMMENT_TYPE, position)
            fragment.arguments = bundle
            return fragment
        }
    }
}
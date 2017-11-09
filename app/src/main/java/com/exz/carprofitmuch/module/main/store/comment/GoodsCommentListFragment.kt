package com.exz.carprofitmuch.module.main.store.comment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsCommentAdapter
import com.exz.carprofitmuch.bean.GoodsCommentBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
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
 * Created by 史忠文
 * on 2017/10/17.
 */

class GoodsCommentListFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: GoodsCommentAdapter<GoodsCommentBean>

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
        return false
    }
    private val arrayList2= ArrayList<GoodsCommentBean>()
    private fun initRecycler() {
        mAdapter = GoodsCommentAdapter()
        val imgs = ArrayList<String>()
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        arrayList2.add(GoodsCommentBean(imgs))
        arrayList2.add(GoodsCommentBean(imgs))
        arrayList2.add(GoodsCommentBean(imgs))

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
                if (offset==0)
                header.visibility = View.GONE
            }
        })
        refreshLayout.setOnRefreshListener(this)
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

    private fun iniData(){
        DataCtrlClass.goodsCommentData(context, currentPage) {
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
        fun newInstance(position:Int): GoodsCommentListFragment {
            val bundle = Bundle()
            val fragment = GoodsCommentListFragment()
            bundle.putInt(COMMENT_TYPE,position)
            fragment.arguments = bundle
            return fragment
        }
    }
}
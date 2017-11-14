package com.exz.carprofitmuch.module.mine.favorite

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.FavoriteGoodsAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.config.Constants.Collection_Edit
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity.Companion.GoodsDetail_Intent_GoodsId
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Delete
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Edit
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.removeItem
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_favorite_goods.*

class FavoriteGoodsFragment : MyBaseFragment(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {


    var refreshState = Constants.RefreshState.STATE_REFRESH
    var currentPage = 1
    lateinit var mAdapter: FavoriteGoodsAdapter<GoodsBean>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_favorite_goods, container, false)
        return rootView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            Edit_Type = "-1"
            onRefresh(refreshLayout)
        }
    }


    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Collection_Edit)))
    fun Refresh(tag: String) {
        if (Edit_Type == Edit_Type_Edit) {//正常状态
            lay_bottom.visibility = View.GONE
            refreshLayout.isEnableRefresh = true
            SZWUtils.setPaddingSmart(mRecyclerView, 45f)
            mRecyclerView.smoothScrollBy(0, SizeUtils.dp2px(-45f))
        } else {//编辑状态
            lay_bottom.visibility = View.VISIBLE
            refreshLayout.isEnableRefresh = false
            SZWUtils.minusPaddingSmart(mRecyclerView, 45f)

        }
        mAdapter.notifyDataSetChanged()
    }

    override fun initView() {
        initToolbar()
        initRecycler()
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
        mAdapter = FavoriteGoodsAdapter()
        val arrayList = ArrayList<GoodsBean>()
        arrayList.add(GoodsBean("1"))
        arrayList.add(GoodsBean("2"))
        arrayList.add(GoodsBean("3"))
        arrayList.add(GoodsBean("4"))
        arrayList.add(GoodsBean("5"))
        arrayList.add(GoodsBean("6"))
        arrayList.add(GoodsBean("7"))
        arrayList.add(GoodsBean("8"))
        arrayList.add(GoodsBean("9"))
        arrayList.add(GoodsBean("10"))
        arrayList.add(GoodsBean("11"))
        mAdapter.addData(arrayList)
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
            override fun onSimpleItemClick(baseAdapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                    Edit_Type_Edit -> {
                        //跳转
                        val intent = Intent(context, GoodsDetailActivity::class.java)
                        intent.putExtra(GoodsDetail_Intent_GoodsId, mAdapter.data[position].id)
                        startActivity(intent)
                    }
                    Edit_Type_Delete -> {
                        //删除
                        mAdapter.data[position].isCheck = !mAdapter.data[position].isCheck
                        mAdapter.notifyItemChanged(position)
                        val b = mAdapter.data.none { !it.isCheck }
                        cb_check.isChecked = b
                    }
                }
            }
        })
    }


    override fun initEvent() {
        cb_check.setOnClickListener(this)
        bt_delete.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            cb_check -> {//全选
                mAdapter.animatorEnable = false
                for (goodsBean in mAdapter.data) {
                    goodsBean.isCheck = cb_check.isChecked
                }
                mAdapter.notifyDataSetChanged()
                mRecyclerView.post {
                    mAdapter.animatorEnable = true
                }
            }
            bt_delete -> {//删除
                var b = false
                val goodsEntities = ArrayList<GoodsBean>()
                for (GoodsBean in mAdapter.data) {
                    if (GoodsBean.isCheck) {
                        goodsEntities.add(GoodsBean)
                        b = true
                    }
                }
                if (b) {
                    DialogUtils.delete(context) {
                        DataCtrlClass.favoriteGoodsIsCollection(context, "1", goodsEntities.toTypedArray()) {
                            removeItem(mAdapter, it)
                        }
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun initData() {
        DataCtrlClass.favoriteGoodsListData(context, currentPage) {
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

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        currentPage = 1
        refreshState = Constants.RefreshState.STATE_REFRESH
        initData()

    }

    override fun onLoadMoreRequested() {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        initData()
    }

    companion object {
        fun newInstance(): FavoriteGoodsFragment {
            val bundle = Bundle()
            val fragment = FavoriteGoodsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
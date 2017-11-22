package com.exz.carprofitmuch.module.main.map

import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.RedPacketTreasureAdapter
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.pop.MapTreasurePop
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_ads.*
import kotlinx.android.synthetic.main.header_treasure_redpacket.view.*

/**
 * Created by pc on 2017/11/22.
 * 宝藏领取
 */

class MapTreasureActivity : BaseActivity(), BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: RedPacketTreasureAdapter
    private val imgData = java.util.ArrayList<String>()
    private lateinit var headerView: View
    private lateinit var pop: MapTreasurePop
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.main_treasure_get)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 15f)
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_treasure_red_packet
    }

    override fun init() {
        initRecycler()
        initView()
        initEvent()
    }

    private fun initView() {
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        headerView = View.inflate(mContext, R.layout.header_treasure_redpacket, null)
        headerView.tv_title.text = "本批宝藏赞助商家"
        mAdapter.addHeaderView(headerView)

    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {

        pop=MapTreasurePop(mContext)
        val coupons = ArrayList<CouponBean>()
        coupons.add(CouponBean())
        coupons.add(CouponBean())
        coupons.add(CouponBean())
        mAdapter = RedPacketTreasureAdapter(1)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        imgData.add("res://com.exz.carprofitmuch/" + R.mipmap.icon_map_treasure3)
        mAdapter.setNewData(imgData)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.setPadding(50, 50, 0, 0)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.addOnItemTouchListener(object :OnItemClickListener(){
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                pop.showPopupWindow()
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
        DataCtrlClass.mainAdsData(this, currentPage) {
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
}

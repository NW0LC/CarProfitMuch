package com.exz.carprofitmuch.module.main.store.score

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ScoreStoreAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.ScoreStoreBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_store.*
import kotlinx.android.synthetic.main.header_store.view.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ScoreStoreActivity : BaseActivity(), OnRefreshListener{


    private lateinit var mAdapter: ScoreStoreAdapter<ScoreStoreBean>
    private lateinit var headerView: View
    private lateinit var footerView: View
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.main_score_store_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        return false
    }

    override fun setInflateId(): Int=R.layout.activity_score_store

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initHeaderAndFooter()
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {
        mAdapter = ScoreStoreAdapter()
        val arrayList = ArrayList<ScoreStoreBean>()
        val scoreList = ArrayList<GoodsBean>()
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        scoreList.add(GoodsBean())
        arrayList.add(ScoreStoreBean(R.mipmap.icon_score_store_recommend,title="为你推荐",scoreList= scoreList))
        arrayList.add(ScoreStoreBean(R.mipmap.icon_score_store_division,title="积分推荐",scoreList= scoreList))

        mAdapter.setNewData(arrayList)
        headerView = View.inflate(this, R.layout.layout_banner, null)
        footerView = View.inflate(this, R.layout.footer_score_store, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.addFooterView(footerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))

    }

    private fun initHeaderAndFooter() {
        val bannersBean = ArrayList<BannersBean>()
        bannersBean.add(BannersBean())
        bannersBean.add(BannersBean())
        bannersBean.add(BannersBean())
        headerView.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        headerView.banner.setImageLoader(BannerImageLoader())
        //设置图片集合
        headerView.banner.setImages(bannersBean)
        //设置自动轮播，默认为true
        headerView.banner.isAutoPlay(true)
        //设置轮播时间
        headerView.banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        headerView.banner.setIndicatorGravity(BannerConfig.CENTER)
        //banner设置方法全部调用完毕时最后调用
        headerView.banner.start()

    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreStoreData(this) {
            if (it != null) {
                mAdapter.setNewData(it)
            }
        }
    }
}
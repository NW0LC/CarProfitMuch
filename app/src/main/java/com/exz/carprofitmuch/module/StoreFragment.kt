package com.exz.carprofitmuch.module

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.MainStoreAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.MainStoreScoreCardBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.store.goodssearch.SearchResultActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity
import com.exz.carprofitmuch.module.main.store.score.ScoreStoreActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceListActivity
import com.facebook.drawee.view.SimpleDraweeView
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.header_store.view.*
import kotlinx.android.synthetic.main.layout_card_main_store_score.view.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class StoreFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener {


    private lateinit var mAdapter: MainStoreAdapter<GoodsBean>
    private lateinit var headerView: View
    private lateinit var footerView: View
    private lateinit var hotRecommendViews: MutableList<Any>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_store, container, false)
        return rootView
    }

    override fun initView() {
        initBar()
        initRecycler()
        initHeaderAndFooter()
    }

    override fun initEvent() {
        headerView.bt_type_more_0.setOnClickListener(this)
        headerView.bt_type_more_1.setOnClickListener(this)
        headerView.bt_type_more_2.setOnClickListener(this)
        headerView.bt_service_lay_0.setOnClickListener(this)
        headerView.bt_service_lay_1.setOnClickListener(this)
        headerView.bt_service_lay_2.setOnClickListener(this)
    }

    private fun initRecycler() {
        mAdapter = MainStoreAdapter()
        val arrayList = ArrayList<GoodsBean>()
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        mAdapter.setNewData(arrayList)
        headerView = View.inflate(context, R.layout.header_store, null)
        footerView = View.inflate(context, R.layout.footer_main, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.addFooterView(footerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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
        mRecyclerView.addOnItemTouchListener(object :OnItemClickListener(){
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                startActivity(Intent(context, GoodsDetailActivity::class.java))
            }
        })
    }

    private fun initBar() {
        toolbar.navigationIcon = null
        mTitle.text = getString(R.string.main_store_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(activity)
        StatusBarUtil.setPaddingSmart(activity, toolbar)
        StatusBarUtil.setPaddingSmart(activity, mRecyclerView)
        StatusBarUtil.setPaddingSmart(activity, blurView)
        StatusBarUtil.setMargin(activity, header)
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

        val cards = ArrayList<MainStoreScoreCardBean>()
        cards.add(MainStoreScoreCardBean("价值500元加油卡", "5000", "res://${context.packageName}/${R.mipmap.icon_main_store_score_bg_1}"))
        cards.add(MainStoreScoreCardBean("价值500元加油卡", "5000", "res://${context.packageName}/${R.mipmap.icon_main_store_score_bg_2}"))
        headerView.lay_type_0.removeAllViews()
        for (card in cards) {
            val cardMainStoreScore = View.inflate(context, R.layout.layout_card_main_store_score, null)
            cardMainStoreScore.img_score_bg.setImageURI(card.img)
            cardMainStoreScore.tv_score_name.text = card.title
            cardMainStoreScore.tv_score_count.text = String.format("${card.price}%s",getString(R.string.SCORE))
            headerView.lay_type_0.addView(cardMainStoreScore)
            cardMainStoreScore.setOnClickListener {

            }
        }


        hotRecommendViews = ArrayList()
        hotRecommendViews.add(headerView.tv_service_title_0)
        hotRecommendViews.add(headerView.tv_service_info_0)
        hotRecommendViews.add(headerView.tv_service_price_0)
        hotRecommendViews.add(headerView.img_service_bg_0)
        hotRecommendViews.add(headerView.tv_service_title_1)
        hotRecommendViews.add(headerView.tv_service_info_1)
        hotRecommendViews.add(headerView.tv_service_price_1)
        hotRecommendViews.add(headerView.img_service_bg_1)
        hotRecommendViews.add(headerView.tv_service_title_2)
        hotRecommendViews.add(headerView.tv_service_info_2)
        hotRecommendViews.add(headerView.tv_service_price_2)
        hotRecommendViews.add(headerView.img_service_bg_2)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            headerView.bt_type_more_0 -> {
                startActivity(Intent(context, ScoreStoreActivity::class.java))
            }
            headerView.bt_type_more_1 -> {
                startActivity(Intent(context, ServiceListActivity::class.java))
            }
            headerView.bt_type_more_2 -> {
                startActivity(Intent(context, SearchResultActivity::class.java))
            }
            headerView.bt_service_lay_0 -> {
            }
            headerView.bt_service_lay_1 -> {
            }
            headerView.bt_service_lay_2 -> {
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.mainStoreData(context) {
            if (it != null) {
                for (i in 0 until it.scoreGoods.size) {
                    (hotRecommendViews[i * 4] as TextView).text = it.serviceBeans[i].title
                    (hotRecommendViews[i * 4 + 1] as TextView).text = it.serviceBeans[i].info
                    (hotRecommendViews[i * 4 + 2] as TextView).text =String.format("${getString(R.string.main_store_service_price)}%s",it.serviceBeans[i].price)
                    (hotRecommendViews[i * 4 + 3] as SimpleDraweeView).setImageURI(it.scoreGoods[i].img)
                }
                mAdapter.setNewData(it.scoreGoods)
            }
            refreshLayout?.finishRefresh()
        }
    }


    companion object {
        fun newInstance(): StoreFragment {
            val bundle = Bundle()
            val fragment = StoreFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
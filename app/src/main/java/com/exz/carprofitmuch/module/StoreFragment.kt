package com.exz.carprofitmuch.module

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.MainStoreAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.MainStoreServiceBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.store.normal.GoodsClassifyActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity.Companion.GoodsDetail_Intent_GoodsId
import com.exz.carprofitmuch.module.main.store.score.ScoreStoreActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceListActivity
import com.exz.carprofitmuch.utils.SZWUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
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

    private var banners = ArrayList<BannersBean>()
    private var severModel =ArrayList<MainStoreServiceBean>()
    private lateinit var mAdapter: MainStoreAdapter<GoodsBean>
    private lateinit var headerView: View
    private lateinit var footerView: View
    private lateinit var hotRecommendViews: MutableList<Any>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_store, container, false)
        return rootView
    }

    override fun initView() {
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initBar()
        initRecycler()
        initHeaderAndFooter()
        refreshLayout.autoRefresh()
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
        headerView = View.inflate(context, R.layout.header_store, null)
        footerView = View.inflate(context, R.layout.footer_main, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.addFooterView(footerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val intent = Intent(context, GoodsDetailActivity::class.java)
                intent.putExtra(GoodsDetail_Intent_GoodsId, mAdapter.data[position].goodsId)
                startActivity(intent)
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
        headerView.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        headerView.banner.setImageLoader(BannerImageLoader())
        //设置自动轮播，默认为true
        headerView.banner.isAutoPlay(true)
        //设置轮播时间
        headerView.banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        headerView.banner.setIndicatorGravity(BannerConfig.CENTER)

//        val cards = ArrayList<MainStoreScoreCardBean>()
//        cards.add(MainStoreScoreCardBean("价值500元加油卡", "5000", "res://${context.packageName}/${R.mipmap.icon_main_store_score_bg_1}"))
//        cards.add(MainStoreScoreCardBean("价值500元加油卡", "5000", "res://${context.packageName}/${R.mipmap.icon_main_store_score_bg_2}"))


        headerView.bt_service_lay_2.layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(35f)) / 3 * 2
        hotRecommendViews = ArrayList()
        hotRecommendViews.add(headerView.tv_service_title_0)
        hotRecommendViews.add(headerView.tv_service_info_0)
        hotRecommendViews.add(headerView.tv_service_price_0)
        hotRecommendViews.add(headerView.img_service_bg_0)
        hotRecommendViews.add(headerView.tv_service_title_2)
        hotRecommendViews.add(headerView.tv_service_info_2)
        hotRecommendViews.add(headerView.tv_service_price_2)
        hotRecommendViews.add(headerView.img_service_bg_2)
        hotRecommendViews.add(headerView.tv_service_title_1)
        hotRecommendViews.add(headerView.tv_service_info_1)
        hotRecommendViews.add(headerView.tv_service_price_1)
        hotRecommendViews.add(headerView.img_service_bg_1)


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
                startActivity(Intent(context, GoodsClassifyActivity::class.java))
//                startActivity(Intent(context, SearchResultActivity::class.java))
            }
            headerView.bt_service_lay_0 -> {
                if (severModel.isNotEmpty()&&SZWUtils.getMarkIntent(context, severModel[0]) != null) {
                    startActivity(SZWUtils.getMarkIntent(context, severModel[0]))
                }
            }
            headerView.bt_service_lay_1 -> {
                if (severModel.isNotEmpty()&&SZWUtils.getMarkIntent(context, severModel[2]) != null) {
                    startActivity(SZWUtils.getMarkIntent(context, severModel[2]))
                }
            }
            headerView.bt_service_lay_2 -> {
                if (severModel.isNotEmpty()&&SZWUtils.getMarkIntent(context, severModel[1]) != null) {
                    startActivity(SZWUtils.getMarkIntent(context, severModel[1]))
                }
            }
        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.bannerData(context, "1") {
            refreshLayout?.finishRefresh()
            if (it != null) {
                banners = it
                //设置图片集合
                headerView.banner.setImages(it)
                //banner设置方法全部调用完毕时最后调用
                headerView.banner.start()
            }
        }
        DataCtrlClass.mainStoreData(context) {
            if (it != null) {
                headerView.lay_type_0.removeAllViews()
                for (card in it.scoreModel) {
                    val cardMainStoreScore = View.inflate(context, R.layout.layout_card_main_store_score, null)
                    headerView.img_score_bg.layoutParams.height=ScreenUtils.getScreenWidth()/3
                    cardMainStoreScore.img_score_bg.setImageURI(card.imgUrl)
//                    cardMainStoreScore.tv_score_name.text = card.title
//                    cardMainStoreScore.tv_score_count.text = String.format("${card.price}%s", getString(R.string.SCORE))
                    headerView.lay_type_0.addView(cardMainStoreScore)
                    cardMainStoreScore.setOnClickListener {
                        val markIntent = SZWUtils.getMarkIntent(context, card)
                        if (markIntent != null) {
                            startActivity(markIntent)
                        }
                    }
                }
                severModel=it.severModel
                for (i in 0 until it.severModel.size) {
//                    (hotRecommendViews[i * 4] as TextView).text = it.severModel[i].title
//                    (hotRecommendViews[i * 4 + 1] as TextView).text = it.serviceBeans[i].info
//                    (hotRecommendViews[i * 4 + 2] as TextView).text = String.format("${getString(R.string.main_store_service_price)}%s", it.serviceBeans[i].price)
                    (hotRecommendViews[i * 4 + 3] as SimpleDraweeView).setImageURI(it.severModel[i].imgUrl)
                }
                mAdapter.setNewData(it.goodsModel)
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
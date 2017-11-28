package com.exz.carprofitmuch.module

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.MainAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.InformationBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.AdsActivity
import com.exz.carprofitmuch.module.main.map.MapPinActivity
import com.exz.carprofitmuch.widget.MyWebActivity
import com.facebook.drawee.view.SimpleDraweeView
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.header_main.view.*
import org.jetbrains.anko.textColor

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MainFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener {


    private var mScrollY = 0
    private lateinit var mAdapter: MainAdapter<InformationBean>
    private lateinit var headerView: View
    private lateinit var footerView: View
    private lateinit var hotRecommendViews: MutableList<Any>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun initView() {
        initBar()
        initRecycler()
        initHeaderAndFooter()
    }

    override fun initEvent() {
        headerView.banner.setOnBannerListener {
            val intent = Intent(context, MyWebActivity::class.java)
//            intent.putExtra(MyWebActivity.Intent_Title, response.body().info[it].title)
//            intent.putExtra(MyWebActivity.Intent_Url, response.body().info[it].url)
            startActivity(intent)
        }
        headerView.bt_tab_1.setOnClickListener(this)
        headerView.bt_tab_2.setOnClickListener(this)
        headerView.bt_tab_3.setOnClickListener(this)
        headerView.bt_tab_4.setOnClickListener(this)
        headerView.bt_tab_5.setOnClickListener(this)
        headerView.bt_more_hot_recommend.setOnClickListener(this)
        headerView.bt_hot_lay_0.setOnClickListener(this)
        headerView.bt_hot_lay_1.setOnClickListener(this)
        headerView.bt_hot_lay_2.setOnClickListener(this)
        headerView.bt_hot_lay_3.setOnClickListener(this)
        headerView.bt_hot_lay_4.setOnClickListener(this)
        headerView.bt_more_hot_info.setOnClickListener(this)
    }


    private fun initRecycler() {
        mAdapter = MainAdapter()
        val arrayList = ArrayList<InformationBean>()
        arrayList.add(InformationBean(1))
        arrayList.add(InformationBean(2))
        arrayList.add(InformationBean(1))
        arrayList.add(InformationBean(2))
        arrayList.add(InformationBean(1))
        arrayList.add(InformationBean(2))
        arrayList.add(InformationBean(1))
        mAdapter.setNewData(arrayList)
        headerView = View.inflate(context, R.layout.header_main, null)
        footerView = View.inflate(context, R.layout.footer_main, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.addFooterView(footerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val h = DensityUtil.dp2px(170f)
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollY += dy
                if (mScrollY < h) {
                    buttonBarLayout.alpha = 1f * mScrollY / h
                    blurView.alpha = 1f * mScrollY / h
                }

            }
        })
        refreshLayout.setOnRefreshListener(this)
    }

    private fun initBar() {
        toolbar.navigationIcon = null
        buttonBarLayout.alpha = 0f
        blurView.alpha = 0f
        mTitle.text = getString(R.string.app_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(activity)
        StatusBarUtil.setPaddingSmart(activity, toolbar)
        StatusBarUtil.setPaddingSmart(activity, blurView)
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

        headerView.bt_hot_lay_0.layoutParams.height=ScreenUtils.getScreenWidth()/2
        hotRecommendViews = ArrayList()
        hotRecommendViews.add(headerView.tv_hot_title_0)
        hotRecommendViews.add(headerView.tv_hot_info_0)
        hotRecommendViews.add(headerView.img_hot_bg_0)
        hotRecommendViews.add(headerView.tv_hot_title_1)
        hotRecommendViews.add(headerView.tv_hot_info_1)
        hotRecommendViews.add(headerView.img_hot_bg_1)
        hotRecommendViews.add(headerView.tv_hot_title_2)
        hotRecommendViews.add(headerView.tv_hot_info_2)
        hotRecommendViews.add(headerView.img_hot_bg_2)
        hotRecommendViews.add(headerView.tv_hot_title_3)
        hotRecommendViews.add(headerView.tv_hot_info_3)
        hotRecommendViews.add(headerView.img_hot_bg_3)
        hotRecommendViews.add(headerView.tv_hot_title_4)
        hotRecommendViews.add(headerView.tv_hot_info_4)
        hotRecommendViews.add(headerView.img_hot_bg_4)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            headerView.bt_tab_1 -> {
            }
            headerView.bt_tab_2 -> {
            }
            headerView.bt_tab_3 -> {
                startActivity(Intent(context, AdsActivity::class.java))
            }
            headerView.bt_tab_4 -> {//宝藏领取
                startActivity(Intent(context, MapPinActivity::class.java).putExtra("className",context.getString(R.string.main_treasure_get)))
            }
            headerView.bt_tab_5 -> {//红包领取
                startActivity(Intent(context, MapPinActivity::class.java).putExtra("className", context.getString(R.string.main_redpacket_get)))
            }
            headerView.bt_tab_6 -> {//商家活动
            }
            headerView.bt_more_hot_recommend -> {
            }
            headerView.bt_hot_lay_0 -> {
            }
            headerView.bt_hot_lay_1 -> {
            }
            headerView.bt_hot_lay_2 -> {
            }
            headerView.bt_hot_lay_3 -> {
            }
            headerView.bt_hot_lay_4 -> {
            }
            headerView.bt_more_hot_info -> {
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.mainData(context) {
            if (it != null) {
                for (i in 0 until it.mainRecommends.size) {
                    (hotRecommendViews[i * 3] as TextView).text = it.mainRecommends[i].title
                    (hotRecommendViews[i * 3] as TextView).textColor = Color.parseColor(it.mainRecommends[i].titleColor)
                    (hotRecommendViews[i * 3 + 1] as TextView).text = it.mainRecommends[i].info
                    (hotRecommendViews[i * 3 + 2] as SimpleDraweeView).setImageURI(it.mainRecommends[i].img)
                }
                mAdapter.setNewData(it.infos)
            }
        }
    }


    companion object {
        fun newInstance(): MainFragment {
            val bundle = Bundle()
            val fragment = MainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
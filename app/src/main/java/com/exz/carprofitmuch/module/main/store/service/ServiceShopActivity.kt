package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ServiceCommentAdapter
import com.exz.carprofitmuch.adapter.ServiceShopGoodsAdapter
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.bean.CommentBean
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.store.comment.ServiceCommentListActivity
import com.exz.carprofitmuch.pop.CouponPop
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_shop.*
import kotlinx.android.synthetic.main.layout_banner.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceShopActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {

    private lateinit var couponPop: CouponPop
    private lateinit var mServiceShopGoodsAdapter: ServiceShopGoodsAdapter<ServiceGoodsBean>
    private lateinit var mServiceCommentAdapter: ServiceCommentAdapter<CommentBean>
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.service_shop_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        return false
    }

    override fun setInflateId(): Int= R.layout.activity_service_shop

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        couponPop = CouponPop(mContext)
        val coupons = ArrayList<CouponBean>()
        coupons.add(CouponBean())
        coupons.add(CouponBean())
        coupons.add(CouponBean())
        couponPop.data= coupons

        initRecycler()
        initHeaderAndFooter()
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_service_goodsMore.setOnClickListener(this)
        bt_service_shop_comment.setOnClickListener(this)
        bt_coupon.setOnClickListener(this)
    }
    private val arrayList=ArrayList<ServiceGoodsBean>()
    private val arrayList2=ArrayList<CommentBean>()
    private fun initRecycler() {
        mServiceShopGoodsAdapter = ServiceShopGoodsAdapter()
        mServiceCommentAdapter = ServiceCommentAdapter()

        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())
        arrayList.add(ServiceGoodsBean())

        val imgs = ArrayList<String>()
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        imgs.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1191873587,3864918266&fm=27&gp=0.jpg")
        arrayList2.add(CommentBean(imgs))
        arrayList2.add(CommentBean(imgs))
        arrayList2.add(CommentBean(imgs))

        mServiceShopGoodsAdapter.setNewData(arrayList)
        mServiceCommentAdapter.setNewData(arrayList2)

        mServiceShopGoodsAdapter.bindToRecyclerView(mServiceRecyclerView)
        mServiceRecyclerView.layoutManager = LinearLayoutManager(this)
        mServiceRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
        mServiceRecyclerView.isNestedScrollingEnabled = false
        mServiceRecyclerView.isFocusable = false



        mServiceCommentAdapter.bindToRecyclerView(mCommentRecyclerView)
        mCommentRecyclerView.layoutManager = LinearLayoutManager(this)
        mCommentRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
        mCommentRecyclerView.isNestedScrollingEnabled = false
        mCommentRecyclerView.isFocusable = false


        mServiceRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                startActivity(Intent(this@ServiceShopActivity,ServiceDetailActivity::class.java))
            }
        })
    }

    private fun initHeaderAndFooter() {
        val bannersBean = ArrayList<BannersBean>()
        bannersBean.add(BannersBean())
        bannersBean.add(BannersBean())
        bannersBean.add(BannersBean())
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(BannerImageLoader())
        //设置图片集合
        banner.setImages(bannersBean)
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //banner设置方法全部调用完毕时最后调用
        banner.start()

    }
    override fun onClick(p0: View?) {
        when (p0) {
            bt_service_goodsMore -> {
                mServiceShopGoodsAdapter.goodsCount=arrayList.size
                bt_service_goodsMore.visibility=View.GONE
                mServiceShopGoodsAdapter.notifyDataSetChanged()
            }
            bt_service_shop_comment -> {
               startActivity(Intent(this, ServiceCommentListActivity::class.java))
            }
            bt_coupon -> {
                couponPop.showPopupWindow()
            }
            else -> {
            }
        }
    }
    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreStoreData(this) {
            if (it != null) {
//                mServiceShopGoodsAdapter.setNewData(it)
            }
        }
    }

    companion object {
        val ServiceShop_Intent_ShopId="ServiceShop_Intent_ShopId"
    }
}
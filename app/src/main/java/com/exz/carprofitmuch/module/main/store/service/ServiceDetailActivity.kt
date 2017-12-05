package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.graphics.Paint
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
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.MainActivity
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity.Companion.GoodsCommentList_Intent_Id
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity.Companion.GoodsCommentList_Intent_IdMark
import com.exz.carprofitmuch.module.main.store.comment.ServiceCommentListActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceConfirmActivity.Companion.ServiceConfirm_Intent_ids
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity.Companion.ServiceShop_Intent_ServiceShopId
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.view.preview.PreviewActivity
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IMAGES
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_POSITION
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_SHOW_NUM
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_detail.*
import kotlinx.android.synthetic.main.layout_banner.*
import org.jetbrains.anko.backgroundColor

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceDetailActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {


    private lateinit var mServiceShopGoodsAdapter: ServiceShopGoodsAdapter<ServiceGoodsBean>
    private lateinit var mServiceCommentAdapter: ServiceCommentAdapter<CommentBean>
    private var serviceGoodsBean: ServiceGoodsBean? = null
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.service_detail_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_service_detail

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initRecycler()
        initHeaderAndFooter()
        initEvent()
        onRefresh(refreshLayout)
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_service_shop_comment.setOnClickListener(this)
        tv_service_confirm.setOnClickListener(this)
        bt_address.setOnClickListener(this)
        tv_service_store_phone.setOnClickListener(this)
        bt_goods_detail.setOnClickListener(this)
    }

    private fun initRecycler() {
        mServiceShopGoodsAdapter = ServiceShopGoodsAdapter()
        mServiceCommentAdapter = ServiceCommentAdapter()


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
                startActivity(Intent(this@ServiceDetailActivity, ServiceDetailActivity::class.java))
            }
        })
    }

    private fun initHeaderAndFooter() {
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(BannerImageLoader())
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.setOnBannerListener {
            if (serviceGoodsBean != null) {
                val intent = Intent(mContext, PreviewActivity::class.java)
                intent.putExtra(PREVIEW_INTENT_IMAGES, serviceGoodsBean?.mainImgs)
                intent.putExtra(PREVIEW_INTENT_SHOW_NUM, true)
                intent.putExtra(PREVIEW_INTENT_POSITION, it)
                mContext.startActivity(intent)
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_service_shop_comment -> {
                val intent = Intent(this, ServiceCommentListActivity::class.java)
                intent.putExtra(GoodsCommentList_Intent_Id,serviceGoodsBean?.goodsId)
                intent.putExtra(GoodsCommentList_Intent_IdMark,"1")
                startActivity(intent)
            }
            tv_service_confirm -> {
                val intent = Intent(this, ServiceConfirmActivity::class.java)
                val ids=serviceGoodsBean?.shopInfo?.shopId+","+serviceGoodsBean?.goodsId+","+serviceGoodsBean?.payMark
                intent.putExtra(ServiceConfirm_Intent_ids,ids)
                startActivity(intent)
            }
            tv_service_store_phone -> {
                DialogUtils.Call(this, serviceGoodsBean?.shopInfo?.shopPhone ?: "")
            }
            bt_address -> {
                val intent = Intent(this@ServiceDetailActivity, ServiceShopActivity::class.java)
                intent.putExtra(ServiceShop_Intent_ServiceShopId,serviceGoodsBean?.shopInfo?.shopId?:"")
                startActivity(intent)
            }
            bt_goods_detail -> {
                val intent = Intent(this, MyWebActivity::class.java)
                intent.putExtra(Intent_Url, "")
                intent.putExtra(Intent_Title, "")
                startActivity(intent)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.serviceGoodsDetailData(this, intent.getStringExtra(Service_Intent_ServiceId) ?: "", MainActivity.locationEntity?.longitude ?: "", MainActivity.locationEntity?.latitude ?: "") {
           refreshLayout?.finishRefresh()
            if (it != null) {
                serviceGoodsBean = it
                //设置图片集合
                val banners = ArrayList<BannersBean>()
                it.mainImgs.mapTo(banners) { BannersBean(it) }
                banner.setImages(banners)
                //banner设置方法全部调用完毕时最后调用
                banner.start()
                tv_service_goodsName.text = it.goodsName
                tv_service_goodsInfo.text = it.subTitle

                if (it.payMark=="1") {
                    tv_service_goodsPrice.text = String.format("%s"+getString(R.string.SCORE), it.goodsPrice)
                    tv_service_goodsOldPrice.text = String.format("%s"+getString(R.string.SCORE), it.oldPrice)
                }else{
                    tv_service_goodsPrice.text = String.format(getString(R.string.CNY) + "%s", it.goodsPrice)
                    tv_service_goodsOldPrice.text = String.format(getString(R.string.CNY) + "%s", it.oldPrice)
                }

                tv_service_goodsOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
                tv_service_goodsSoldCount.text = String.format(getString(R.string.service_list_serviceGoods_soldCount) + "%s", it.saleCount)
                tv_service_shop_name.text = it.shopInfo.shopName
                tv_service_shop_address.text = it.shopInfo.shopAddress
                tv_distance.text=it.shopInfo.distance
                if (it.isDelete == "1") {
                    tv_service_confirm.text = getString(R.string.goods_detail_pass)
                    tv_service_confirm.backgroundColor= ContextCompat.getColor(mContext,R.color.MaterialGrey600)
                    tv_service_confirm.isClickable=false
                }
                mServiceShopGoodsAdapter.goodsCount =it.goodsList.size
                mServiceShopGoodsAdapter.setNewData(it.goodsList)
                mRatingBar.rating = it.commentAverage.toFloat()
                tv_service_detail_score.text = String.format("%s${mContext.getString(R.string.UNIT_SCORE)}", it.commentAverage)
                tv_service_detail_commentCount.text = String.format(mContext.getString(R.string.service_detail_commentCount), it.commentCount)
                mServiceCommentAdapter.setNewData(it.commentList)
            }
        }
    }

    companion object {
        val Service_Intent_ServiceId = "Service_Intent_ServiceId"
    }
}
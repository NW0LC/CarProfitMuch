package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ServiceCommentAdapter
import com.exz.carprofitmuch.adapter.ServiceShopGoodsAdapter
import com.exz.carprofitmuch.bean.CommentBean
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.bean.ServiceShopBean
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity.Companion.GoodsCommentList_Intent_Id
import com.exz.carprofitmuch.module.main.store.comment.GoodsCommentListActivity.Companion.GoodsCommentList_Intent_IdMark
import com.exz.carprofitmuch.module.main.store.comment.ServiceCommentListActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceDetailActivity.Companion.Service_Intent_ServiceId
import com.exz.carprofitmuch.pop.CouponPop
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_shop.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceShopActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {

    private lateinit var couponPop: CouponPop
    private lateinit var mServiceShopGoodsAdapter: ServiceShopGoodsAdapter<ServiceGoodsBean>
    private lateinit var mServiceCommentAdapter: ServiceCommentAdapter<CommentBean>
    private var serviceStoreBean: ServiceShopBean?=null

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
        img.layoutParams.height=ScreenUtils.getScreenWidth()*15/32
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        couponPop = CouponPop(mContext)

        initRecycler()
        initEvent()
        onRefresh(refreshLayout)
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_service_goodsMore.setOnClickListener(this)
        bt_service_shop_comment.setOnClickListener(this)
        bt_coupon.setOnClickListener(this)
        tv_service_store_phone.setOnClickListener(this)
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
                val intent = Intent(this@ServiceShopActivity, ServiceDetailActivity::class.java)
                intent.putExtra(Service_Intent_ServiceId,mServiceShopGoodsAdapter.data[position].goodsId)
                startActivity(intent)
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_service_goodsMore -> {
                if (serviceStoreBean!=null)
                mServiceShopGoodsAdapter.goodsCount=serviceStoreBean?.goodsList?.size?:0
                bt_service_goodsMore.visibility=View.GONE
                mServiceShopGoodsAdapter.notifyDataSetChanged()
            }
            bt_service_shop_comment -> {
                val intent = Intent(this, ServiceCommentListActivity::class.java)
                intent.putExtra(GoodsCommentList_Intent_Id,serviceStoreBean?.shopId)
                intent.putExtra(GoodsCommentList_Intent_IdMark,"2")
                startActivity(intent)
            }
            tv_service_store_phone -> {
                if (serviceStoreBean!=null)
                    DialogUtils.Call(this,serviceStoreBean?.shopPhone?:"")

            }
            bt_coupon -> {
                var goodsIds=""
                for (serviceGoodsBean in serviceStoreBean?.goodsList?:ArrayList()) {
                    goodsIds+=serviceGoodsBean.goodsId+","
                }
                if (goodsIds.isNotEmpty()) {
                    goodsIds=goodsIds.substring(0,goodsIds.length-1)
                }
                DataCtrlClass.couponListData(serviceStoreBean?.shopId ?: "",  goodsIds) {
                    if (it != null) {
                        couponPop.data = it
                        couponPop.showPopupWindow()
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.serviceShopData(this,intent.getStringExtra(ServiceShop_Intent_ServiceShopId)?:"") {
            refreshLayout?.finishRefresh()
            if (it != null) {
                serviceStoreBean=it
                img.setImageURI(it.shopImgUrl)
                tv_service_shop_name.text=it.shopName
                tv_service_shop_address.text=it.shopAddress
                bt_coupon.visibility = if (it.isCoupon == "1") View.VISIBLE else View.GONE
                mRatingBar.rating = it.score.toFloat()
                tv_service_shop_score.text = String.format("%s${mContext.getString(R.string.UNIT_SCORE)}", it.score)
                tv_service_shop_type.text=String.format(getString(R.string.service_shop_type,it.goodsCount))
                tv_service_shop_commentCount.text=String.format(getString(R.string.service_shop_comment,it.commentCount))
                bt_service_goodsMore.visibility=if (it.goodsCount.toIntOrNull()?:0>3) View.VISIBLE else View.GONE
                tv_service_goodsMore.text=String.format(getString(R.string.service_shop_goods_footer),it.goodsCount.toIntOrNull()?:0)

                mServiceShopGoodsAdapter.setNewData(it.goodsList)
                mServiceCommentAdapter.setNewData(it.commentList)

            }

        }
    }
    companion object {
        val ServiceShop_Intent_ServiceShopId="ServiceShop_Intent_ServiceShopId"
    }
}
package com.exz.carprofitmuch.module.main.store.normal

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsShopAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.ShopBean
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopSearchResultActivity.Companion.GoodsShopSearchResult_Intent_Status
import com.exz.carprofitmuch.pop.GoodsShopClassifyPop
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.activity_goods_shop.*
import kotlinx.android.synthetic.main.layout_banner.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class GoodsShopActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private lateinit var mNewAdapter: GoodsShopAdapter<GoodsBean>
    private lateinit var mHotAdapter: GoodsShopAdapter<GoodsBean>
    private lateinit var goodsShopClassifyPop: GoodsShopClassifyPop
    private var recommends: ArrayList<GoodsBean>? = null
    private var shopBean: ShopBean? = null
    override fun setInflateId(): Int = R.layout.activity_goods_shop

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)

        scrollView.setOnScrollChangeListener(SZWUtils.MyNestedScrollListener(bottom_bar, SizeUtils.dp2px(45f)))
        return false
    }


    override fun init() {
        goodsShopClassifyPop = GoodsShopClassifyPop(this)
        goodsShopClassifyPop.shopId=intent.getStringExtra(GoodsShop_Intent_ShopId) ?: ""

        val mNsp = SpannableString(getString(R.string.goods_shop_newGoodsList))
        mNsp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.MaterialRed600)), mNsp.length - 1, mNsp.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_newGoodsList.text = mNsp
        val mHsp = SpannableString(getString(R.string.goods_shop_hotGoodsList))
        mHsp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.MaterialRed600)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_hotGoodsList.text = mHsp

        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initBanner()
        initRecycler()
        initEvent()
        onRefresh(refreshLayout)
    }

    private fun initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(BannerImageLoader())

        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        banner.setOnBannerListener {
            if (recommends != null) {
                if (SZWUtils.getMarkIntent(this@GoodsShopActivity, recommends?.get(it) as GoodsBean) != null)
                    startActivity(SZWUtils.getMarkIntent(this@GoodsShopActivity, recommends?.get(it)as GoodsBean))
            }

        }

    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        mRightImg.setOnClickListener(this)
        mLeftImg.setOnClickListener(this)
        mTitle.setOnClickListener(this)
        bt_goodsShop_favorite.setOnClickListener(this)
        bt_goodsShop_allGoods.setOnClickListener(this)
        bt_goodsShop_newGoods.setOnClickListener(this)
        bt_goodsShop_hotGoods.setOnClickListener(this)
        bt_goodsShop_detail.setOnClickListener(this)
        bt_goodsShop_classify.setOnClickListener(this)
        bt_goodsShop_phone.setOnClickListener(this)
    }

    private fun initRecycler() {
        mNewAdapter = GoodsShopAdapter()
        mHotAdapter = GoodsShopAdapter()
        mNewAdapter.bindToRecyclerView(mNewRecyclerView)
        mNewRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mNewRecyclerView.isNestedScrollingEnabled = false
        mNewRecyclerView.isFocusable = false

        mHotAdapter.bindToRecyclerView(mHotRecyclerView)
        mHotRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mHotRecyclerView.isNestedScrollingEnabled = false
        mHotRecyclerView.isFocusable = false


        mNewAdapter.onItemClickListener = this
        mHotAdapter.onItemClickListener = this
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
        if (SZWUtils.getMarkIntent(this@GoodsShopActivity, adapter.data[position] as GoodsBean) != null)
            startActivity(SZWUtils.getMarkIntent(this@GoodsShopActivity, adapter.data[position] as GoodsBean))
    }

    override fun onClick(p0: View?) {
        val shopSearchIntent = Intent(this, GoodsShopSearchResultActivity::class.java)
        when (p0) {
            mLeftImg -> {
                onBackPressed()
            }
            mTitle -> {
                shopSearchIntent.putExtra("isShowSoft", true)
                shopSearchIntent.putExtra(GoodsShop_Intent_ShopId,intent.getStringExtra(GoodsShop_Intent_ShopId) ?: "")
                startActivity(shopSearchIntent)
            }
            mRightImg -> {
//                startActivity(Intent(this, GoodsShopSearchResultActivity::class.java))
                goodsShopClassifyPop.showPopupWindow()
            }
            bt_goodsShop_favorite -> {
                if (shopBean != null) {
                    DataCtrlClass.editFavoriteData(this, shopBean?.shopId ?: "", "0",
                            if (shopBean?.isCollected == "1") {
                                shopBean?.isCollected = "0"
                                "0"
                            } else {
                                shopBean?.isCollected = "1"
                                "1"
                            }) {
                        bt_goodsShop_favorite.setText(if (shopBean?.isCollected == "1") R.string.goods_shop_favorite_remove else R.string.goods_shop_favorite_add)

                    }
                }
            }
            bt_goodsShop_allGoods -> {
                shopSearchIntent.putExtra(GoodsShop_Intent_ShopId,intent.getStringExtra(GoodsShop_Intent_ShopId) ?: "")
                startActivity(shopSearchIntent)
            }
            bt_goodsShop_newGoods -> {
                shopSearchIntent.putExtra(GoodsShop_Intent_ShopId,intent.getStringExtra(GoodsShop_Intent_ShopId) ?: "")
                shopSearchIntent.putExtra(GoodsShopSearchResult_Intent_Status,"1")
                startActivity(shopSearchIntent)
            }
            bt_goodsShop_hotGoods -> {
                shopSearchIntent.putExtra(GoodsShop_Intent_ShopId,intent.getStringExtra(GoodsShop_Intent_ShopId) ?: "")
                shopSearchIntent.putExtra(GoodsShopSearchResult_Intent_Status,"2")
                startActivity(shopSearchIntent)
            }
            bt_goodsShop_detail -> {//
                val shopId = intent.getStringExtra(GoodsShop_Intent_ShopId)
                val intent = Intent(this, MyWebActivity::class.java)
                intent.putExtra(Intent_Url, "${Urls.url}Mobile/ShopDetail.aspx?id=$shopId")
                intent.putExtra(Intent_Title, getString(R.string.goods_shop_detail))
                startActivity(intent)
            }
            bt_goodsShop_classify -> {//
                goodsShopClassifyPop.showPopupWindow()
            }
            bt_goodsShop_phone -> {//
                if (shopBean != null) {
                    DialogUtils.Call(this,shopBean?.shopPhone?:"")
                }
            }
            else -> {
            }
        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {

        DataCtrlClass.goodsShopClassifyData( intent.getStringExtra(GoodsShop_Intent_ShopId) ?: "") {
            if (it != null)
                goodsShopClassifyPop.data = it
        }
        DataCtrlClass.goodsShopData(this, intent.getStringExtra(GoodsShop_Intent_ShopId) ?: "") {
            refreshLayout?.finishRefresh()
            if (it != null) {
                shopBean = it
                img_goods_shop.setImageURI(it.shopImgUrl)
                tv_goodsShop_name.text = it.shopName
                tv_goodsShop_tag.text = it.shopMark
                tv_goodsShop_favoriteCount.text = String.format(getString(R.string.goods_shop_favoriteCount), it.collectedCount)
                tv_goodsShop_allGoodsCount.text = it.allCount
                tv_goodsShop_newGoodsCount.text = it.newCount
                tv_goodsShop_hotGoodsCount.text = it.hotCount
                bt_goodsShop_favorite.setText(if (it.isCollected == "1") R.string.goods_shop_favorite_remove else R.string.goods_shop_favorite_add)
                recommends = it.recommendGoods
                //设置图片集合
                banner.setImages(it.recommendGoods)
                //banner设置方法全部调用完毕时最后调用
                banner.start()

                mHotAdapter.setNewData(it.hotGoodsList)
                mNewAdapter.setNewData(it.newGoodsList)
            }
        }
    }

    companion object {
        val GoodsShop_Intent_ShopId = "GoodsShop_Intent_ShopId"
    }
}
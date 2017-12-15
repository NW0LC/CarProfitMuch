package com.exz.carprofitmuch.module

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.module.login.LoginActivity
import com.exz.carprofitmuch.module.main.promotion.PromotionsPersonalActivity
import com.exz.carprofitmuch.module.mine.*
import com.exz.carprofitmuch.module.mine.comment.MyCommentActivity
import com.exz.carprofitmuch.module.mine.comment.TreasureListActivity
import com.exz.carprofitmuch.module.mine.coupon.CouponActivity
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity
import com.exz.carprofitmuch.module.mine.favorite.FavoriteShopActivity
import com.exz.carprofitmuch.module.mine.goodsorder.GoodsOrderActivity
import com.exz.carprofitmuch.module.mine.returngoods.ReturnGoodsActivity
import com.exz.carprofitmuch.module.mine.shop.OpenShopActivity
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_mine.*
import kotlinx.android.synthetic.main.layout_progress_score.*
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MineFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private var mHasNews = false
    private var mOffset = 0
    private var overDate =""
    private var mScrollY = 0
    private var openState = "0"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_mine, container, false)
        return rootView
    }

    private var realScore = 0f
    private var unlockScore = 0f
    private var totalScore = 0f

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (MyApplication.checkUserLogin()) {
                getUserInfo()
            }
        }
    }

    override fun initView() {
        initBar()
        refreshLayout.setOnRefreshListener(this)
    }

    private fun getUserInfo() {
        DataCtrlClassXZW.getUserInfo(context) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                img_head.setImageURI(it.headerUrl)
                tv_userName.text = PreferencesService.getAccountKey(context)
                overDate=it.overDate
                tv_userInfo.text = String.format("${it.level}${if (it.overDate.isNotEmpty()) it.overDate + getString(R.string.mine_vip_pass) else ""}")//会员等级 -过期时间
                totalScore = it.scoreT.toFloat()//车险总积分
                realScore = it.scoreG.toFloat()//车险获得积分
                unlockScore = it.scoreL.toFloat()//车险解锁积分
                rootView.postDelayed({ SZWUtils.resetProgress(progressBar = progressBar, parentLayout = rootView, realScore = realScore, unlockScore = unlockScore, totalScore = totalScore, reset = reset) { reset = false } }, 2000)
                tv_myBalance.text = String.format(context.getString(R.string.CNY) + "%s", it.balance)//余额
                bt_tab_card_count.text = SZWUtils.setUnitTextColor(context, String.format(context.getString(R.string.unit_piece), it.wallet))//可用卡劵数量
                bt_tab_coupon_count.text = SZWUtils.setUnitTextColor(context, String.format(context.getString(R.string.unit_piece), it.coupon))//可用优惠券数量
                bt_tab_treasure_count.text = SZWUtils.setUnitTextColor(context, String.format(context.getString(R.string.unit_individual), it.treasure))//待领取宝藏数量
                bt_tab_score_count.text = SZWUtils.setUnitTextColor(context, String.format(context.getString(R.string.unit_individual), it.score))//我的积分
                openState = it.openState
                bt_applyFor_openShop.visibility = if (it.openState == "1") View.GONE else View.VISIBLE
                mHasNews = it.isMsg == "1"
                if (mHasNews) {
                    toolbar.menu.getItem(1)?.setIcon(R.mipmap.icon_mine_msg_on)
                } else {
                    toolbar.menu.getItem(1)?.setIcon(R.mipmap.icon_mine_msg_off)
                }

            }

        }
    }


    private fun initBar() {
        mTitle.text = getString(R.string.main_mine_name)
        //状态栏透明和间距处理
        StatusBarUtil.setPaddingSmart(context, toolbar)
        StatusBarUtil.setPaddingSmart(context, blurView)

        toolbar.navigationIcon = null
        toolbar.inflateMenu(R.menu.menu_mine)
        toolbar.setOnMenuItemClickListener(this)
        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }
        })
        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0
            private val h = DensityUtil.dp2px(170f)
            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                var scrollNewY = scrollY
                if (lastScrollY < h) {
                    scrollNewY = Math.min(h, scrollNewY)
                    mScrollY = if (scrollNewY > h) h else scrollNewY
                    parallax.translationY = (mOffset - mScrollY).toFloat()
                    blurView.alpha = 1f * mScrollY / h
                    buttonBarLayout.alpha = 1f * mScrollY / h
                }
                lastScrollY = scrollNewY
            }
        })
        buttonBarLayout.alpha = 0f
        blurView.alpha = 0f
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                startActivityForResult(Intent(context, SettingsActivity::class.java), 300)
            }
            R.id.action_notifications -> {
                startActivityForResult(Intent(context, MsgActivity::class.java), 100)
            }
        }
        return false
    }

    override fun initEvent() {
        img_head.setOnClickListener(this)
        bt_myBalance.setOnClickListener(this)
        bt_tab_card.setOnClickListener(this)
        bt_tab_coupon.setOnClickListener(this)
        bt_treasure.setOnClickListener(this)
        bt_tab_favoriteGoods.setOnClickListener(this)
        bt_tab_favoriteShop.setOnClickListener(this)
        bt_tab_myComment.setOnClickListener(this)
        bt_tab_score.setOnClickListener(this)
        bt_order_tab_1.setOnClickListener(this)
        bt_order_tab_2.setOnClickListener(this)
        bt_order_tab_3.setOnClickListener(this)
        bt_order_tab_4.setOnClickListener(this)
        bt_order_tab_5.setOnClickListener(this)
        bt_tab_history.setOnClickListener(this)
        bt_promotions.setOnClickListener(this)
        bt_applyFor_openShop.setOnClickListener(this)
        bt_guarantee_slip.setOnClickListener(this)
        bt_vip_recharge.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            img_head -> {// 个人资料
                val intent = Intent(context, PersonInfoActivity::class.java)
                SZWUtils.checkLogin(this, intent, PersonInfoActivity::class.java.name)
            }
            bt_vip_recharge -> {// vip充值
                val intent = Intent(context, PayVipActivity::class.java).putExtra("overDate",overDate)
                SZWUtils.checkLogin(this, intent, PayVipActivity::class.java.name)
            }
            bt_order_tab_1 -> {//我的订单-待付款
                startActivity(Intent(context, GoodsOrderActivity::class.java).putExtra("currentTab", 1))
            }
            bt_order_tab_2 -> {//我的订单-待收货
                startActivity(Intent(context, GoodsOrderActivity::class.java).putExtra("currentTab", 3))
            }
            bt_order_tab_3 -> {//我的订单-待评价
                startActivity(Intent(context, GoodsOrderActivity::class.java).putExtra("currentTab", 4))
            }
            bt_order_tab_4 -> {//退货退款
                startActivity(Intent(context, ReturnGoodsActivity::class.java))
            }
            bt_order_tab_5 -> {//全部订单
                startActivity(Intent(context, GoodsOrderActivity::class.java).putExtra("currentTab", 0))
            }

            bt_myBalance -> { //账户余额
                val intent = Intent(context, AccountBalanceActivity::class.java)
                SZWUtils.checkLogin(this, intent, AccountBalanceActivity::class.java.name)
            }
            bt_tab_coupon -> {//优惠券
                val intent = Intent(context, CouponActivity::class.java)
                SZWUtils.checkLogin(this, intent, CouponActivity::class.java.name)
            }
            bt_treasure -> {//我的宝藏
                startActivity(Intent(context, TreasureListActivity::class.java))
            }
            bt_tab_history -> {//我的足迹
                startActivity(Intent(context, FootprintActivity::class.java))

            }
            bt_tab_myComment -> {//我的评价
                startActivity(Intent(context, MyCommentActivity::class.java))

            }
            bt_tab_card -> {//卡券包
                startActivityForResult(Intent(context, CardPackageListActivity::class.java), 100)
            }
            bt_tab_favoriteGoods -> {//商品收藏
                startActivityForResult(Intent(context, FavoriteGoodsActivity::class.java), 100)
            }
            bt_tab_favoriteShop -> {//商铺收藏
                startActivityForResult(Intent(context, FavoriteShopActivity::class.java), 100)
            }
            bt_tab_score -> {//积分中心
                startActivityForResult(Intent(context, ScoreCenterActivity::class.java), 100)
            }
            bt_applyFor_openShop -> {//申请开店
                if (openState == "0")
                    context.toast("开店审核中..")
                else {
                    val intent = Intent(context, OpenShopActivity::class.java).putExtra(OpenShopActivity.OPENSTATE, openState)
                    SZWUtils.checkLogin(this, intent, OpenShopActivity::class.java.name)
                }
            }
            bt_promotions -> {//我的活动
                startActivity(Intent(context, PromotionsPersonalActivity::class.java))
            }
            bt_guarantee_slip -> {//我的保单
                startActivity(Intent(context, MyPolicyActivity::class.java))
            }

        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {
        if (MyApplication.checkUserLogin()) {
            reset = true
            getUserInfo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == LoginActivity.RESULT_LOGIN_CANCELED) {
            (activity as MainActivity).mainTabBar.currentTab = 0
        } else if (resultCode == Activity.RESULT_OK) {
            //刷新
            onRefresh(refreshLayout)
        }
    }

    override fun onDetach() {
        super.onDetach()
        reset = false
        SZWUtils.start = 0
        SZWUtils.secondStart = 0
    }

    companion object {
        var reset = false
        fun newInstance(): MineFragment {
            val bundle = Bundle()
            val fragment = MineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
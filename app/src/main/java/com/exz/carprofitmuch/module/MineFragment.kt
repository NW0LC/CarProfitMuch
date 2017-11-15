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
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.module.login.LoginActivity
import com.exz.carprofitmuch.module.mine.*
import com.exz.carprofitmuch.module.mine.coupon.CouponActivity
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity
import com.exz.carprofitmuch.module.mine.favorite.FavoriteShopActivity
import com.exz.carprofitmuch.module.mine.redpacket.RedPackageActivity
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_mine.*
import kotlinx.android.synthetic.main.layout_progress_score.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MineFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private var mHasNews = false
    private var mOffset = 0
    private var mScrollY = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_mine, container, false)
        return rootView
    }

    private var realScore = 700f
    private var unlockScore = 900f
    private var totalScore = 3000f

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            SZWUtils.resetProgress(progressBar = progressBar, parentLayout = rootView, realScore = realScore, unlockScore = unlockScore, totalScore = totalScore) {}
        }
    }
    override fun initView() {
        initBar()
        refreshLayout.setOnRefreshListener(this)
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
                startActivityForResult(Intent(context, SettingsActivity::class.java), 100)
            }
            R.id.action_notifications -> {
                mHasNews = !mHasNews
                if (mHasNews) {
                    toolbar.menu.getItem(1)?.setIcon(R.mipmap.icon_mine_msg_on)
                } else {
                    toolbar.menu.getItem(1)?.setIcon(R.mipmap.icon_mine_msg_off)
                }
            }
        }
        return false
    }

    override fun initEvent() {
        bt_header.setOnClickListener(this)
        bt_myBalance.setOnClickListener(this)
        bt_tab_card.setOnClickListener(this)
        bt_tab_coupon.setOnClickListener(this)
        bt_tab_redPacket.setOnClickListener(this)
        bt_tab_favoriteGoods.setOnClickListener(this)
        bt_tab_favoriteShop.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_header -> {// 个人资料
                startActivityForResult(Intent(context, PersonInfoActivity::class.java), 100)
            }
            bt_myBalance -> { //账户余额
                startActivityForResult(Intent(context, AccountBalanceActivity::class.java), 100)
            }
            bt_tab_coupon -> {//优惠券
                startActivity(Intent(context, CouponActivity::class.java))
            }
            bt_tab_redPacket -> {//红包
                startActivity(Intent(context, RedPackageActivity::class.java))
            }
            bt_tab_card -> {//卡券包
                startActivityForResult(Intent(context, CardPackageListActivity::class.java), 100)
            }
            bt_tab_favoriteGoods -> {//商品收藏
                startActivityForResult(Intent(context, FavoriteGoodsActivity::class.java), 100)
            }
            bt_tab_favoriteShop-> {//商铺收藏
                startActivityForResult(Intent(context, FavoriteShopActivity::class.java), 100)
            }
            bt_tab_score-> {//积分中心
                startActivityForResult(Intent(context, ScoreCenterActivity::class.java), 100)
            }

        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        realScore = 1500f
        unlockScore = 1500f
        totalScore = 3000f
        rootView.postDelayed({ SZWUtils.resetProgress(progressBar = progressBar, parentLayout = rootView, realScore = realScore, unlockScore = unlockScore, totalScore = totalScore) {} }, 2000)
        DataCtrlClass.mainStoreData(context) {
            if (it != null) {

            }
            refreshLayout?.finishRefresh()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == LoginActivity.RESULT_LOGIN_CANCELED) {
            (activity as MainActivity).mainTabBar.currentTab = 0
        } else if (resultCode == Activity.RESULT_OK) {
            //刷新 TODO
        }
    }

    override fun onDetach() {
        super.onDetach()
        SZWUtils.start = 0
        SZWUtils.secondStart = 0
    }

    companion object {
        fun newInstance(): MineFragment {
            val bundle = Bundle()
            val fragment = MineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
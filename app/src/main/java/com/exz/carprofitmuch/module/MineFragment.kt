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
import com.exz.carprofitmuch.module.mine.AccountBalanceActivity
import com.exz.carprofitmuch.module.mine.PersonInfoActivity
import com.exz.carprofitmuch.module.mine.SettingsActivity
import com.exz.carprofitmuch.module.mine.coupon.CouponActivity
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

    private var realScore = 1365f
    private var unlockScore = 2011f
    private var totalScore = 3865f
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden)
            SZWUtils.resetProgress(progressBar = progressBar, parentLayout = rootView, realScore = realScore, unlockScore = unlockScore, totalScore = totalScore) {}
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
                startActivityForResult(Intent(context,SettingsActivity::class.java),100)
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
            bt_tab_coupon.setOnClickListener(this)
        bt_tab_redPacket.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_header -> {
                startActivityForResult(Intent(context, PersonInfoActivity::class.java),100)
            }
            bt_myBalance -> {
                startActivityForResult(Intent(context, AccountBalanceActivity::class.java),100)
            }
            bt_tab_coupon -> {//优惠券
                startActivity(Intent(context, CouponActivity::class.java))
            }
            bt_tab_redPacket -> {//红包
                startActivity(Intent(context, RedPackageActivity::class.java))
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        realScore = 2365f
        unlockScore = 3011f
        totalScore = 3865f
        rootView.postDelayed({ SZWUtils.resetProgress(progressBar = progressBar, parentLayout = rootView, realScore = realScore, unlockScore = unlockScore, totalScore = totalScore) {} }, 2000)
        DataCtrlClass.mainStoreData(context) {
            if (it != null) {

            }
            refreshLayout?.finishRefresh()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== LoginActivity.RESULT_LOGIN_CANCELED){
            (activity as MainActivity).mainTabBar.currentTab=0
        }else if (resultCode== Activity.RESULT_OK){
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
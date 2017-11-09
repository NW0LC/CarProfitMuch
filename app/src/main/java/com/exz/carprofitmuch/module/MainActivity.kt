package com.exz.carprofitmuch.module

import android.support.v4.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.SizeUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.TabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*




class MainActivity : BaseActivity() {
    private val mTitles = arrayOf("首页", "商城", "购物车", "我的")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_home_off, R.mipmap.icon_store_off, R.mipmap.icon_goods_car_off, R.mipmap.icon_mine_off)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_home_on, R.mipmap.icon_store_on, R.mipmap.icon_goods_car_on, R.mipmap.icon_mine_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_main
    override fun init() {
//        SZWUtils.resetProgress(progressBar=progressBar,tagView = bar_tag,start = 0,end = 50,secondStart = 0,secondEnd = 100,animatorSpeed = 30,secondAnimatorSpeed = 15,delayMillis = 1000)
        mTitles.indices.mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        mFragments.add(MainFragment.newInstance())
        mFragments.add(StoreFragment.newInstance())
        mFragments.add(MainFragment.newInstance())
        mFragments.add(MineFragment.newInstance())
        mainTabBar.setTabData(mTabEntities, this, R.id.frameLayout, mFragments)
        mainTabBar.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                startAnimation(position)
            }

            override fun onTabReselect(position: Int) {
                startAnimation(position)
            }
        })
    }

    fun startAnimation(position: Int) {
        val iconAnimate = ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f)
        val iconAnimateMain = ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        val titleAnimate = ScaleAnimation(0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f)
        iconAnimate.duration = 50
        iconAnimateMain.duration = 50
        titleAnimate.duration = 50

        if (position == 0) {
            mainTabBar.getTitleView(0).visibility = View.GONE
            val lp = LinearLayout.LayoutParams(SizeUtils.dp2px(50f), SizeUtils.dp2px(50f))
            lp.topMargin = SizeUtils.dp2px(5f)
            mainTabBar.getIconView(0).layoutParams = lp
            mainTabBar.getIconView(0).startAnimation(iconAnimateMain)
            mainTabBar.getIconView(0).scaleType = ImageView.ScaleType.CENTER_INSIDE
        } else {
            mainTabBar.getTitleView(position).visibility = View.VISIBLE
            mainTabBar.getTitleView(0).visibility = View.VISIBLE
            val lp = LinearLayout.LayoutParams(SizeUtils.dp2px(37f), SizeUtils.dp2px(37f))
            mainTabBar.getIconView(0).layoutParams = lp
            mainTabBar.getIconView(0).scaleType = ImageView.ScaleType.FIT_CENTER
            mainTabBar.getTitleView(position).startAnimation(titleAnimate)
            mainTabBar.getIconView(position).startAnimation(iconAnimate)
        }
    }

}

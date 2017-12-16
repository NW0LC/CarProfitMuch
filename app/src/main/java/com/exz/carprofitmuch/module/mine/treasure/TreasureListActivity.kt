package com.exz.carprofitmuch.module.mine.comment

import android.support.v4.app.Fragment
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.TabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_coupon_list.*
import java.util.*

/**
 * Created by pc on 2017/11/9.
 * 我的宝藏
 */

class TreasureListActivity : BaseActivity() {
    private val mTitles = arrayOf("未领取", "已领取", "已过期")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_home_off, R.mipmap.icon_store_off, R.mipmap.icon_store_off)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_home_on, R.mipmap.icon_store_on, R.mipmap.icon_store_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.main_my_treasure)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_coupon_list

    override fun init() {
        initTabBar()
        initEvent()
    }

    private fun initTabBar() {
        mTitles.indices.mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        mFragments.add(TreasureListFragment.newInstance(0))
        mFragments.add(TreasureListFragment.newInstance(1))
        mFragments.add(TreasureListFragment.newInstance(2))
        mTabLayout.setTabData(mTabEntities, this, R.id.frameLayout, mFragments)
    }


    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
    }
}
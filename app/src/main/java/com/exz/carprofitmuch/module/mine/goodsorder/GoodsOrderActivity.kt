package com.exz.carprofitmuch.module.mine.goodsorder

import android.os.Bundle
import android.support.v4.app.Fragment
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.TabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_comment_list.*
import java.util.*

/**
 * Created by pc on 2017/11/15.
 * 我的订单
 */

class GoodsOrderActivity : BaseActivity() {
    private val mTitles = arrayOf("全部", "待付款", "待收货","待评价","已结束")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_home_off, R.mipmap.icon_store_off, R.mipmap.icon_goods_car_off, R.mipmap.icon_mine_off,R.mipmap.icon_mine_on)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_home_on, R.mipmap.icon_store_on, R.mipmap.icon_goods_car_on, R.mipmap.icon_mine_on,R.mipmap.icon_mine_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.mine_my_order)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_comment_list


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTabBar()
        initEvent()
    }

    private fun initTabBar() {
        mTitles.indices.mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        mFragments.add(GoodsOrderFragment.newInstance(0))
        mFragments.add(GoodsOrderFragment.newInstance(1))
        mFragments.add(GoodsOrderFragment.newInstance(2))
        mFragments.add(GoodsOrderFragment.newInstance(3))
        mFragments.add(GoodsOrderFragment.newInstance(4))
        mTabLayout.setTabData(mTabEntities, this, R.id.frameLayout, mFragments)
        if(intent.hasExtra("currentTab"))mTabLayout.currentTab=intent.getIntExtra("currentTab",0)
    }


    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
    }
}

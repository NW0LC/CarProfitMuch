package com.exz.carprofitmuch.module.main.promotion

import android.support.v4.app.Fragment
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.TabEntity
import com.flyco.tablayout.listener.CustomTabEntity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_coupon_list.*
import java.util.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class PromotionsPersonalActivity : BaseActivity() {

    private val mTitles = arrayOf("未开始", "已开始", "审核中", "已通过", "未通过", "已过期")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_home_off, R.mipmap.icon_store_off, R.mipmap.icon_goods_car_off, R.mipmap.icon_mine_off, R.mipmap.icon_mine_off, R.mipmap.icon_mine_off)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_home_on, R.mipmap.icon_store_on, R.mipmap.icon_goods_car_on, R.mipmap.icon_mine_on, R.mipmap.icon_mine_on, R.mipmap.icon_mine_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.promotion_personal_name)
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
        mFragments.add(PromotionsPersonalFragment.newInstance("1"))
        mFragments.add(PromotionsPersonalFragment.newInstance("2"))
        mFragments.add(PromotionsPersonalFragment.newInstance("3"))
        mFragments.add(PromotionsPersonalFragment.newInstance("4"))
        mFragments.add(PromotionsPersonalFragment.newInstance("5"))
        mFragments.add(PromotionsPersonalFragment.newInstance("6"))
        mTabLayout.setTabData(mTabEntities, this, R.id.frameLayout, mFragments)
    }


    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
    }
}
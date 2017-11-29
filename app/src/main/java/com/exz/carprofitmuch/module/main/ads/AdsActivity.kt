package com.exz.carprofitmuch.module.main.ads

import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.SizeUtils
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.layout_ads_tab.view.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class AdsActivity : BaseActivity(){

    private val mTitles = arrayOf("首页", "消息")
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
//        mTitle.text = getString(R.string.ads_name)
        val tabsLay = View.inflate(mContext, R.layout.layout_ads_tab, null)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity=Gravity.CENTER_VERTICAL
        tabsLay.layoutParams=layoutParams
        val params= ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,SizeUtils.dp2px(32f))
        tabsLay.tab.layoutParams=params
        buttonBarLayout.addView( tabsLay,0)


        mFragments.add(AdsFreeFragment.newInstance("0"))
        mFragments.add(AdsFreeFragment.newInstance("1"))
        tabsLay.tab.setTabData(mTitles,this,R.id.frameLayout,mFragments)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        return false
    }

    override fun setInflateId(): Int= R.layout.activity_ads

    override fun init() {
        initEvent()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }
}
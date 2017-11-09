package com.exz.carprofitmuch.module.login

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.User
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants.Result.Intent_ClassName
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

internal class LoginActivity : BaseActivity() {
    private var mOffset = 0
    private var mScrollY = 0
    private val list = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_login

    override fun init() {

        val tabTitles = arrayOf("登录", "注册")
        list.add(LoginFragment.newInstance(viewpager))
        list.add(RegisterFragment.newInstance(viewpager))
        tabs.setViewPager(viewpager, tabTitles, this, list)
        viewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                viewpager.resetHeight(position)
            }
        })


        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
            }
        })
        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0
            private val h = DensityUtil.dp2px(170f)
            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                var ScrollY = scrollY
                if (lastScrollY < h) {
                    ScrollY = Math.min(h, ScrollY)
                    mScrollY = if (ScrollY > h) h else ScrollY
                    parallax.translationY = (mOffset - mScrollY).toFloat()
                }
                lastScrollY = ScrollY
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除注册读取短信Observer
        contentResolver.unregisterContentObserver((list[1] as RegisterFragment).smsContentObserver)
    }
companion object {
    private val RESULT_LOGIN_OK=2000
    val RESULT_LOGIN_CANCELED=3000

    fun loginSuccess(context: Activity,mobile:String ,user:User?){
        PreferencesService.saveAccount(context, mobile, "")
        PreferencesService.saveAutoLoginToken(context, user?.autoLoginToken?:"")
        val intent = context.intent
        val className = intent.getStringExtra(Intent_ClassName)
        if (!TextUtils.isEmpty(className)) {
            intent.setClassName(context, className)
            context.startActivityForResult(intent,100)
        }else{
        context.setResult(RESULT_LOGIN_OK,intent)
        context.finish()
        context.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
    }
    }
}
    override fun onBackPressed() {
        setResult(RESULT_LOGIN_CANCELED)
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100){
            setResult(RESULT_LOGIN_OK,data)
            finish()
        }
    }
}

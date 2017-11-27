package com.exz.carprofitmuch.module.main.promotion

import android.view.View
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.BannersBean
import com.exz.carprofitmuch.imageloader.BannerImageLoader
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_promotions_detail.*
import kotlinx.android.synthetic.main.layout_banner.*

/**
 * Created by 史忠文
 * on 2017/11/8.
 */
class PromotionsDetailActivity : BaseActivity(), View.OnClickListener {


    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.promotion_detail)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_promotions_detail

    override fun init() {
        initBanner(null)
        bt_submit.setOnClickListener(this)
    }

    private fun initBanner(bannersBean: ArrayList<BannersBean>?) {
        val bannersBeans = ArrayList<BannersBean>()
        if (bannersBean?.isEmpty() == true) {
            bannersBeans.add(BannersBean())
            bannersBeans.add(BannersBean())
            bannersBeans.add(BannersBean())
        }

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(BannerImageLoader())
        //设置图片集合
        banner.setImages(bannersBean)
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //banner设置方法全部调用完毕时最后调用
        banner.start()

    }

    override fun onClick(view: View) {
        when (view) {

        }
    }


}
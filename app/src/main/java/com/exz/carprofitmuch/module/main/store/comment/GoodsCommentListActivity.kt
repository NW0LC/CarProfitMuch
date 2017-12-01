package com.exz.carprofitmuch.module.main.store.comment

import android.support.v4.app.Fragment
import com.exz.carprofitmuch.DataCtrlClass
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

class GoodsCommentListActivity : BaseActivity(){

    private val mTitles = arrayOf("全部", "晒图", "低分", "最新")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_home_off, R.mipmap.icon_store_off, R.mipmap.icon_goods_car_off, R.mipmap.icon_mine_off)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_home_on, R.mipmap.icon_store_on, R.mipmap.icon_goods_car_on, R.mipmap.icon_mine_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.service_comment_list_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_comment_list

    override fun init() {
        initTabBar()
        initEvent()
        DataCtrlClass.commentCountPwd(this,intent.getStringExtra(GoodsCommentList_Intent_Id)?:"",intent.getStringExtra(GoodsCommentList_Intent_IdMark)?:""){
            if (it!=null){
                (mTabEntities[0] as TabEntity).title=mTitles[0]+"("+it.total+")"
                (mTabEntities[1] as TabEntity).title=mTitles[1]+"("+it.hasImg+")"
                (mTabEntities[2] as TabEntity).title=mTitles[2]+"("+it.lowScore+")"
            }
            mTabLayout.setTabData(mTabEntities)
        }
    }

    private fun initTabBar() {
        mTitles.indices.mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        mFragments.add(GoodsCommentListFragment.newInstance(intent.getStringExtra(GoodsCommentList_Intent_Id)?:"",intent.getStringExtra(GoodsCommentList_Intent_IdMark)?:"","0"))
        mFragments.add(GoodsCommentListFragment.newInstance(intent.getStringExtra(GoodsCommentList_Intent_Id)?:"",intent.getStringExtra(GoodsCommentList_Intent_IdMark)?:"","1"))
        mFragments.add(GoodsCommentListFragment.newInstance(intent.getStringExtra(GoodsCommentList_Intent_Id)?:"",intent.getStringExtra(GoodsCommentList_Intent_IdMark)?:"","2"))
        mFragments.add(GoodsCommentListFragment.newInstance(intent.getStringExtra(GoodsCommentList_Intent_Id)?:"",intent.getStringExtra(GoodsCommentList_Intent_IdMark)?:"","3"))
        mTabLayout.setTabData(mTabEntities, this, R.id.frameLayout, mFragments)
    }


    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
    }
    companion object {
        val GoodsCommentList_Intent_Id="GoodsCommentList_Intent_Id"
        val GoodsCommentList_Intent_IdMark="GoodsCommentList_Intent_IdMark"
    }
}
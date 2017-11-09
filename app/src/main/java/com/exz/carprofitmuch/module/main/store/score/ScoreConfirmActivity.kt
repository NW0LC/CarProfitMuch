package com.exz.carprofitmuch.module.main.store.score

import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_confirm.*



/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ScoreConfirmActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {

    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.score_confirm_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)


        return false
    }

    override fun setInflateId(): Int= R.layout.activity_score_confirm

    override fun init() {
        val scoreConfirmAddressDetail = getString(R.string.score_confirm_address_detail)
        val msp = SpannableString(scoreConfirmAddressDetail+"更换当前号码将从手机发送一条普通短信进行验证")
        msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.MaterialGrey700)), 0, scoreConfirmAddressDetail.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_address.text = msp



        initEvent()

    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_choose_type.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_confirm -> {

            }

        }
    }
    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreStoreData(this) {
            if (it != null) {
                val scoreConfirmAddressDetail = getString(R.string.score_confirm_address_detail)
                val msp = SpannableString(scoreConfirmAddressDetail + "更换当前号码将从手机发送一条普通短信进行验证")
                msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.MaterialGrey700)), 0, scoreConfirmAddressDetail.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_address.text = msp
            }
        }
    }
}
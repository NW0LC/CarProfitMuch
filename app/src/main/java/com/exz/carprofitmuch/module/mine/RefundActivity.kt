package com.exz.carprofitmuch.module.mine

import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_refund.*

/**
 * Created by pc on 2017/11/21.
 * 申请退款
 */

class RefundActivity : BaseActivity() {
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.mine_return_total)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        toolbar.setNavigationOnClickListener { finish() }
        return false
    }


    override fun setInflateId(): Int {
        return R.layout.activity_refund
    }
}

package com.exz.carprofitmuch.module.mine

import android.content.Intent
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_account_balance.*

/**
 * Created by 史忠文
 * on 2017/11/8.
 */
class AccountBalanceActivity : BaseActivity(), View.OnClickListener {


    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.mine_balance)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_account_balance

    override fun init() {
        bt_recharge_money.setOnClickListener(this)
        bt_request_withdrawal.setOnClickListener(this)
        bt_balance_change.setOnClickListener(this)
        initBalance()
    }

    override fun onClick(view: View) {
        when (view) {
            bt_recharge_money -> startActivity(Intent(mContext, PayBalanceRechargeActivity::class.java))
            bt_request_withdrawal -> startActivity(Intent(mContext, AccountBalanceWithdrawalActivity::class.java))
            bt_balance_change -> {
                startActivity(Intent(this, AccountBalanceRecordActivity::class.java))
            }
        }
    }

    /**
     * 请求余额
     */
    private fun initBalance() {
        DataCtrlClass.accountBalance(this) {
                tv_balance.text=it
        }
    }

    override fun onResume() {
        super.onResume()
        initBalance()
    }

}
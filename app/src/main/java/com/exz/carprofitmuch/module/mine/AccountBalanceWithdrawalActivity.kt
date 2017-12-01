package com.exz.carprofitmuch.module.mine

import android.text.TextUtils
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.module.main.pay.PayActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_account_balance_withdrawal.*

/**
 * Created by 史忠文
 * on 2017/8/18.
 * 申请提现
 */
class AccountBalanceWithdrawalActivity : PayActivity(), View.OnClickListener {

    override fun setInflateId() = R.layout.activity_account_balance_withdrawal
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.balance_withdrawal_name)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
        tv_price.text = String.format("%s", intent.getStringExtra(Balance))
        bt_confirm.setOnClickListener(this)
    }

    companion object {
        var Balance = "balance"
    }

    override fun onClick(p0: View?) {
        val applyMoney = tv_withdrawal_price.text.toString().trim()
        if (TextUtils.isEmpty(applyMoney)) {
            tv_withdrawal_price.setShakeAnimation()
            return
        }
        val bankCardNum = tv_withdrawal_cardNum.text.toString().trim()
        if (TextUtils.isEmpty(bankCardNum)) {
            tv_withdrawal_cardNum.setShakeAnimation()
            return
        }
        val bankName = tv_withdrawal_bankName.text.toString().trim()
        if (TextUtils.isEmpty(bankName)) {
            tv_withdrawal_bankName.setShakeAnimation()
            return
        }
        val bankAddress = tv_withdrawal_bankAddress.text.toString().trim()
        if (TextUtils.isEmpty(bankAddress)) {
            tv_withdrawal_bankAddress.setShakeAnimation()
            return
        }
        val userName = tv_withdrawal_cardUserName.text.toString().trim()
        if (TextUtils.isEmpty(userName)) {
            tv_withdrawal_cardUserName.setShakeAnimation()
            return
        }
        val userPhone = tv_withdrawal_cardUserPhone.text.toString().trim()
        if (TextUtils.isEmpty(userPhone)) {
            tv_withdrawal_cardUserPhone.setShakeAnimation()
            return
        }

        DataCtrlClass.requestWithdrawal(this,
                applyMoney,
                bankCardNum,
                bankName,
                bankAddress,
                userName,
                userPhone) {
            if (it != null) {
                onBackPressed()
            }
        }
    }


}
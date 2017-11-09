package com.exz.carprofitmuch.module.mine

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
        bt_confirm.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        DataCtrlClass.requestWithdrawal(this,
                tv_price.text.toString(),
                tv_withdrawal_cardNum.text.toString(),
                tv_withdrawal_bankName.text.toString(),
                tv_withdrawal_bankAddress.text.toString(),
                tv_withdrawal_cardUserName.text.toString(),
                tv_withdrawal_cardUserPhone.text.toString()){
            if (it!=null) {
                onBackPressed()
            }
        }
    }


}
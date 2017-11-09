package com.exz.carprofitmuch.module.mine

import android.content.Intent
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.module.main.pay.PwdGetCodeActivity
import com.exz.carprofitmuch.module.main.pay.PwdSetActivity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_account_safe.*

/**
 * Created by 史忠文
 * on 2017/11/8.
 */
class AccountSafeActivity : BaseActivity(), View.OnClickListener {


    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.settings_name)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_account_safe

    override fun init() {
        bt_login_pwd.setOnClickListener(this)
        bt_pay_set_pwd.setOnClickListener(this)
        bt_pay_change_pwd.setOnClickListener(this)
        checkHavePayPwd()
    }

    override fun onClick(view: View) {
        when (view) {
           bt_login_pwd -> startActivity(Intent(mContext, ReAccountPwdActivity::class.java))
           bt_pay_set_pwd -> startActivity(Intent(mContext, PwdGetCodeActivity::class.java))
           bt_pay_change_pwd -> {
                val intent = Intent(this, PwdSetActivity::class.java)
                intent.putExtra(PwdSetActivity.PwdSetActivity_Type, PwdSetActivity.PwdSetActivity_Type_1)
                startActivity(intent)
            }
        }
    }

    /**
     * 是否有支付密码
     */
    private fun checkHavePayPwd() {
        DataCtrlClass.checkHavePayPwd(this){
            when (it) {
                "0" -> {
                    PwdSetActivity.IsSetPwd = true
                    bt_pay_set_pwd.text = getString(R.string.account_pwd_set)
                }
                "1" -> {
                    PwdSetActivity.IsSetPwd = false
                    bt_pay_set_pwd.text = getString(R.string.account_pwd_forget)
                }
                else -> {
                    bt_pay_set_pwd.text = getString(R.string.account_pwd_null)
                    bt_pay_set_pwd.isClickable=false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkHavePayPwd()
    }

}
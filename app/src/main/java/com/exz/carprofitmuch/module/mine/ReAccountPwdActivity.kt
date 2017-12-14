package com.exz.carprofitmuch.module.mine

import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_reset_pwd.*
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2017/11/8.
 */
class ReAccountPwdActivity : BaseActivity() {
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.account_pwd_change_login)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_reset_pwd
    override fun init() {
        bt_confirm.setOnClickListener{
            when {
                et_old_pwd.text.isEmpty() -> et_old_pwd.setShakeAnimation()
                et_new_pwd.text.isEmpty() -> et_new_pwd.setShakeAnimation()
                et_new_pwd_again.text.isEmpty() -> et_new_pwd_again.setShakeAnimation()
                et_new_pwd.text!=et_new_pwd_again.text -> toast("两次密码不一致")
                else -> {
                    DataCtrlClass.changeAccountPwd(this,et_old_pwd.text.toString(),et_new_pwd.text.toString()){
                        if (null!=it){
                            finish()
                        }
                    }
                }
            }
        }
    }
}